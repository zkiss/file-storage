package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.EntryPoint;

import hu.bme.vihijv37.bus1fj.web.client.screen.LoginScreen;

public class FsWeb implements EntryPoint {

    @Override
    public void onModuleLoad() {
	ClientUtil.goTo(new LoginScreen());
    }

}
