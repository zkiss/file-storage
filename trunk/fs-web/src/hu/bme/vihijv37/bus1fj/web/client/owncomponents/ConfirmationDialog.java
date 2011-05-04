package hu.bme.vihijv37.bus1fj.web.client.owncomponents;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO javadoc
 * 
 * @author Zoltan Kiss
 */
public class ConfirmationDialog extends DialogBox {

    public static class ConfirmOption {
	private final Option option;
	private final String buttonText;

	public ConfirmOption(Option option) {
	    this.option = option;
	    switch (option) {
	    case YES:
		this.buttonText = "Yes";
		break;
	    case NO:
		this.buttonText = "No";
		break;
	    default:
		this.buttonText = "Cancel";
		break;
	    }
	}

	public ConfirmOption(Option option, String buttonText) {
	    this.option = option;
	    this.buttonText = buttonText;
	}

    }

    public enum Option {
	YES, CANCEL, NO, ;
    }

    /**
     * TODO javadoc
     * 
     * @param options
     * @param captionText
     * @param innerWidget
     * @param handler
     */
    public static void show(List<ConfirmOption> options, String captionText, Widget innerWidget, ConfirmationCallback handler) {
	ConfirmationDialog dialog = new ConfirmationDialog(options, captionText, innerWidget);
	dialog.addConfirmationHandler(handler);
	dialog.center();
	dialog.show();
    }

    private ConfirmationDialogDispatcher dispatcher = new ConfirmationDialogDispatcher();

    private ConfirmationDialog(List<ConfirmOption> options, String captionText, Widget innerWidget) {
	super(true, true);
	this.setText(captionText);
	VerticalPanel innerPanel = new VerticalPanel();
	innerPanel.getElement().getStyle().setPadding(5, Unit.PX);
	innerPanel.setSpacing(5);
	innerPanel.add(innerWidget);
	innerPanel.add(this.initButtonPanel(options));
	this.add(innerPanel);
    }

    private void addConfirmationHandler(ConfirmationCallback handler) {
	this.dispatcher.addHandler(handler);
    }

    private HorizontalPanel initButtonPanel(List<ConfirmOption> options) {
	HorizontalPanel ret = new HorizontalPanel();
	ret.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	for (ConfirmOption option : options) {
	    switch (option.option) {
	    case YES:
		Button yesBtn = new Button(option.buttonText, new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			ConfirmationDialog.this.dispatcher.fireEvent(true);
			ConfirmationDialog.this.hide(true);
		    }
		});
		ret.add(yesBtn);
		break;
	    case NO:
		Button noBtn = new Button(option.buttonText, new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			ConfirmationDialog.this.dispatcher.fireEvent(false);
			ConfirmationDialog.this.hide(true);
		    }
		});
		ret.add(noBtn);
		break;
	    default:
		Button cancelButton = new Button(option.buttonText, new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			ConfirmationDialog.this.dispatcher.fireEvent(null);
			ConfirmationDialog.this.hide(true);
		    }
		});
		ret.add(cancelButton);
		break;
	    }
	}
	return ret;
    }
}
