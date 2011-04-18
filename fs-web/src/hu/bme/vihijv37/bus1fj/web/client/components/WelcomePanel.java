package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WelcomePanel extends VerticalPanel {

    public WelcomePanel() {
	this.add(new WelcomeTopPanel());
	this.add(new Label("Welcome"));
    }

}
