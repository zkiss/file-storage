package hu.bme.vihijv37.bus1fj.web.server;

import java.io.File;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;

/**
 * Szerver oldali utility függvényeket tartalmaz
 * 
 * @author Zoltan Kiss
 */
public final class ServerUtils {

    /**
     * A fájl elérési útját adja meg a webszerver publikus mappájához
     * viszonyítva
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
	return PasswordHash.hashPassword(plainPassword);
    }

    private ServerUtils() {
	throw new UnsupportedOperationException("This class should not be instantiated");
    }

}
