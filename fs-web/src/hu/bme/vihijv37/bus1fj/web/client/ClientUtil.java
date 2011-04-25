package hu.bme.vihijv37.bus1fj.web.client;

public class ClientUtil {

    public static String getFileName(String filePath) {
	int lastIndexOf = filePath.lastIndexOf("\\");
	if (lastIndexOf >= 0) {
	    return filePath.substring(lastIndexOf + 1);
	}
	return filePath;
    }
}
