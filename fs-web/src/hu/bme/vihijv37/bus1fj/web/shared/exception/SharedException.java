package hu.bme.vihijv37.bus1fj.web.shared.exception;

/**
 * Olyan exception, amit a kliens oldal is biztosan lát, tehát nem dob hibát a
 * szerializációja
 * 
 * @author Zoltan Kiss
 */
public abstract class SharedException extends Exception {

    private static final long serialVersionUID = -8394756100463075334L;

    SharedException() {
	// GWT miatt kell
    }

    public SharedException(String message) {
	super(message);
    }

    public SharedException(String message, SharedException cause) {
	super(message, cause);
    }

}
