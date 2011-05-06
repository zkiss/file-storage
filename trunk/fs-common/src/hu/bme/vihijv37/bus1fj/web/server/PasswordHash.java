package hu.bme.vihijv37.bus1fj.web.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link User} entitás jelszavát hash-eli
 * 
 * @author Zoltan Kiss
 */
public final class PasswordHash {

    private static final Log LOG = LogFactory.getLog(PasswordHash.class);

    private static final char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Hash-eli a jelszót
     * 
     * @param plainPassword
     * @return
     */
    public static String hashPassword(String plainPassword) {
	StringBuilder sb = null;

	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");

	    byte[] bytes = md.digest(plainPassword.getBytes());
	    sb = new StringBuilder(2 * bytes.length);
	    for (int i = 0; i < bytes.length; i++) {
		int low = (bytes[i] & 0x0f);
		int high = ((bytes[i] & 0xf0) >> 4);
		sb.append(PasswordHash.HEXADECIMAL[high]);
		sb.append(PasswordHash.HEXADECIMAL[low]);
	    }
	} catch (NoSuchAlgorithmException e) {
	    // lehetetlen
	    PasswordHash.LOG.fatal("Could not hash password", e);
	}

	return sb != null ? sb.toString() : "";
    }

    private PasswordHash() {
	throw new UnsupportedOperationException("This class should not be instantiated");
    }

}
