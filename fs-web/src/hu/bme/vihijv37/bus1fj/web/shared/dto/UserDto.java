package hu.bme.vihijv37.bus1fj.web.shared.dto;

import java.util.HashSet;
import java.util.Set;

public class UserDto extends EntityBaseDto {

    private static final long serialVersionUID = 817120011473348758L;

    private String email;

    private String password;

    private String name;

    private Set<UploadDto> uploads = new HashSet<UploadDto>();

    public String getEmail() {
	return this.email;
    }

    public String getName() {
	return this.name;
    }

    public String getPassword() {
	return this.password;
    }

    public Set<UploadDto> getUploads() {
	return this.uploads;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @Override
    public String toString() {
	return super.toString("User") + "[" + this.email + "]";
    }

}
