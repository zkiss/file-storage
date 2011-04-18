package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import hu.bme.vihijv37.bus1fj.web.client.Session;

public class WelcomeTopPanel extends Grid {

    public WelcomeTopPanel() {
	super(1, 4);
	String currentUserName = Session.getInstance().getCurrentUser() == null ? "unknown" : Session.getInstance().getCurrentUser().getName();

	Label welcomeLabel = new Label("Welcome, " + currentUserName + "!", false);
	welcomeLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomeLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);

	Label welcomePageLabel = new Label("Welcome page", false);
	welcomePageLabel.setStyleName("link");
	welcomePageLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomePageLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	welcomePageLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new WelcomePanel());
	    }
	});

	Label settingsLabel = new Label("User settings", false);
	settingsLabel.setStyleName("link");
	settingsLabel.getElement().getStyle().setDisplay(Display.INLINE);
	settingsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	settingsLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new UserSettingsPanel());
	    }
	});

	Label logoutLabel = new Label("Logout", false);
	logoutLabel.setStyleName("link");
	logoutLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	logoutLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		Session.getInstance().setCurrentUser(null);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new LoginScreen());
	    }
	});

	this.setWidget(0, 0, welcomeLabel);
	this.setWidget(0, 1, welcomePageLabel);
	this.setWidget(0, 2, settingsLabel);
	this.setWidget(0, 3, logoutLabel);

	this.setGridStyle();
    }

    private void setGridStyle() {
	this.setCellPadding(5);
	this.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_DEFAULT);
	this.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
	this.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

	this.getCellFormatter().setWidth(0, 0, "600px");
	this.getCellFormatter().setWidth(0, 1, "60px");
	this.getCellFormatter().setWidth(0, 2, "60px");
	this.getCellFormatter().setWidth(0, 3, "30px");

	this.getCellFormatter().setStyleName(0, 1, "topGridCell");
	this.getCellFormatter().setStyleName(0, 2, "topGridCell");
    }

}
