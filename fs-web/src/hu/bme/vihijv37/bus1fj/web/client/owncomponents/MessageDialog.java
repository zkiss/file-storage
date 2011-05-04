package hu.bme.vihijv37.bus1fj.web.client.owncomponents;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Felhasználói üzenetek megjelenítésére szolgáló segédosztály
 * 
 * @author Zoltan Kiss
 */
public class MessageDialog extends DialogBox {

    /**
     * Üzenet megjelenítése
     * 
     * @param captionText
     *            címsor
     * @param message
     *            üzenet
     * @param closeHandler
     *            a dialógusablak bezárása esetén visszahívott listener,
     *            <code>null</code> is lehet.
     */
    public static void show(String captionText, String message, CloseHandler<PopupPanel> closeHandler) {
	MessageDialog messageDialog = new MessageDialog(captionText, message);
	messageDialog.center();
	messageDialog.show();
	if (closeHandler != null) {
	    messageDialog.addCloseHandler(closeHandler);
	}
    }

    private final Button closeButton;

    private MessageDialog(String captionText, String message) {
	super(true, true);
	this.setText(captionText);

	final VerticalPanel innerPanel = new VerticalPanel();
	innerPanel.setSpacing(5);
	innerPanel.getElement().getStyle().setPadding(10, Unit.PX);
	innerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

	innerPanel.add(new HTML(message));

	this.closeButton = new Button("OK", new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		MessageDialog.this.hide(true);
	    }
	});
	this.closeButton.setWidth("60px");
	innerPanel.add(this.closeButton);

	this.add(innerPanel);
    }

}