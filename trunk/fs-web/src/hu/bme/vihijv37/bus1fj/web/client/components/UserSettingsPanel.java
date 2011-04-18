package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserSettingsPanel extends VerticalPanel {

    public UserSettingsPanel() {
	this.add(new WelcomeTopPanel());
	this.add(new Label("UserSettings"));
    }

}
