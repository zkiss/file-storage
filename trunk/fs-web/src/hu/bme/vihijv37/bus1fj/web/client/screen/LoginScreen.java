package hu.bme.vihijv37.bus1fj.web.client.screen;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.AbstractAsyncCallback;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.GuiNames;
import hu.bme.vihijv37.bus1fj.web.client.dialog.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

/**
 * Bejelentkezo oldal
 * 
 * @author Zoltan Kiss
 */
public class LoginScreen extends VerticalPanel {

    private static final String COL1_WIDTH = "150px";
    private static final String COL2_WIDTH = "150px";
    private static final String CONTENT_WIDTH = "300px";
    private static final String WIDTH = "820px";

    private final TextBox userNameTextBox;
    private final PasswordTextBox passwordTextBox;

    public LoginScreen() {
	this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	this.setWidth(LoginScreen.WIDTH);

	final FlexTable contentTable = new FlexTable();
	contentTable.setWidth(LoginScreen.CONTENT_WIDTH);
	contentTable.getCellFormatter().setWidth(0, 0, LoginScreen.COL1_WIDTH);
	contentTable.getCellFormatter().setWidth(0, 1, LoginScreen.COL2_WIDTH);
	contentTable.getCellFormatter().setWidth(1, 0, LoginScreen.COL1_WIDTH);
	contentTable.getCellFormatter().setWidth(1, 1, LoginScreen.COL2_WIDTH);

	// email
	int row = 0;
	contentTable.setText(row, 0, "E-mail");
	this.userNameTextBox = new TextBox();
	this.userNameTextBox.addKeyDownHandler(new KeyDownHandler() {
	    @Override
	    public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    LoginScreen.this.doLogin();
		}
	    }
	});
	contentTable.setWidget(row, 1, this.userNameTextBox);
	this.userNameTextBox.setWidth(LoginScreen.COL1_WIDTH);

	// password
	row++;
	contentTable.setText(row, 0, "Password");
	this.passwordTextBox = new PasswordTextBox();
	this.passwordTextBox.addKeyDownHandler(new KeyDownHandler() {
	    @Override
	    public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    LoginScreen.this.doLogin();
		}
	    }
	});
	contentTable.setWidget(row, 1, this.passwordTextBox);
	this.passwordTextBox.setWidth(LoginScreen.COL2_WIDTH);

	// login button
	row++;
	contentTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	contentTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	contentTable.setWidget(row, 0, new Button("Login", new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		LoginScreen.this.doLogin();
	    }
	}));
	this.add(contentTable);

	// regisztrációs link
	final HorizontalPanel registerNowPanel = new HorizontalPanel();
	registerNowPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	registerNowPanel.getElement().getStyle().setPaddingTop(15, Unit.PX);
	registerNowPanel.setSpacing(2);

	registerNowPanel.add(new Label("Not registered?"));

	final Label createAccountLabel = new Label("Create a free account now!");
	createAccountLabel.setStyleName(GuiNames.STYLE_LINK);
	createAccountLabel.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		ClientUtil.goTo(new RegisterScreen());
	    }
	});
	registerNowPanel.add(createAccountLabel);

	this.add(registerNowPanel);

	this.userNameTextBox.setFocus(true);
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

    private void doLogin() {
	if (this.checkFields()) {
	    ClientUtil.getService().login(this.userNameTextBox.getText(), //
		    this.passwordTextBox.getText(), new AbstractAsyncCallback<UserDto>() {
			@Override
			public void onSuccess(UserDto result) {
			    ClientUtil.login(result);
			}
		    });
	} else {
	    MessageDialog.show("Error", "Please enter your username and password", null);
	}
    }

}
