package hu.bme.vihijv37.bus1fj.web.client.components;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

import hu.bme.vihijv37.bus1fj.web.client.GWTServiceUtil;
import hu.bme.vihijv37.bus1fj.web.client.Session;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.ConfirmationCallback;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.ConfirmationDialog;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.ConfirmationDialog.ConfirmOption;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.ConfirmationDialog.Option;
import hu.bme.vihijv37.bus1fj.web.client.owncomponents.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.dto.FileDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class WelcomePanel extends VerticalPanel {

    private static final String SERVLET_URL = "FsWeb/fileUploader";
    private static final UserDto CURRENT_USER = Session.getInstance().getCurrentUser();
    private static final String WIDTH = "196px";
    private FlexTable contentTable;
    private Set<FileDto> fileList;

    public WelcomePanel() {
	this.add(new MenuPanel());
	this.contentTable = new FlexTable();
	this.contentTable.setWidth("320px");
	this.contentTable.setCellSpacing(0);
	CaptionPanel captionPanel = new CaptionPanel("Manage files");
	captionPanel.add(this.contentTable);
	this.add(captionPanel);
	this.loadUserFiles();
    }

    private void createGui() {
	this.contentTable.removeAllRows();
	int row = 0;
	for (final FileDto file : this.fileList) {
	    String url = "<a href=\"" + file.getPath() + "\">" + file.getPath() + "</a>";
	    HTML link = new HTML(url);
	    link.setStyleName("link");
	    this.contentTable.setWidget(row, 0, link);
	    Button deleteBtn = new Button("Delete", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    Label innerWidget = new Label("Selected file will be removed. Do you want to continue?");

		    ConfirmationCallback handler = new ConfirmationCallback() {

			@Override
			public void onConfirmation(Boolean result) {
			    if (result) {
				WelcomePanel.this.removeFile(file);
			    }
			}
		    };
		    List<ConfirmOption> options = Arrays.asList(new ConfirmOption(Option.YES, "Yes"), new ConfirmOption(Option.NO, "No"));
		    ConfirmationDialog.show(options, "Please confirm", innerWidget, handler);
		}
	    });
	    this.contentTable.setWidget(row, 1, deleteBtn);
	    this.contentTable.getCellFormatter().setWidth(row, 0, WelcomePanel.WIDTH);
	    this.contentTable.getRowFormatter().setStyleName(row, "tableRow");
	    row++;
	}
	final FormPanel formPanel = new FormPanel();
	formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
	formPanel.setMethod(FormPanel.METHOD_POST);

	String action = GWT.getHostPageBaseURL() + WelcomePanel.SERVLET_URL;
	formPanel.setAction(action + "?userId=" + Session.getInstance().getCurrentUser().getId());

	final FileUpload uploader = new FileUpload();
	uploader.setName("uploadFormElement");

	HorizontalPanel formInnerPanel = new HorizontalPanel();
	formInnerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	formInnerPanel.add(uploader);
	formInnerPanel.add(new Button("Upload new file", new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if ("".equals(uploader.getFilename())) {
		    MessageDialog.show("Error", "Please select a file!", null);
		} else {
		    formPanel.submit();
		}
	    }
	}));

	formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

	    @Override
	    public void onSubmitComplete(SubmitCompleteEvent event) {
		MessageDialog.show("Info", "Fileupload successfully!", new CloseHandler<PopupPanel>() {

		    @Override
		    public void onClose(CloseEvent<PopupPanel> event) {
			WelcomePanel.this.loadUserFiles();
		    }
		});
	    }
	});

	formPanel.setWidget(formInnerPanel);
	this.contentTable.setWidget(row, 0, formPanel);

    }

    private void loadUserFiles() {
	GWTServiceUtil.getService().getUserFiles(WelcomePanel.CURRENT_USER, new AsyncCallback<Set<FileDto>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		MessageDialog.show("Error", caught.getMessage(), null);
	    }

	    @Override
	    public void onSuccess(Set<FileDto> result) {
		WelcomePanel.this.fileList = result;
		if (WelcomePanel.this.fileList == null) {
		    WelcomePanel.this.fileList = new LinkedHashSet<FileDto>();
		}
		WelcomePanel.this.createGui();
	    }
	});
    }

    private void removeFile(final FileDto file) {
	GWTServiceUtil.getService().removeFile(file, new AsyncCallback<Void>() {

	    @Override
	    public void onFailure(Throwable caught) {
		MessageDialog.show("Error", caught.getMessage(), null);
	    }

	    @Override
	    public void onSuccess(Void result) {
		MessageDialog.show("Info", "Remove succesfully!", new CloseHandler<PopupPanel>() {

		    @Override
		    public void onClose(CloseEvent<PopupPanel> event) {
			WelcomePanel.this.loadUserFiles();
		    }
		});
	    }
	});
    }

}
