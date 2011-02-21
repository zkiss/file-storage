package hu.bme.vihijv37.bus1fj.web.server.converter.conversion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import hu.bme.vihijv37.bus1fj.web.server.converter.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FieldAccessor extends Accessor {

    private static final Log log = LogFactory.getLog(FieldAccessor.class);

    private final Field field;

    public FieldAccessor(Method getter, Field field) {
	super(getter);
	this.field = field;
    }

    @Override
    public void setValue(Object target, Object value) throws ConverterException {
	if (FieldAccessor.log.isTraceEnabled()) {
	    FieldAccessor.log.trace("trying to set field value: " + this.field.getName());
	}
	int modifiers = this.field.getModifiers();
	if (!Modifier.isPublic(modifiers)) {
	    this.field.setAccessible(true);
	}
	try {
	    this.field.set(target, value);
	} catch (IllegalArgumentException e) {
	    throw new ConverterException("Could not set field value", e);
	} catch (IllegalAccessException e) {
	    throw new ConverterException("Could not set field value", e);
	} finally {
	    if (!Modifier.isPublic(modifiers)) {
		this.field.setAccessible(false);
	    }
	}
    }

}
