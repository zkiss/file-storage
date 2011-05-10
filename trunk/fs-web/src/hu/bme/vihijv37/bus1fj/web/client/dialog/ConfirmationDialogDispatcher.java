package hu.bme.vihijv37.bus1fj.web.client.dialog;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationDialogDispatcher {

    private List<ConfirmationCallback> handlerList = new ArrayList<ConfirmationCallback>();

    public void addHandler(ConfirmationCallback handler) {
	this.handlerList.add(handler);
    }

    public void fireEvent(Boolean event) {
	/*
	 * Elkerüljük a ConcurrentModificationException-t azzal, hogy lemásoljuk
	 * a listener-ek listáját.
	 */
	ArrayList<ConfirmationCallback> listenerCpy = new ArrayList<ConfirmationCallback>(this.handlerList);
	for (ConfirmationCallback handler : listenerCpy) {
	    handler.onConfirmation(event);
	}
    }

    public void removeHandler(ConfirmationCallback handler) {
	this.handlerList.remove(handler);
    }
}
