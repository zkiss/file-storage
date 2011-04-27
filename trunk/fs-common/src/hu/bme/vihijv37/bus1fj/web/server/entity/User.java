package hu.bme.vihijv37.bus1fj.web.server.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Felhasználó
 * 
 * @author Zoltan Kiss
 */
@Entity
@Table(name = "USERS")
public class User extends EntityBase {

    private static final long serialVersionUID = 817120011473348758L;

    @Column(name = "EMAIL", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", length = 32, nullable = false)
    private String password;

    @Column(name = "NAME", length = 255, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Upload> uploads = new HashSet<Upload>();

    /**
     * A felhasználó e-mail címe
     * 
     * @return
     */
    public String getEmail() {
	return this.email;
    }

    /**
     * A felhasználó neve
     * 
     * @return
     */
    public String getName() {
	return this.name;
    }

    /**
     * A felhasználó jelszavának MD5 hash-e
     * 
     * @return
     */
    public String getPassword() {
	return this.password;
    }

    /**
     * A felhasználó által feltöltött fájlok
     * 
     * @return
     */
    public Set<Upload> getUploads() {
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
	return super.toString() + "[" + this.email + "]";
    }

}
