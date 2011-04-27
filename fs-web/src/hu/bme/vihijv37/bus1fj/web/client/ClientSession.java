package hu.bme.vihijv37.bus1fj.web.client;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class ClientSession {

    private UserDto currentUser;

    private static final ClientSession instance = new ClientSession();

    public static ClientSession getInstance() {
	return ClientSession.instance;
    }

    private ClientSession() {
	//
    }

    public UserDto getCurrentUser() {
	return this.currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
	this.currentUser = currentUser;
    }

}
