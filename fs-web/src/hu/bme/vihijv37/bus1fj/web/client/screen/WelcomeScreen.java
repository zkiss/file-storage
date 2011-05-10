package hu.bme.vihijv37.bus1fj.web.client.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import hu.bme.vihijv37.bus1fj.web.client.AbstractAsyncCallback;
import hu.bme.vihijv37.bus1fj.web.client.ClientSession;
import hu.bme.vihijv37.bus1fj.web.client.ClientUtil;
import hu.bme.vihijv37.bus1fj.web.client.GuiNames;
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationCallback;
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationDialog;
import hu.bme.vihijv37.bus1fj.web.client.dialog.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationDialog.ConfirmOption;
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationDialog.Option;
import hu.bme.vihijv37.bus1fj.web.shared.UploadFormConstants;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadDto;

public class WelcomeScreen extends VerticalPanel {

    private static final String WIDTH = "696px";
    private FlexTable contentTable;
    private List<UploadDto> uploadList;

    public WelcomeScreen() {
	this.add(new MenuPanel());
	this.contentTable = new FlexTable();
	this.contentTable.setWidth("800px");
	this.contentTable.setCellSpacing(0);
	CaptionPanel captionPanel = new CaptionPanel("Manage files");
	captionPanel.add(this.contentTable);
	this.add(captionPanel);
	this.loadUserFiles();
    }

    private void createGui() {
	this.contentTable.removeAllRows();
	int row = 0;
	for (final UploadDto upload : this.uploadList) {
	    String url = "<a href=\"" + upload.getUrlPath() + "\">" + upload.getPath() + "</a>";
	    HTML link = new HTML(url);
	    link.setStyleName(GuiNames.STYLE_LINK);
	    this.contentTable.setWidget(row, 0, link);
	    Button deleteBtn = new Button("Delete", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    Label innerWidget = new Label("Selected file will be removed. Do you want to continue?");

		    ConfirmationCallback handler = new ConfirmationCallback() {

			@Override
			public void onConfirmation(Boolean result) {
			    if ((result != null) && result) {
				WelcomeScreen.this.removeFile(upload);
			    }
			}
		    };
		    List<ConfirmOption> options = Arrays.asList(new ConfirmOption(Option.YES, "Yes"), new ConfirmOption(Option.CANCEL, "Cancel"));
		    ConfirmationDialog.show(options, "Please confirm", innerWidget, handler);
		}
	    });
	    this.contentTable.setWidget(row, 1, deleteBtn);
	    this.contentTable.getCellFormatter().setWidth(row, 0, WelcomeScreen.WIDTH);
	    this.contentTable.getCellFormatter().setStyleName(row, 0, GuiNames.STYLE_TABLE_CELL);
	    this.contentTable.getCellFormatter().setStyleName(row, 1, GuiNames.STYLE_TABLE_CELL);
	    this.contentTable.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
	    row++;
	}
	final FormPanel formPanel = new FormPanel();
	formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	formPanel.setMethod(FormPanel.METHOD_POST);

	String action = GWT.getHostPageBaseURL() + UploadFormConstants.SERVLET_URL;
	formPanel.setAction(action + "?" + UploadFormConstants.USER_ID + "=" + ClientSession.getInstance().getCurrentUser().getId());

	final FileUpload uploader = new FileUpload();
	uploader.setName(UploadFormConstants.UPLOADER_FORM_ELEMENT_ID);

	final HorizontalPanel formInnerPanel = new HorizontalPanel();
	formInnerPanel.getElement().getStyle().setPaddingTop(3, Unit.PX);
	formInnerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	formInnerPanel.add(uploader);
	formInnerPanel.add(new Button("Upload&nbsp;new&nbsp;file", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if ("".equals(uploader.getFilename())) {
		    MessageDialog.show("Error", "Please select a file", null);
		} else {
		    formPanel.submit();
		}
	    }
	}));

	formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
	    @Override
	    public void onSubmitComplete(SubmitCompleteEvent event) {
		MessageDialog.show("Info", "File uploaded successfully", new CloseHandler<PopupPanel>() {

		    @Override
		    public void onClose(CloseEvent<PopupPanel> event) {
			WelcomeScreen.this.loadUserFiles();
		    }
		});
	    }
	});

	formPanel.setWidget(formInnerPanel);
	this.contentTable.setWidget(row, 0, formPanel);

    }

    private void loadUserFiles() {
	ClientUtil.getService().getUserUploads(ClientSession.getInstance().getCurrentUser().getId(), new AbstractAsyncCallback<List<UploadDto>>() {
	    @Override
	    public void onSuccess(List<UploadDto> result) {
		WelcomeScreen.this.uploadList = result;
		if (WelcomeScreen.this.uploadList == null) {
		    WelcomeScreen.this.uploadList = new ArrayList<UploadDto>(0);
		}
		WelcomeScreen.this.createGui();
	    }
	});
    }

    private void removeFile(final UploadDto file) {
	ClientUtil.getService().removeFile(file.getId(), new AbstractAsyncCallback<Void>() {
	    @Override
	    public void onSuccess(Void result) {
		WelcomeScreen.this.loadUserFiles();
		MessageDialog.show("Info", "File deleted", null);
	    }
	});
    }

}
