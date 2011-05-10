package hu.bme.vihijv37.bus1fj.web.client.screen;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.GuiNames;

/**
 * Belépés után megjelenő menüsor
 * 
 * @author Zoltan Kiss
 */
public class MenuPanel extends Grid {

    private static final int ROWS = 1;
    private static final int COLS = 4;
    private static final int CELL_PADDING = 5;

    public MenuPanel() {
	super(MenuPanel.ROWS, MenuPanel.COLS);
	final String userName = ClientSession.getInstance().getCurrentUser() == null ? "unknown" : ClientSession.getInstance().getCurrentUser().getName();

	final Label welcomeLabel = new Label("Welcome, " + userName + "!", false);
	welcomeLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomeLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);

	final Label welcomePageLabel = new Label("Welcome page", false);
	welcomePageLabel.setStyleName(GuiNames.STYLE_LINK);
	welcomePageLabel.getElement().getStyle().setDisplay(Display.INLINE);
	welcomePageLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	welcomePageLabel.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ClientUtil.goTo(new WelcomeScreen());
	    }
	});

	final Label settingsLabel = new Label("User settings", false);
	settingsLabel.setStyleName(GuiNames.STYLE_LINK);
	settingsLabel.getElement().getStyle().setDisplay(Display.INLINE);
	settingsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	settingsLabel.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ClientUtil.goTo(new UserSettingsScreen());
	    }
	});

	final Label logoutLabel = new Label("Logout", false);
	logoutLabel.setStyleName(GuiNames.STYLE_LINK);
	logoutLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	logoutLabel.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ClientUtil.logout();
	    }
	});

	this.setWidget(0, 0, welcomeLabel);
	this.setWidget(0, 1, welcomePageLabel);
	this.setWidget(0, 2, settingsLabel);
	this.setWidget(0, 3, logoutLabel);

	this.setGridStyle();
    }

    private void setGridStyle() {
	this.setCellPadding(MenuPanel.CELL_PADDING);
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
