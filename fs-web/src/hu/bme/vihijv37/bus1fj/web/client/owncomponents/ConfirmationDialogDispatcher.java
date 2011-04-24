package hu.bme.vihijv37.bus1fj.web.client.owncomponents;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationDialogDispatcher {

    private List<ConfirmationCallback> handlerList = new ArrayList<ConfirmationCallback>();

    public void addHandler(ConfirmationCallback handler) {
	this.handlerList.add(handler);
    }

    public void fireEvent(Boolean event) {
	for (ConfirmationCallback handler : this.handlerList) {
	    handler.onConfirmation(event);
	}
    }

    public void removeHandler(ConfirmationCallback handler) {
	this.handlerList.remove(handler);
    }
}
