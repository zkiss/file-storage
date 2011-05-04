package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import hu.bme.vihijv37.bus1fj.web.client.owncomponents.MessageDialog;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    /**
     * Az alapértelmezett hibakezelést követően van meghívva
     */
    protected void handleFailure() {
	// alapesetben semmit sem csinál
    }

    @Override
    public final void onFailure(Throwable caught) {
	String message;
	if (caught instanceof ServiceException) {
	    message = caught.getMessage();
	} else {
	    message = "An unknown error occured";
	}
	MessageDialog.show("Error", message, null);
	this.handleFailure();
    }

}
