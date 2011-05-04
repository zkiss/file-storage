package hu.bme.vihijv37.bus1fj.web.server.uploader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hu.bme.vihijv37.bus1fj.web.server.JpaManager;
import hu.bme.vihijv37.bus1fj.web.server.ServerUtils;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.server.entity.Upload;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.UploadFormConstants;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Fájlok feltöltését lekezelő servlet implementáció. Sajnos ezt a GWT még nem
 * támogatja beépítetten.
 * 
 * @author Zoltan Kiss
 */
public class FileUploaderServlet extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 4157585117575772801L;
    private static final Log LOG = LogFactory.getLog(FileUploaderServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/plain");
	FileItem uploadItem = this.getFileItem(request, UploadFormConstants.UPLOADER_FORM_ELEMENT_ID);
	if (uploadItem == null) {
	    FileUploaderServlet.LOG.debug("Did not receive file upload");
	    response.getWriter().write("NO-SCRIPT-DATA");
	    return;
	}

	File destinationFile = null;
	EntityManager em = null;
	EntityTransaction transaction = null;
	try {
	    final long userId = Long.parseLong(request.getParameter(UploadFormConstants.USER_ID));
	    em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	    final FsServiceDao dao = new FsServiceDao(em);

	    User user = dao.get(User.class, userId);
	    final String uploadPath = uploadItem.getName();
	    destinationFile = new File(ServerUtils.getUploadDirRelativePath(user, uploadPath));
	    File uploadDir = destinationFile.getParentFile();
	    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
		FileUploaderServlet.LOG.error("Could not create directory: " + uploadDir.getAbsolutePath());
	    } else {
		if (destinationFile.createNewFile()) {
		    uploadItem.write(destinationFile);

		    Upload upload = new Upload();
		    upload.setPath(uploadPath);
		    upload.setUser(user);

		    transaction = em.getTransaction();
		    transaction.begin();
		    dao.insert(upload);
		    transaction.commit();
		} else {
		    FileUploaderServlet.LOG.info("File already exists: " + destinationFile.getAbsolutePath());
		}
	    }
	} catch (Exception e) {
	    FileUploaderServlet.LOG.error("Could not save upload", e);
	    if ((destinationFile != null) && destinationFile.exists() && destinationFile.delete()) {
		FileUploaderServlet.LOG.fatal( //
			"An error occured during the file upload process and could not delete the uploaded file, this file should be manually removed: "
				+ destinationFile.getAbsolutePath());
	    }
	    if ((transaction != null) && transaction.isActive()) {
		transaction.rollback();
	    }
	    if (em != null) {
		em.close();
	    }
	}
    }

    private FileItem getFileItem(HttpServletRequest request, String fieldName) {
	ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
	try {
	    List<?> items = upload.parseRequest(request);
	    Iterator<?> it = items.iterator();
	    while (it.hasNext()) {
		FileItem item = (FileItem) it.next();
		if (!item.isFormField() && fieldName.equals(item.getFieldName())) {
		    return item;
		}
	    }
	} catch (FileUploadException e) {
	    FileUploaderServlet.LOG.error("Could not parse request", e);
	    return null;
	}
	return null;
    }
}
