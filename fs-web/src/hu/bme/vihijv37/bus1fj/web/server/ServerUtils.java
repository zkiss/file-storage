package hu.bme.vihijv37.bus1fj.web.server;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Jelszó hashelésének az algoritmusát tartalmazza
 * 
 * @author Zoltan Kiss
 */
public final class ServerUtils {

    private static final Log LOG = LogFactory.getLog(ServerUtils.class);

    private static final char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * A fájl elérési útját adja meg a publikus mappához viszonyítva
     * 
     * @param user
     *            a feltöltés gazdája
     * @param uploadPath
     *            a feltöltött fájl user tárolómappához viszonyított relatív
     *            elérési útja (fájlnév)
     * @return
     */
    public static String getUploadDirRelativePath(User user, String uploadPath) {
	return ServerProperties.getUploadDirectory().getPath() + File.separator + //
		user.getEmail() + File.separator + //
		uploadPath;
    }

    /**
     * Visszaadja a pontos elérési útját egy feltöltésnek a szerveren
     * 
     * @param user
     *            a feltöltés gazdája
     * @param uploadPath
     *            a fájl neve
     * @return
     */
    public static File getUploadFile(User user, String uploadPath) {
	return new File(ServerProperties.getUploadDirectory(), //
		ServerUtils.getUploadDirRelativePath(user, uploadPath));
    }

    /**
     * Hash-eli a jelszót
     * 
     * @param plainPassword
     * @return
     */
    public static String hashPassword(String plainPassword) {
	StringBuilder sb = null;

	try {
	    MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
	    md.reset();

	    byte[] bytes = md.digest(plainPassword.getBytes());
	    sb = new StringBuilder(2 * bytes.length);
	    for (int i = 0; i < bytes.length; i++) {
		int low = (bytes[i] & 0x0f);
		int high = ((bytes[i] & 0xf0) >> 4);
		sb.append(ServerUtils.HEXADECIMAL[high]);
		sb.append(ServerUtils.HEXADECIMAL[low]);
	    }
	} catch (NoSuchAlgorithmException e) {
	    ServerUtils.LOG.error("Could not hash password", e);
	}

	return sb != null ? sb.toString() : "";
    }

    private ServerUtils() {
	throw new UnsupportedOperationException("This class should not be instantiated");
    }

}
