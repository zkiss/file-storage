package hu.bme.vihijv37.bus1fj.web.server.converter.conversion;

public abstract class ConversionRule {

    public final String property;

    ConversionRule(String property) {
	this.property = property;
    }

}
