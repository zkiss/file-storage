package hu.bme.vihijv37.bus1fj.web.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import hu.bme.vihijv37.bus1fj.web.shared.dto.FileDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public interface FsServiceAsync {

    void getUserFiles(UserDto user, AsyncCallback<Set<FileDto>> callback);

    /**
     * @param userName
     * @param password
     * @param callback
     * @see FsService#login(String, String)
     */
    void login(String userName, String password, AsyncCallback<UserDto> callback);

    void register(String name, String email, String password, AsyncCallback<UserDto> callback);

    void removeFile(FileDto file, AsyncCallback<Void> callback);

    void updateUser(UserDto user, AsyncCallback<UserDto> callback);

}
