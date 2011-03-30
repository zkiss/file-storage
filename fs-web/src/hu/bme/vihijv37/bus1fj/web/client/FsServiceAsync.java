package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public interface FsServiceAsync {

    /**
     * @param userName
     * @param password
     * @param callback
     * @see FsService#login(String, String)
     */
    void login(String userName, String password, AsyncCallback<UserDto> callback);

}
