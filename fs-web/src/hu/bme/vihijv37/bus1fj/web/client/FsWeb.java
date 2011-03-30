package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import hu.bme.vihijv37.bus1fj.web.client.components.LoginScreen;

public class FsWeb implements EntryPoint {

    private final FsServiceAsync greetingService = GWT.create(FsService.class);

    @Override
    public void onModuleLoad() {
	LoginScreen loginScreen = new LoginScreen();
	RootPanel.get("main").clear();
	RootPanel.get("main").add(loginScreen);
    }

}
