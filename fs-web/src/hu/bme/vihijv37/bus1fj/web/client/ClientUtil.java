package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import hu.bme.vihijv37.bus1fj.web.client.screen.LoginScreen;
import hu.bme.vihijv37.bus1fj.web.client.screen.WelcomeScreen;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

/**
 * Globálisan elérhető utility függvények
 * 
 * @author Zoltan Kiss
 */
public final class ClientUtil {

    private static final FsServiceAsync service = GWT.create(FsService.class);

    public static FsServiceAsync getService() {
	return ClientUtil.service;
    }

    /**
     * A {@link GuiNames#DOM_MAIN} névvel azonosított elem feltöltése a megadott
     * {@link Widget}-tel.
     * 
     * @param widget
     */
    public static void goTo(Widget widget) {
	RootPanel.get(GuiNames.DOM_MAIN).clear();
	RootPanel.get(GuiNames.DOM_MAIN).add(widget);
    }

    /**
     * Sikeres bejelentkezés
     * 
     * @param user
     */
    public static void login(UserDto user) {
	ClientSession.getInstance().setCurrentUser(user);
	ClientUtil.goTo(new WelcomeScreen());
    }

    /**
     * Kijelentkezés
     */
    public static void logout() {
	ClientSession.getInstance().setCurrentUser(null);
	ClientUtil.goTo(new LoginScreen());
    }

    private ClientUtil() {
	throw new UnsupportedOperationException("This class should not be instantiated");
    }

}
