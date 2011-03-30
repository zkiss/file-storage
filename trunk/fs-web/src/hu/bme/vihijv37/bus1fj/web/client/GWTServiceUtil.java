package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.GWT;

public class GWTServiceUtil {
    private static FsServiceAsync service = GWT.create(FsService.class);

    public static FsServiceAsync getService() {
	return GWTServiceUtil.service;
    }

    private GWTServiceUtil() {
	//
    }
}
