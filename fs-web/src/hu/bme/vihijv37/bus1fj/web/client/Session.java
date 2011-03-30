package hu.bme.vihijv37.bus1fj.web.client;

import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class Session {

    private UserDto currentUser;

    private static final Session instance = new Session();

    public static Session getInstance() {
	return Session.instance;
    }

    private Session() {
	//
    }

    public UserDto getCurrentUser() {
	return this.currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
	this.currentUser = currentUser;
    }

}
