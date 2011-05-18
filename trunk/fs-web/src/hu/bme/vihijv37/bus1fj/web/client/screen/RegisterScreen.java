package hu.bme.vihijv37.bus1fj.web.client.screen;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.AbstractAsyncCallback;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.dialog.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class RegisterScreen extends VerticalPanel {

    private static final String COL1_WIDTH = "150px";
    private static final String COL2_WIDTH = "150px";
    private static final String WIDTH = "820px";
    private static final String CONTENT_WIDTH = "300px";

    private final TextBox emailTextBox;
    private final TextBox userNameTextBox;
    private final PasswordTextBox passwordTextBox;
    private final PasswordTextBox reTypePasswordTextBox;

    public RegisterScreen() {
	final FlexTable contentTable = new FlexTable();

	this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	this.setWidth(RegisterScreen.WIDTH);

	int row = 0;
	contentTable.setWidth(RegisterScreen.CONTENT_WIDTH);
	contentTable.getCellFormatter().setWidth(0, 0, RegisterScreen.COL1_WIDTH);
	contentTable.getCellFormatter().setWidth(0, 1, RegisterScreen.COL2_WIDTH);

	contentTable.getCellFormatter().setWidth(1, 0, RegisterScreen.COL1_WIDTH);
	contentTable.getCellFormatter().setWidth(1, 1, RegisterScreen.COL2_WIDTH);

	contentTable.setText(row, 0, "E-mail");
	this.emailTextBox = new TextBox();
	contentTable.setWidget(row, 1, this.emailTextBox);
	this.emailTextBox.setWidth(RegisterScreen.COL2_WIDTH);
	row++;

	contentTable.setText(row, 0, "Name");
	this.userNameTextBox = new TextBox();
	contentTable.setWidget(row, 1, this.userNameTextBox);
	this.userNameTextBox.setWidth(RegisterScreen.COL2_WIDTH);
	row++;

	contentTable.setText(row, 0, "Password");
	this.passwordTextBox = new PasswordTextBox();
	contentTable.setWidget(row, 1, this.passwordTextBox);
	this.passwordTextBox.setWidth(RegisterScreen.COL2_WIDTH);
	row++;

	contentTable.setText(row, 0, "Confirm password");
	this.reTypePasswordTextBox = new PasswordTextBox();
	contentTable.setWidget(row, 1, this.reTypePasswordTextBox);
	this.reTypePasswordTextBox.setWidth(RegisterScreen.COL2_WIDTH);
	row++;

	contentTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	contentTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	contentTable.setWidget(row, 0, new Button("Register", new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		RegisterScreen.this.doRegister();
	    }
	}));

	this.add(contentTable);
    }

    private boolean checkFields() {
	boolean ret = true;
	if ("".equals(this.userNameTextBox.getText())) {
	    ret = false;
	}
	if ("".equals(this.passwordTextBox.getText())) {
	    ret = false;
	}
	if ("".equals(this.emailTextBox.getText())) {
	    ret = false;
	}
	if (!ret) {
	    MessageDialog.show("Message", "All fields must be filled", null);
	} else if (!this.passwordTextBox.getText().equals(this.reTypePasswordTextBox.getText())) {
	    ret = false;
	    MessageDialog.show("Message", "Passwords do not match", null);
	}
	return ret;
    }

    private void doRegister() {
	if (RegisterScreen.this.checkFields()) {
	    ClientUtil.getService().register(RegisterScreen.this.userNameTextBox.getText(), //
		    RegisterScreen.this.emailTextBox.getText(), //
		    RegisterScreen.this.passwordTextBox.getText(), //
		    new AbstractAsyncCallback<UserDto>() {
			@Override
			public void onSuccess(UserDto result) {
			    ClientUtil.login(result);
			}
		    });
	}

    }

}
