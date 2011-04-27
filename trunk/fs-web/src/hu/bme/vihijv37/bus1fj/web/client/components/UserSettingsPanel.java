package hu.bme.vihijv37.bus1fj.web.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class UserSettingsPanel extends VerticalPanel {

    private static final String WIDTH = "196px";
    private TextBox emailTb;
    private TextBox userNameTb;
    private PasswordTextBox passTb;
    private PasswordTextBox passReTb;
    private UserDto currentUser = ClientSession.getInstance().getCurrentUser();

    public UserSettingsPanel() {
	this.add(new MenuPanel());
	Grid grid = new Grid(5, 2);

	int row = 0;
	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, 0, "E-mail");
	this.emailTb = new TextBox();
	this.emailTb.setEnabled(false);
	this.emailTb.setText(this.currentUser.getEmail());
	this.emailTb.setWidth(UserSettingsPanel.WIDTH);
	this.emailTb.addKeyUpHandler(new KeyUpHandler() {

	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsPanel.this.doSave();
		}
	    }
	});
	grid.setWidget(row, 1, this.emailTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, 0, "User name");
	this.userNameTb = new TextBox();
	this.userNameTb.setText(this.currentUser.getName());
	this.userNameTb.setWidth(UserSettingsPanel.WIDTH);
	this.userNameTb.addKeyUpHandler(new KeyUpHandler() {

	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsPanel.this.doSave();
		}
	    }
	});
	grid.setWidget(row, 1, this.userNameTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, 0, "New password");
	this.passTb = new PasswordTextBox();
	this.passTb.setWidth(UserSettingsPanel.WIDTH);
	grid.setWidget(row, 1, this.passTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, 0, "New password again");
	this.passReTb = new PasswordTextBox();
	this.passReTb.setWidth(UserSettingsPanel.WIDTH);
	grid.setWidget(row, 1, this.passReTb);
	row++;

	Button saveBtn = new Button("Save", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		UserSettingsPanel.this.doSave();
	    }
	});

	grid.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
	grid.setWidget(row, 1, saveBtn);

	CaptionPanel captionPanel = new CaptionPanel("User settings");
	captionPanel.add(grid);
	this.add(captionPanel);

    }

    private boolean checkFields() {
	boolean ret = true;
	if ("".equals(this.userNameTb.getText().trim())) {
	    MessageDialog.show("Error", "UserName can't be left empty", null);
	    ret = false;
	}
	if (!this.passTb.getText().equals(this.passReTb.getText())) {
	    MessageDialog.show("Error", "Password do not match!", null);
	    ret = false;
	}
	return ret;
    }

    private void doSave() {
	if (UserSettingsPanel.this.checkFields()) {
	    this.setFields();
	    ClientUtil.getService().updateUser(this.currentUser, new AsyncCallback<UserDto>() {

		@Override
		public void onFailure(Throwable caught) {
		    MessageDialog.show("Error", caught.getMessage(), null);
		}

		@Override
		public void onSuccess(UserDto result) {
		    ClientSession.getInstance().setCurrentUser(result);
		    MessageDialog.show("Info", "Save settings successfully!", null);
		}
	    });
	}
    }

    private void setFields() {
	this.currentUser.setName(this.userNameTb.getText());
	String pass = this.passTb.getText();
	this.currentUser.setPassword(!"".equals(pass) ? pass : null);
    }

    private void setRowSizeAndAlignment(int row, Grid grid) {
	grid.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
	grid.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

	grid.getCellFormatter().setWidth(row, 0, "120px");
	grid.getCellFormatter().setWidth(row, 1, "200px");
    }

}
