package hu.bme.vihijv37.bus1fj.web.server.dao;

public class DaoException extends Exception {

    private static final long serialVersionUID = -8907155023063351082L;

    public DaoException() {
	super();
    }

    public DaoException(String message) {
	super(message);
    }

    public DaoException(String message, Throwable cause) {
	super(message, cause);
    }

    public DaoException(Throwable cause) {
	super(cause);
    }

}
