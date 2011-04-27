package hu.bme.vihijv37.bus1fj.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public interface FsServiceAsync {

    /**
     * @param userId
     * @param callback
     * @see FsService#getUserUploads(long)
     */
    void getUserUploads(long userId, AsyncCallback<List<UploadDto>> callback);

    /**
     * @param userName
     * @param password
     * @param callback
     * @see FsService#login(String, String)
     */
    void login(String userName, String password, AsyncCallback<UserDto> callback);

    /**
     * @param name
     * @param email
     * @param password
     * @param callback
     * @see FsService#register(String, String, String)
     */
    void register(String name, String email, String password, AsyncCallback<UserDto> callback);

    /**
     * @param fileId
     * @param callback
     * @see FsService#removeFile(long)
     */
    void removeFile(long fileId, AsyncCallback<Void> callback);

    /**
     * @param user
     * @param callback
     * @see FsService#updateUser(UserDto)
     */
    void updateUser(UserDto user, AsyncCallback<UserDto> callback);

}
