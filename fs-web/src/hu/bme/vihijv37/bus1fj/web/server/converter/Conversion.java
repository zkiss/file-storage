package hu.bme.vihijv37.bus1fj.web.server.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.TreeMap;
import java.util.TreeSet;

import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.Accessor;
import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.ConversionDefinition;
import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.FieldAccessor;
import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.MethodAccessor;
import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.Skip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JavaBean-ek közti konvertálást írja le
 * 
 * @author Zoltan Kiss
 */
class Conversion {

    private static final Log log = LogFactory.getLog(Conversion.class);

    private static Field getField(Class<?> clazz, String fieldName) throws SecurityException, NoSuchFieldException {
	Field ret = null;
	try {
	    ret = clazz.getDeclaredField(fieldName);
	} catch (NoSuchFieldException e) {
	    // ősosztályt nézzük
	    if (clazz.getSuperclass() == null) {
		throw e;
	    }
	    ret = Conversion.getField(clazz.getSuperclass(), fieldName);
	}
	return ret;
    }

    private static String getPropertyKey(Class<?> clazz, String property) {
	return clazz.getName() + "." + property;
    }

    private static Method getSetter(Class<?> clazz, String property, Method getter) throws SecurityException, NoSuchMethodException {
	String setterName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
	return clazz.getDeclaredMethod(setterName, getter.getReturnType());
    }

    private static String getterToProperty(String methodName) {
	String ret;
	if (methodName.startsWith("is")) {
	    ret = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
	} else {
	    ret = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
	}
	return ret;
    }

    private static boolean isGetter(Method method) {
	String methodName = method.getName();

	boolean ret = true;
	Method[] objectMethods = Object.class.getMethods();
	for (int i = 0; ret && (i < objectMethods.length); i++) {
	    ret = !methodName.equals(objectMethods[i].getName());
	}

	if (ret) {
	    ret = (methodName.startsWith("get") && //
		    Character.isUpperCase(methodName.charAt(3)) && //
		    (method.getParameterTypes().length == 0))
		    || (methodName.startsWith("is") && //
			    Character.isUpperCase(methodName.charAt(2)) && //
		    (method.getParameterTypes().length == 0));
	}

	return ret;
    }

    private ConversionDefinition conversionDefinition;

    private final TreeSet<String> properties;

    /**
     * Kulcs: {@link #getPropertyKey(Class, String)}<br>
     * Érték: {@link Accessor}
     */
    private final TreeMap<String, Accessor> propertyAccessors;

    public Conversion(ConversionDefinition conversionDefinition) {
	this.conversionDefinition = conversionDefinition;
	if (conversionDefinition.isEnum()) {
	    this.properties = null;
	    this.propertyAccessors = null;
	} else {
	    this.properties = new TreeSet<String>();
	    this.propertyAccessors = new TreeMap<String, Accessor>();
	    this.discoverProperties();
	}
    }

    private void checkProperties(Class<?> clazz) {
	for (String property : this.properties) {
	    if (this.getAccessor(clazz, property) == null) {
		Conversion.log.warn("No accessor was found for property: " + Conversion.getPropertyKey(clazz, property));
	    }
	}
    }

    private void determineAccessor(Class<?> clazz, String property, Method getter) throws ConverterException {
	Accessor accessor = null;
	try {
	    // előbb setterrel próbálkozunk
	    Method setter = Conversion.getSetter(clazz, property, getter);
	    accessor = new MethodAccessor(getter, setter);
	    Conversion.log.trace("Using MethodAccessor for property: " + Conversion.getPropertyKey(clazz, property));
	} catch (Exception e) {
	    // field-del is próbálkozunk
	}

	if (accessor == null) {
	    // field
	    Field field;
	    try {
		field = Conversion.getField(clazz, property);
		if (!getter.getReturnType().isAssignableFrom(field.getType())) {
		    throw new ConverterException("Field for property " + Conversion.getPropertyKey(clazz, property)
			    + " is incompatible with getter's return type: " + getter.getName());
		}
		accessor = new FieldAccessor(getter, field);
		Conversion.log.trace("Using FieldAccessor for property: " + Conversion.getPropertyKey(clazz, property));
	    } catch (Exception e) {
		// nincs field
		throw new ConverterException("No setter or field was found for property: " + Conversion.getPropertyKey(clazz, property), e);
	    }
	}

	this.propertyAccessors.put(Conversion.getPropertyKey(clazz, property), accessor);
    }

    private void discoverProperties() {
	this.discoverProperties(this.conversionDefinition.class1);
	this.discoverProperties(this.conversionDefinition.class2);
	this.checkProperties(this.conversionDefinition.class1);
	this.checkProperties(this.conversionDefinition.class2);
    }

    private void discoverProperties(Class<?> clazz) {
	// ami property, annak van getter
	for (Method method : clazz.getMethods()) {
	    if (Conversion.isGetter(method)) {
		String property = Conversion.getterToProperty(method.getName());
		if (!(this.conversionDefinition.getRule(property) instanceof Skip)) {
		    this.properties.add(property);
		    try {
			this.determineAccessor(clazz, property, method);
		    } catch (ConverterException e) {
			if (Conversion.log.isTraceEnabled()) {
			    Conversion.log.trace("Could not determine accessor for property: " + Conversion.getPropertyKey(clazz, property));
			}
		    }
		} else {
		    Conversion.log.trace("Property skipped: " + Conversion.getPropertyKey(clazz, property));
		}
	    }
	}
    }

    /**
     * A JavaBean-ek tulajdonságait lekérdező és módosító objektum lekérdezése
     * 
     * @param clazz
     * @param property
     * @return
     */
    public Accessor getAccessor(Class<?> clazz, String property) {
	return this.propertyAccessors.get(Conversion.getPropertyKey(clazz, property));
    }

    public ConversionDefinition getConversionDefinition() {
	return this.conversionDefinition;
    }

    /**
     * A JavaBean-ek tulajdonságai
     * 
     * @return
     */
    public TreeSet<String> getProperties() {
	return this.properties;
    }

}
