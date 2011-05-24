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
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationDialog.ConfirmOption;
import hu.bme.vihijv37.bus1fj.web.client.dialog.ConfirmationDialog.Option;
import hu.bme.vihijv37.bus1fj.web.client.dialog.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.UploadFormConstants;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadDto;

public class WelcomeScreen extends VerticalPanel {

    private static final String EMPTY_RESPONSE = "<pre></pre>";
    private static final String CONTENT_WIDTH = "800px";
    private static final String COL1_WIDTH = "696px";

    private FlexTable uploadsTable;

    public WelcomeScreen() {
	this.add(new MenuPanel());

	final CaptionPanel captionPanel = new CaptionPanel("Manage files");

	this.uploadsTable = new FlexTable();
	this.uploadsTable.setWidth(WelcomeScreen.CONTENT_WIDTH);
	this.uploadsTable.setCellSpacing(0);
	captionPanel.add(this.uploadsTable);

	this.add(captionPanel);

	this.loadUserFiles();
    }

    private void loadUserFiles() {
	ClientUtil.getService().getUserUploads(ClientSession.getInstance().getCurrentUser().getId(), new AbstractAsyncCallback<List<UploadDto>>() {
	    @Override
	    public void onSuccess(List<UploadDto> result) {
		WelcomeScreen.this.showUploads(result != null ? result : new ArrayList<UploadDto>(0));
	    }
	});
    }

    private void removeFileStep1(final UploadDto upload) {
	List<ConfirmOption> options = Arrays.asList(new ConfirmOption(Option.YES), new ConfirmOption(Option.CANCEL));
	ConfirmationDialog.show(options, "Question", //
		new Label("Selected file will be removed. Do you want to continue?"), //
		new ConfirmationCallback() {
		    @Override
		    public void onConfirmation(Boolean result) {
			if ((result != null) && result) {
			    WelcomeScreen.this.removeFileStep2(upload);
			}
		    }
		});
    }

    private void removeFileStep2(UploadDto upload) {
	ClientUtil.getService().removeFile(upload.getId(), new AbstractAsyncCallback<Void>() {
	    @Override
	    public void onSuccess(Void result) {
		WelcomeScreen.this.loadUserFiles();
		MessageDialog.show("Info", "File deleted", null);
	    }
	});
    }

    private void showUploads(List<UploadDto> uploads) {
	this.uploadsTable.removeAllRows();

	// fájlok listázása
	int row = 0;
	for (final UploadDto upload : uploads) {
	    final HTML link = new HTML("<a href=\"" + upload.getUrlPath() + "\">" + upload.getPath() + "</a>");
	    link.setStyleName(GuiNames.STYLE_LINK);
	    this.uploadsTable.setWidget(row, 0, link);
	    this.uploadsTable.setWidget(row, 1, new Button("Delete", new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
		    WelcomeScreen.this.removeFileStep1(upload);
		}
	    }));
	    this.uploadsTable.getCellFormatter().setWidth(row, 0, WelcomeScreen.COL1_WIDTH);
	    this.uploadsTable.getCellFormatter().setStyleName(row, 0, GuiNames.STYLE_TABLE_CELL);
	    this.uploadsTable.getCellFormatter().setStyleName(row, 1, GuiNames.STYLE_TABLE_CELL);
	    this.uploadsTable.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
	    row++;
	}

	// új fájl feltöltése
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
	formInnerPanel.add(new Button("Upload new file", new ClickHandler() {
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
		if (!WelcomeScreen.EMPTY_RESPONSE.equals(event.getResults())) {
		    MessageDialog.show("Error", event.getResults(), new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
			    WelcomeScreen.this.loadUserFiles();
			}
		    });
		} else {
		    WelcomeScreen.this.loadUserFiles();
		}
	    }
	});

	formPanel.setWidget(formInnerPanel);

	this.uploadsTable.setWidget(row, 0, formPanel);
    }

}
