package hu.bme.vihijv37.bus1fj.web.server.converter.conversion;

import java.util.TreeMap;

public class ConversionDefinition {

    public final Class<?> class1;

    public final Class<?> class2;

    private final TreeMap<String, ConversionRule> conversionRules;

    public ConversionDefinition(Class<?> c1, Class<?> c2, ConversionRule... conversionRules) {
	this.class1 = c1;
	this.class2 = c2;
	this.conversionRules = new TreeMap<String, ConversionRule>();
	for (ConversionRule conversionRule : conversionRules) {
	    this.conversionRules.put(conversionRule.property, conversionRule);
	}
    }

    public Class<?> convert(Class<?> clazz) {
	Class<?> ret = null;
	if (this.class1.equals(clazz)) {
	    ret = this.class2;
	} else if (this.class2.equals(clazz)) {
	    ret = this.class1;
	}
	return ret;
    }

    @Override
    public boolean equals(Object obj) {
	boolean ret;
	if (obj == null) {
	    ret = false;
	} else if (obj == this) {
	    ret = true;
	} else if (obj instanceof ConversionDefinition) {
	    ConversionDefinition other = (ConversionDefinition) obj;
	    ret = (this.class1.equals(other.class1) && this.class2.equals(other.class2)) || //
		    (this.class1.equals(other.class2) && this.class2.equals(other.class1));
	} else {
	    ret = false;
	}
	return ret;
    }

    public ConversionRule getRule(String property) {
	return this.conversionRules.get(property);
    }

    @Override
    public int hashCode() {
	return this.class1.hashCode() + this.class2.hashCode();
    }

    public boolean isEnum() {
	return this.class1.isEnum() && this.class2.isEnum();
    }

    @Override
    public String toString() {
	return this.class1.getSimpleName() + " <-> " + this.class2.getSimpleName();
    }
}
