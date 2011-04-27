package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
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
		    ClientUtil.getService().register(RegisterScreen.this.userNameTextBox.getText(), RegisterScreen.this.emailTextBox.getText(),
			    RegisterScreen.this.passwordTextBox.getText(), new AsyncCallback<UserDto>() {
				@Override
				public void onFailure(Throwable caught) {
				    MessageDialog.show("Error", caught.getMessage(), null);
				};

				@Override
				public void onSuccess(UserDto result) {
				    ClientSession.getInstance().setCurrentUser(result);
				    MessageDialog.show("Info", "Save succesfully!", new CloseHandler<PopupPanel>() {

					@Override
					public void onClose(CloseEvent<PopupPanel> event) {
					    RootPanel.get("main").clear();
					    RootPanel.get("main").add(new LoginScreen());
					}
				    });
				};
			    });
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
	if (!ret) {
	    MessageDialog.show("Message", "Please fill all field!", null);
	}

	if (!this.passwordTextBox.getText().equals(this.reTypePasswordTextBox.getText())) {
	    ret = false;
	    MessageDialog.show("Message", "Passwords do not match!", null);
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
