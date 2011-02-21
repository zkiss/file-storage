package hu.bme.vihijv37.bus1fj.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public interface FsServiceAsync {

    void login(String userName, String passwordHash, AsyncCallback<UserDto> callback);

}
