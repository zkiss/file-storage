package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class FsWeb implements EntryPoint {

    private final FsServiceAsync greetingService = GWT.create(FsService.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
    }
}
