package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.AbstractAsyncCallback;
import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.GuiNames;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

/**
 * Bejelentkezo oldal
 * 
 * @author Zoltan Kiss
 */
public class LoginScreen extends VerticalPanel {

    private final Button loginBtn;
    private final FlexTable contentTable;
    private final TextBox userNameTextBox;
    private final PasswordTextBox passwordTextBox;
    private final HorizontalPanel registerNowPanel;
    private final Label linkToRegisterLabel;

    public LoginScreen() {
	this.registerNowPanel = new HorizontalPanel();
	this.contentTable = new FlexTable();
	this.userNameTextBox = new TextBox();
	this.passwordTextBox = new PasswordTextBox();
	this.passwordTextBox.addKeyDownHandler(new KeyDownHandler() {

	    @Override
	    public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    LoginScreen.this.doLogin();
		}
	    }
	});

	this.userNameTextBox.addKeyDownHandler(new KeyDownHandler() {

	    @Override
	    public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    LoginScreen.this.doLogin();
		}
	    }
	});

	this.loginBtn = new Button("Login", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		LoginScreen.this.doLogin();
	    }
	});
	this.linkToRegisterLabel = new Label("Create a free account now!");

	this.initPanel();
	this.createGui();
    }

    private boolean checkFields() {
	boolean ret = true;
	if ("".equals(this.userNameTextBox.getText())) {
	    ret = false;
	}
	if ("".equals(this.passwordTextBox.getText())) {
	    ret = false;
	}
	return ret;
    }

    private void createGui() {
	this.setupContentTable();
	this.createRegisterNowPanel();
	this.add(this.contentTable);
	this.add(this.registerNowPanel);
	this.userNameTextBox.setFocus(true);
    }

    private void createRegisterNowPanel() {
	this.registerNowPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	this.registerNowPanel.getElement().getStyle().setPaddingTop(15, Unit.PX);
	this.registerNowPanel.setSpacing(2);

	this.linkToRegisterLabel.setStyleName(GuiNames.STYLE_LINK);
	this.linkToRegisterLabel.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		RootPanel.get(GuiNames.DOM_MAIN).clear();
		RootPanel.get(GuiNames.DOM_MAIN).add(new RegisterScreen());
	    }
	});

	this.registerNowPanel.add(new Label("Not registered?"));
	this.registerNowPanel.add(this.linkToRegisterLabel);
    }

    private void doLogin() {
	if (LoginScreen.this.checkFields()) {
	    ClientUtil.getService().login(LoginScreen.this.userNameTextBox.getText(), LoginScreen.this.passwordTextBox.getText(),
		    new AbstractAsyncCallback<UserDto>() {
			@Override
			public void onSuccess(UserDto result) {
			    ClientSession.getInstance().setCurrentUser(result);
			    RootPanel main = RootPanel.get(GuiNames.DOM_MAIN);
			    main.clear();
			    main.add(new WelcomePanel());
			}
		    });
	} else {
	    MessageDialog.show("Error", "Please enter your username and password!", null);
	}

    }

    private void initPanel() {
	this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	this.setWidth("820px");
    }

    private void setupContentTable() {
	this.contentTable.setWidth("300px");
	this.contentTable.getCellFormatter().setWidth(0, 0, "150px");
	this.contentTable.getCellFormatter().setWidth(0, 1, "150px");

	this.contentTable.getCellFormatter().setWidth(1, 0, "150px");
	this.contentTable.getCellFormatter().setWidth(1, 1, "150px");

	this.contentTable.setText(0, 0, "User");
	this.contentTable.setWidget(0, 1, this.userNameTextBox);
	this.userNameTextBox.setWidth("150px");

	this.contentTable.setText(1, 0, "Password");
	this.contentTable.setWidget(1, 1, this.passwordTextBox);
	this.passwordTextBox.setWidth("150px");

	this.contentTable.getFlexCellFormatter().setColSpan(2, 0, 2);
	this.contentTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	this.contentTable.setWidget(2, 0, this.loginBtn);
    }

}
