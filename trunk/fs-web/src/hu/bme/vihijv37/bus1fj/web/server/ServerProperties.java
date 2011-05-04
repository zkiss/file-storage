package hu.bme.vihijv37.bus1fj.web.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A szerver oldali beállításokat tároló <code>server.properties</code> fájlból
 * olvassa fel a beállításokat és azokat objektumorientált módon elérhetővé
 * teszi.
 * 
 * @author Zoltan Kiss
 */
public class ServerProperties {

    private static final Log LOG = LogFactory.getLog(ServerProperties.class);
    private static final String PROPERTIES_FILE = "/server.properties";
    private static final String UPLOAD_DIR_DEFAULT = "files";
    private static final String UPLOAD_DIR_KEY = "uploadDir";
    private static final File UPLOAD_DIR;

    static {
	Properties properties = new Properties();
	InputStream is = null;
	try {
	    is = ServerProperties.class.getResourceAsStream(ServerProperties.PROPERTIES_FILE);
	    if (is != null) {
		properties.load(is);
	    }
	} catch (Exception e) {
	    ServerProperties.LOG.error("Could not read properties file", e);
	} finally {
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException e) {
		    ServerProperties.LOG.error("Could not close inputstream", e);
		}
	    }
	}
	String uploadDir = properties.getProperty(ServerProperties.UPLOAD_DIR_KEY, ServerProperties.UPLOAD_DIR_DEFAULT);
	UPLOAD_DIR = new File(uploadDir);
	if (!ServerProperties.UPLOAD_DIR.isDirectory() && !ServerProperties.UPLOAD_DIR.mkdirs()) {
	    ServerProperties.LOG.error("Could not create directory: " + ServerProperties.UPLOAD_DIR.getAbsolutePath());
	}
    }

    /**
     * Az a könyvtár, ahova a felhasználók által feltöltött fájlok kerülnek. A
     * könyvtár relatív elérési út a webszerver publikus mappájához viszonyítva
     * 
     * @return
     */
    public static File getUploadDirectory() {
	return ServerProperties.UPLOAD_DIR;
    }

}
