package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import hu.bme.vihijv37.bus1fj.web.client.components.LoginScreen;

public class FsWeb implements EntryPoint {

    @Override
    public void onModuleLoad() {
	LoginScreen loginScreen = new LoginScreen();
	RootPanel.get(GuiNames.DOM_MAIN).clear();
	RootPanel.get(GuiNames.DOM_MAIN).add(loginScreen);
    }

}
