package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.GWTServiceUtil;
import hu.bme.vihijv37.bus1fj.web.client.Session;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class RegisterScreen extends VerticalPanel {

    private final TextBox emailTextBox;
    private final TextBox userNameTextBox;
    private final PasswordTextBox passwordTextBox;
    private final PasswordTextBox reTypePasswordTextBox;
    private final Button registerButton;

    public RegisterScreen() {
	FlexTable contentTable = new FlexTable();
	this.emailTextBox = new TextBox();
	this.userNameTextBox = new TextBox();
	this.passwordTextBox = new PasswordTextBox();
	this.reTypePasswordTextBox = new PasswordTextBox();
	this.registerButton = new Button("Register", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if (RegisterScreen.this.checkFields()) {
		    GWTServiceUtil.getService().register(RegisterScreen.this.userNameTextBox.getSelectedText(),
			    RegisterScreen.this.emailTextBox.getSelectedText(), RegisterScreen.this.passwordTextBox.getSelectedText(),
			    new AsyncCallback<UserDto>() {
				@Override
				public void onFailure(Throwable caught) {
				    caught.printStackTrace();
				};

				@Override
				public void onSuccess(UserDto result) {
				    Session.getInstance().setCurrentUser(result);
				};
			    });
		} else {
		    MessageDialog.show("Message", "Please fill all field!", null);
		}
	    }
	});
	this.initPanel();
	this.setupContentTable(contentTable);

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
	return ret;
    }

    private void initPanel() {
	this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	this.setWidth("820px");
    }

    private void setupContentTable(FlexTable contentTable) {
	int row = 0;
	contentTable.setWidth("300px");
	contentTable.getCellFormatter().setWidth(0, 0, "150px");
	contentTable.getCellFormatter().setWidth(0, 1, "150px");

	contentTable.getCellFormatter().setWidth(1, 0, "150px");
	contentTable.getCellFormatter().setWidth(1, 1, "150px");

	contentTable.setText(row, 0, "E-mail");
	contentTable.setWidget(row, 1, this.emailTextBox);
	this.emailTextBox.setWidth("150px");
	row++;

	contentTable.setText(row, 0, "Name");
	contentTable.setWidget(row, 1, this.userNameTextBox);
	this.userNameTextBox.setWidth("150px");
	row++;

	contentTable.setText(row, 0, "Password");
	contentTable.setWidget(row, 1, this.passwordTextBox);
	this.passwordTextBox.setWidth("150px");
	row++;

	contentTable.setText(row, 0, "Confirm password");
	contentTable.setWidget(row, 1, this.reTypePasswordTextBox);
	this.reTypePasswordTextBox.setWidth("150px");
	row++;

	contentTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	contentTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	contentTable.setWidget(row, 0, this.registerButton);

    }
}
