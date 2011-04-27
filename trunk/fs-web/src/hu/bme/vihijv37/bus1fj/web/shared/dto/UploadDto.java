package hu.bme.vihijv37.bus1fj.web.shared.dto;

public class UploadDto extends EntityBaseDto {

    private static final long serialVersionUID = -6875800091975550956L;

    private String path;

    private UserDto user;

    public String getPath() {
	return this.path;
    }

    public UserDto getUser() {
	return this.user;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public void setUser(UserDto user) {
	this.user = user;
    }

    @Override
    public String toString() {
	return super.toString("Upload") + "[" + this.path + "]";
    }

}
