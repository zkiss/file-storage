package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.GuiNames;

public class MenuPanel extends Grid {

    public MenuPanel() {
	super(1, 4);
	String currentUserName = ClientSession.getInstance().getCurrentUser() == null ? "unknown" : ClientSession.getInstance().getCurrentUser().getName();

	Label welcomeLabel = new Label("Welcome, " + currentUserName + "!", false);
	welcomeLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomeLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);

	Label welcomePageLabel = new Label("Welcome page", false);
	welcomePageLabel.setStyleName(GuiNames.STYLE_LINK);
	welcomePageLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomePageLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	welcomePageLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RootPanel.get(GuiNames.DOM_MAIN).clear();
		RootPanel.get(GuiNames.DOM_MAIN).add(new WelcomePanel());
	    }
	});

	Label settingsLabel = new Label("User settings", false);
	settingsLabel.setStyleName(GuiNames.STYLE_LINK);
	settingsLabel.getElement().getStyle().setDisplay(Display.INLINE);
	settingsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	settingsLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RootPanel.get(GuiNames.DOM_MAIN).clear();
		RootPanel.get(GuiNames.DOM_MAIN).add(new UserSettingsPanel());
	    }
	});

	Label logoutLabel = new Label("Logout", false);
	logoutLabel.setStyleName(GuiNames.STYLE_LINK);
	logoutLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	logoutLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		ClientSession.getInstance().setCurrentUser(null);
		RootPanel.get(GuiNames.DOM_MAIN).clear();
		RootPanel.get(GuiNames.DOM_MAIN).add(new LoginScreen());
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

	this.getCellFormatter().setStyleName(0, 1, GuiNames.STYLE_TOP_GRID_CELL);
	this.getCellFormatter().setStyleName(0, 2, GuiNames.STYLE_TOP_GRID_CELL);
    }

}
