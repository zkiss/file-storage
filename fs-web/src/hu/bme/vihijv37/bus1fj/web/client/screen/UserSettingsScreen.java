package hu.bme.vihijv37.bus1fj.web.client.screen;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.AbstractAsyncCallback;
import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.dialog.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class UserSettingsScreen extends VerticalPanel {

    private static final int COLUMN_1_NUMBER = 0;
    private static final int COLUMN_2_NUMBER = 1;
    private static final String COLUMN_1_WIDTH = "120px";
    private static final String COLUMN_2_WIDTH = "200px";
    private static final String TEXTBOX_WIDTH = "196px";
    private static final int COLS = 2;
    private static final int ROWS = 5;

    private TextBox emailTb;
    private TextBox userNameTb;
    private PasswordTextBox passTb;
    private PasswordTextBox passReTb;
    private UserDto currentUser = ClientSession.getInstance().getCurrentUser();

    public UserSettingsScreen() {
	this.add(new MenuPanel());
	final Grid grid = new Grid(UserSettingsScreen.ROWS, UserSettingsScreen.COLS);

	int row = 0;
	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, UserSettingsScreen.COLUMN_1_NUMBER, "E-mail");
	this.emailTb = new TextBox();
	this.emailTb.setEnabled(false);
	this.emailTb.setText(this.currentUser.getEmail());
	this.emailTb.setWidth(UserSettingsScreen.TEXTBOX_WIDTH);
	this.emailTb.addKeyUpHandler(new KeyUpHandler() {
	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsScreen.this.doSave();
		}
	    }
	});
	grid.setWidget(row, UserSettingsScreen.COLUMN_2_NUMBER, this.emailTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, UserSettingsScreen.COLUMN_1_NUMBER, "User name");
	this.userNameTb = new TextBox();
	this.userNameTb.setText(this.currentUser.getName());
	this.userNameTb.setWidth(UserSettingsScreen.TEXTBOX_WIDTH);
	this.userNameTb.addKeyUpHandler(new KeyUpHandler() {
	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsScreen.this.doSave();
		}
	    }
	});
	grid.setWidget(row, UserSettingsScreen.COLUMN_2_NUMBER, this.userNameTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, UserSettingsScreen.COLUMN_1_NUMBER, "New password");
	this.passTb = new PasswordTextBox();
	this.passTb.setWidth(UserSettingsScreen.TEXTBOX_WIDTH);
	this.passTb.addKeyUpHandler(new KeyUpHandler() {
	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsScreen.this.doSave();
		}

	    }
	});
	grid.setWidget(row, UserSettingsScreen.COLUMN_2_NUMBER, this.passTb);
	row++;

	this.setRowSizeAndAlignment(row, grid);
	grid.setText(row, UserSettingsScreen.COLUMN_1_NUMBER, "Confirm password");
	this.passReTb = new PasswordTextBox();
	this.passReTb.addKeyUpHandler(new KeyUpHandler() {
	    @Override
	    public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    UserSettingsScreen.this.doSave();
		}
	    }
	});
	this.passReTb.setWidth(UserSettingsScreen.TEXTBOX_WIDTH);
	grid.setWidget(row, UserSettingsScreen.COLUMN_2_NUMBER, this.passReTb);
	row++;

	grid.getCellFormatter().setHorizontalAlignment(row, UserSettingsScreen.COLUMN_2_NUMBER, HasHorizontalAlignment.ALIGN_RIGHT);
	grid.setWidget(row, UserSettingsScreen.COLUMN_2_NUMBER, new Button("Save", new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		UserSettingsScreen.this.doSave();
	    }
	}));

	CaptionPanel captionPanel = new CaptionPanel("User settings");
	captionPanel.add(grid);
	this.add(captionPanel);

    }

    private boolean checkFields() {
	boolean ret = true;
	if ("".equals(this.userNameTb.getText().trim())) {
	    MessageDialog.show("Error", "User name must be filled", null);
	    ret = false;
	}
	if (!this.passTb.getText().equals(this.passReTb.getText())) {
	    MessageDialog.show("Error", "Passwords do not match", null);
	    ret = false;
	}
	return ret;
    }

    private void doSave() {
	if (this.checkFields()) {
	    this.setFields();
	    ClientUtil.getService().updateUser(this.currentUser, new AbstractAsyncCallback<UserDto>() {
		@Override
		public void onSuccess(UserDto result) {
		    ClientSession.getInstance().setCurrentUser(result);
		    MessageDialog.show("Info", "Settings saved", null);
		    ClientUtil.goTo(new WelcomeScreen());
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
	grid.getCellFormatter().setHorizontalAlignment(row, UserSettingsScreen.COLUMN_1_NUMBER, HasHorizontalAlignment.ALIGN_LEFT);
	grid.getCellFormatter().setHorizontalAlignment(row, UserSettingsScreen.COLUMN_2_NUMBER, HasHorizontalAlignment.ALIGN_LEFT);

	grid.getCellFormatter().setWidth(row, UserSettingsScreen.COLUMN_1_NUMBER, UserSettingsScreen.COLUMN_1_WIDTH);
	grid.getCellFormatter().setWidth(row, UserSettingsScreen.COLUMN_2_NUMBER, UserSettingsScreen.COLUMN_2_WIDTH);
    }

}
