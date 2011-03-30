package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RegisterScreen extends VerticalPanel {

    private FlexTable contentTable;
    private TextBox emailTextBox;
    private TextBox userNameTextBox;
    private PasswordTextBox passwordTextBox;
    private PasswordTextBox reTypePasswordTextBox;
    private Button registerButton;

    public RegisterScreen() {
	this.contentTable = new FlexTable();
	this.emailTextBox = new TextBox();
	this.userNameTextBox = new TextBox();
	this.passwordTextBox = new PasswordTextBox();
	this.reTypePasswordTextBox = new PasswordTextBox();
	this.registerButton = new Button("Register", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub

	    }
	});

	this.initPanel();
	this.setupContentTable();
	this.add(this.contentTable);
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

	this.contentTable.setText(0, 0, "E-mail");
	this.contentTable.setWidget(0, 1, this.emailTextBox);
	this.emailTextBox.setWidth("150px");

	this.contentTable.setText(1, 0, "Name");
	this.contentTable.setWidget(1, 1, this.userNameTextBox);
	this.userNameTextBox.setWidth("150px");

	this.contentTable.setText(2, 0, "Password");
	this.contentTable.setWidget(2, 1, this.passwordTextBox);
	this.passwordTextBox.setWidth("150px");

	this.contentTable.setText(3, 0, "Confirm password");
	this.contentTable.setWidget(3, 1, this.reTypePasswordTextBox);
	this.reTypePasswordTextBox.setWidth("150px");

	this.contentTable.getFlexCellFormatter().setColSpan(4, 0, 2);
	this.contentTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	this.contentTable.setWidget(4, 0, this.registerButton);
    }
}
