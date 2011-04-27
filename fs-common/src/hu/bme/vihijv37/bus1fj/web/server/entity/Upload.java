package hu.bme.vihijv37.bus1fj.web.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Felhasználók által feltöltött fájl reprezentációja
 * 
 * @author Zoltan Kiss
 */
@Entity
@Table(name = "UPLOADS")
public class Upload extends EntityBase {

    private static final long serialVersionUID = -6875800091975550956L;

    @Column(name = "PATH", nullable = false, length = 255)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    /**
     * A fájl relatív elérési útja a szerveren
     * 
     * @return
     */
    public String getPath() {
	return this.path;
    }

    /**
     * A fájlt feltöltő felhasználó
     * 
     * @return
     */
    public User getUser() {
	return this.user;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public String toString() {
	return super.toString() + "[" + this.path + "]";
    }

}
