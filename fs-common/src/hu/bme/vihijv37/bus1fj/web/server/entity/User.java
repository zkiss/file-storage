package hu.bme.vihijv37.bus1fj.web.server.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User extends EntityBase {

    private static final long serialVersionUID = 817120011473348758L;

    @Column(name = "EMAIL", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", length = 32, nullable = false)
    private String password;

    @Column(name = "NAME", length = 255, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<File> files = new HashSet<File>();

    public String getEmail() {
	return this.email;
    }

    public Set<File> getFiles() {
	return this.files;
    }

    public String getName() {
	return this.name;
    }

    public String getPassword() {
	return this.password;
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
	return super.toString() + "[" + this.email + "]";
    }

}
