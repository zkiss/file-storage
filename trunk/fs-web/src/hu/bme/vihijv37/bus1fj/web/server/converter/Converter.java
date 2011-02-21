package hu.bme.vihijv37.bus1fj.web.server.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.Accessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Converter {

    private static final Log log = LogFactory.getLog(Converter.class);

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object source) throws ConverterException {
	if (Converter.log.isTraceEnabled()) {
	    Converter.log.trace("User called convert: " + source);
	}
	return (T) new Converter(source, ConverterConfig.getBurntInConfig()).convert();
    }

    private static boolean isLazyInit(Object propertyValue) {
	boolean lazyInit;
	try {
	    propertyValue.toString();
	    if (propertyValue instanceof Collection<?>) {
		Collection<?> c = (Collection<?>) propertyValue;
		if (c.iterator().hasNext()) {
		    c.iterator().next();
		}
	    }
	    lazyInit = false;
	} catch (Exception e) {
	    Converter.log.debug("Lazy initialized value, skipping");
	    lazyInit = true;
	}
	return lazyInit;
    }

    /**
     * A JavaBean, amit át kell alakítani
     */
    private final Object source;

    /**
     * A {@link #source}-ból ilyen típusú JavaBean készül
     */
    private final Class<?> targetClass;

    /**
     * A már átalakított objektumok
     */
    private final ConverterMemory memory;

    /**
     * Az átalakítandó JavaBean átalakítási szabályait leíró {@link Conversion}
     */
    private final Conversion conversion;

    /**
     * Koverziós szabályok
     */
    private final ConverterConfig config;

    private Converter(Object source, ConverterConfig config) {
	this(source, config, new ConverterMemory());
    }

    private Converter(Object source, ConverterConfig config, ConverterMemory memory) {
	this.source = source;
	this.config = config;
	this.memory = memory;
	this.conversion = this.config.getConversion(this.source.getClass());
	this.targetClass = this.conversion.getConversionDefinition().convert(this.source.getClass());
    }

    @SuppressWarnings("unchecked")
    private <T> T convert() throws ConverterException {
	if (Converter.log.isTraceEnabled()) {
	    Converter.log.trace("Converting " + this.source + " to " + this.targetClass);
	}
	try {
	    T ret;
	    if (this.conversion.getConversionDefinition().isEnum()) {
		ret = (T) this.convertEnum((Enum<?>) this.source);
		this.memory.put(this.source, ret);
	    } else {
		ret = (T) this.convertClass();
	    }
	    if (Converter.log.isTraceEnabled()) {
		Converter.log.trace("converted: " + ret);
	    }
	    return ret;
	} catch (InstantiationException e) {
	    throw new ConverterException("Could not instantiate class", e);
	} catch (IllegalAccessException e) {
	    throw new ConverterException("Could not instantiate class", e);
	} catch (IllegalArgumentException e) {
	    throw new ConverterException("Could not invoke getter", e);
	} catch (SecurityException e) {
	    throw new ConverterException("Could not set value", e);
	}
    }

    @SuppressWarnings("unchecked")
    private <T> T convertClass() throws InstantiationException, IllegalAccessException, ConverterException {
	T ret = (T) this.targetClass.newInstance();
	this.memory.put(this.source, ret);
	for (String property : this.conversion.getProperties()) {
	    Accessor srcAccessor = this.conversion.getAccessor(this.source.getClass(), property);
	    Accessor targetAccessor = this.conversion.getAccessor(this.targetClass, property);

	    Object getterValue = srcAccessor.getValue(this.source);
	    if ((getterValue != null) && !Converter.isLazyInit(getterValue)) {
		Object converted = this.convertProperty(getterValue, targetAccessor.getTargetClass());
		if (converted != null) {
		    targetAccessor.setValue(ret, converted);
		}
	    }
	}
	return ret;
    }

    private Collection<?> convertCollection(Collection<?> collection, Class<?> targetClass) throws InstantiationException, IllegalAccessException,
	    ConverterException {
	if (Converter.log.isTraceEnabled()) {
	    Converter.log.trace("converting collection: " + collection);
	}
	Collection<Object> ret = this.createCollection(targetClass);
	for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();) {
	    Object element = iterator.next();
	    if (this.config.getConversion(element.getClass()) != null) {
		Object converted = new Converter(element, this.config, this.memory).convert();
		ret.add(converted);
	    } else {
		ret.add(element);
	    }
	}

	return ret;
    }

    @SuppressWarnings("unchecked")
    private <T> T convertEnum(Enum<?> source) {
	/*
	 * Ordinal alapján történik a konverzió
	 */
	Object[] targetEnums = this.targetClass.getEnumConstants();
	return (T) targetEnums[source.ordinal()];
    }

    private Object convertProperty(Object getterValue, Class<?> targetClass) throws InstantiationException, IllegalAccessException, ConverterException {
	Object converted = this.memory.getConvertedValue(getterValue);
	if (converted == null) {
	    if (getterValue instanceof Collection<?>) {
		converted = this.convertCollection((Collection<?>) getterValue, targetClass);
		this.memory.put(getterValue, converted);
	    } else if (this.config.getConversion(getterValue.getClass()) != null) {
		converted = new Converter(getterValue, this.config, this.memory).convert();
		this.memory.put(getterValue, converted);
	    } else {
		converted = getterValue;
	    }
	}
	return converted;
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> createCollection(Class<?> collectionClass) throws InstantiationException, IllegalAccessException {
	Collection<Object> ret;
	if (collectionClass.isInterface()) {
	    if (collectionClass.equals(List.class) || collectionClass.equals(Queue.class)) {
		ret = new LinkedList<Object>();
	    } else if (collectionClass.equals(Set.class)) {
		ret = new HashSet<Object>();
	    } else {
		// Collection
		ret = new LinkedList<Object>();
	    }
	} else {
	    ret = (Collection<Object>) collectionClass.newInstance();
	}
	return ret;
    }

}
