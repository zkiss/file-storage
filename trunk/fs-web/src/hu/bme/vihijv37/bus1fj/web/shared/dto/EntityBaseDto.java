package hu.bme.vihijv37.bus1fj.web.shared.dto;

import java.io.Serializable;

/**
 * Közös ősosztály az entityásoknak. Tartalmaz egy lekérdezhető ID-t, amit a
 * {@link #hashCode()} és az {@link #equals(Object)} metódusában használ. Ezeket
 * a metódusokat nem lehet felülírni.
 * 
 * @author Zoltan Kiss
 */
public abstract class EntityBaseDto implements Serializable {

    private static final long serialVersionUID = 2355552186266075607L;

    long id;

    @Override
    public final boolean equals(Object obj) {
	boolean ret;
	if (obj == null) {
	    ret = false;
	} else if (obj == this) {
	    ret = true;
	} else if (obj.getClass().equals(this.getClass())) {
	    ret = this.id == ((EntityBaseDto) obj).id;
	    if (ret && (this.id == 0)) {
		/*
		 * Ilyenkor az entitás ID-je 0, vagyis még nincsen adatbázisba
		 * mentve, valamint nem azonos a referenciája obj-éval, ami
		 * szintén egy még nem mentett entitás. Ebben az esetben az
		 * identitást különbözőnek vesszük.
		 */
		ret = false;
	    }
	} else {
	    ret = false;
	}
	return ret;
    }

    public final long getId() {
	return this.id;
    }

    @Override
    public final int hashCode() {
	return Long.valueOf(this.id).hashCode();
    }

    @Override
    public String toString() {
	return this.toString("EntityBase");
    }

    String toString(String className) {
	return className + " [#" + this.id + "]";
    }

}
