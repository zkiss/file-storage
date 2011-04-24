package hu.bme.vihijv37.bus1fj.web.server.uploader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UploaderUtil {
    private static final String DEFAULT_UPLOAD_PATH = "files";
    private static final String UPLOADER_PROP_URL = "/upload.properties";
    private static final String PATH_KEY = "path";
    private static final Log LOG = LogFactory.getLog(UploaderUtil.class);
    private static final File UPLOAD_DIR;

    static {
	Properties properties = new Properties();
	InputStream resourceAsStream = null;
	try {
	    resourceAsStream = UploaderUtil.class.getResourceAsStream(UploaderUtil.UPLOADER_PROP_URL);
	    if (resourceAsStream != null) {
		properties.load(resourceAsStream);
	    }
	} catch (IOException ex) {
	    UploaderUtil.LOG.error("Could not read properties file", ex);
	} finally {
	    if (resourceAsStream != null) {
		try {
		    resourceAsStream.close();
		} catch (IOException e) {
		    UploaderUtil.LOG.error("Could not close stream", e);
		}
	    }
	}
	String pathUrl = properties.getProperty(UploaderUtil.PATH_KEY);
	if (pathUrl != null) {
	    UPLOAD_DIR = new File(pathUrl);
	} else {
	    UPLOAD_DIR = new File(UploaderUtil.DEFAULT_UPLOAD_PATH);
	}
	if (!UploaderUtil.UPLOAD_DIR.isDirectory()) {
	    if (!UploaderUtil.UPLOAD_DIR.mkdirs()) {
		UploaderUtil.LOG.error("Could not create directory: " + UploaderUtil.UPLOAD_DIR.getAbsolutePath());
	    }
	}
    }

    public static File getUploaderDir() {
	return UploaderUtil.UPLOAD_DIR;
    }

}
