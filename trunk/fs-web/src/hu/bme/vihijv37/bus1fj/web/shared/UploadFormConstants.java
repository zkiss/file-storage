package hu.bme.vihijv37.bus1fj.web.shared;

/**
 * A fájlfeltöltő form-ban használt konstansok
 * 
 * @author Zoltan Kiss
 */
public interface UploadFormConstants {

    /**
     * A fájlfeltöltést lekezelő servlet neve
     */
    String SERVLET_URL = "FsWeb/fileUploader";

    /**
     * A feltöltött fájlt tartalmazó form elem neve
     */
    String UPLOADER_FORM_ELEMENT_ID = "uploadFormElement";

    /**
     * A fájlt feltöltő felhasználó azonosítója
     */
    String USER_ID = "userId";

}
