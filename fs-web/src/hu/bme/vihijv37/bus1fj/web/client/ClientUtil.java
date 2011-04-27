package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.GWT;

/**
 * Globálisan elérhető utility függvények
 * 
 * @author Zoltan Kiss
 */
public final class ClientUtil {

    private static final FsServiceAsync service = GWT.create(FsService.class);

    public static String getFileName(String filePath) {
	int lastIndexOf = filePath.lastIndexOf("\\");
	if (lastIndexOf >= 0) {
	    return filePath.substring(lastIndexOf + 1);
	}
	return filePath;
    }

    public static FsServiceAsync getService() {
	return ClientUtil.service;
    }

    private ClientUtil() {
	throw new UnsupportedOperationException("This class should not be instantiated");
    }

}
