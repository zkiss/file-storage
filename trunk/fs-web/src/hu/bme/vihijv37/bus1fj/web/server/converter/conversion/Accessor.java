package hu.bme.vihijv37.bus1fj.web.server.converter.conversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hu.bme.vihijv37.bus1fj.web.server.converter.ConverterException;

/**
 * Hozzáférést biztosít egy property értékéhez
 * 
 * @author Zoltan Kiss
 */
public abstract class Accessor {

    private final Method getter;

    private final Class<?> targetClass;

    public Accessor(Method getter) {
	this.getter = getter;
	this.targetClass = getter.getReturnType();
    }

    /**
     * A tulajdonság típusa
     * 
     * @return
     */
    public Class<?> getTargetClass() {
	return this.targetClass;
    }

    /**
     * Property értékének lekérdezése
     * 
     * @param target
     * @return
     * @throws ConverterException
     */
    public final Object getValue(Object target) throws ConverterException {
	try {
	    return this.getter.invoke(target);
	} catch (IllegalArgumentException e) {
	    throw new ConverterException("Could not invoke getter");
	} catch (IllegalAccessException e) {
	    throw new ConverterException("Could not invoke getter");
	} catch (InvocationTargetException e) {
	    throw new ConverterException("Could not invoke getter");
	}
    }

    /**
     * Property értékének beállítása
     * 
     * @param target
     *            az objektum, amin a property értéke be lesz állítva
     * @param value
     *            a property új értéke
     * @throws ConverterException
     */
    public abstract void setValue(Object target, Object value) throws ConverterException;

}
