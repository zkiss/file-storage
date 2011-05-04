package hu.bme.vihijv37.bus1fj.web.server.converter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.ConversionDefinition;
import hu.bme.vihijv37.bus1fj.web.server.converter.conversion.Skip;
import hu.bme.vihijv37.bus1fj.web.server.entity.Upload;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ConverterConfig {

    private static final Log log = LogFactory.getLog(ConverterConfig.class);

    /**
     * Beégetett értékek
     */
    private static final LinkedList<ConversionDefinition> CONVERSION_DEFINITIONS;

    /**
     * Beégetett konfig példány
     */
    private static final ConverterConfig BURNT_IN_CONFIG;

    static {
	CONVERSION_DEFINITIONS = new LinkedList<ConversionDefinition>();

	ConverterConfig.CONVERSION_DEFINITIONS.add(new ConversionDefinition(User.class, UserDto.class, new Skip("uploads")));
	ConverterConfig.CONVERSION_DEFINITIONS.add(new ConversionDefinition(Upload.class, UploadDto.class, new Skip("user"), new Skip("urlPath")));

	BURNT_IN_CONFIG = new ConverterConfig(ConverterConfig.CONVERSION_DEFINITIONS);
    }

    public static ConverterConfig getBurntInConfig() {
	return ConverterConfig.BURNT_IN_CONFIG;
    }

    /**
     * Kulcs: osztálynév<br>
     * Érték: {@link Conversion}
     */
    private final TreeMap<String, Conversion> conversions;

    private ConverterConfig(Collection<ConversionDefinition> conversionDefinitions) {
	this.conversions = new TreeMap<String, Conversion>();

	if (ConverterConfig.log.isInfoEnabled()) {
	    StringBuilder sb = new StringBuilder("Converter config:");
	    for (ConversionDefinition cp : conversionDefinitions) {
		sb.append("\n").append(cp);
	    }
	    ConverterConfig.log.info(sb);
	}

	this.buildConversions(conversionDefinitions);
    }

    private void buildConversions(Collection<ConversionDefinition> conversionDefinitions) {
	for (ConversionDefinition cd : conversionDefinitions) {
	    Conversion conversion = new Conversion(cd);
	    this.conversions.put(cd.class1.getName(), conversion);
	    this.conversions.put(cd.class2.getName(), conversion);
	}
    }

    /**
     * A JavaBean-hoz tartozó {@link Conversion}
     * 
     * @param clazz
     * @return
     */
    public Conversion getConversion(Class<?> clazz) {
	return this.conversions.get(clazz.getName());
    }

}
