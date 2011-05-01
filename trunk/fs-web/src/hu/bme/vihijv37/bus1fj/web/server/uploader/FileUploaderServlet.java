package hu.bme.vihijv37.bus1fj.web.server.uploader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hu.bme.vihijv37.bus1fj.web.server.JpaManager;
import hu.bme.vihijv37.bus1fj.web.server.ServerProperties;
import hu.bme.vihijv37.bus1fj.web.server.dao.DaoException;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.server.entity.Upload;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadFormConstants;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploaderServlet extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 4157585117575772801L;
    private static final Log LOG = LogFactory.getLog(FileUploaderServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/plain");
	FileItem uploadItem = this.getFileItem(request);
	if (uploadItem == null) {
	    response.getWriter().write("NO-SCRIPT-DATA");
	    return;
	}

	File destinationFile = null;
	try {
	    String userId = request.getParameter(UploadFormConstants.PARAM_USER_ID);
	    long userIdLong = Long.parseLong(userId);
	    User user = this.findUserById(userIdLong);
	    if (user != null) {
		File userDir = new File(ServerProperties.getUploadDirectory(), user.getEmail());
		if (!userDir.exists()) {
		    userDir.mkdirs();
		}
		destinationFile = new File(userDir.getPath(), uploadItem.getName());
		if (destinationFile.createNewFile()) {
		    uploadItem.write(destinationFile);
		    String relativePath = ServerProperties.getUploadDirectory() + "\\" + user.getEmail() + "\\" + uploadItem.getName();
		    this.insertFile(relativePath, userIdLong);
		}
	    }
	} catch (Exception ex) {
	    if ((destinationFile != null) && destinationFile.exists()) {
		destinationFile.delete();
	    }
	    FileUploaderServlet.LOG.error(ex.getMessage(), ex);
	}

    }

    private User findUserById(long userId) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	try {
	    return new FsServiceDao(em).get(User.class, userId);
	} catch (DaoException ex) {
	    FileUploaderServlet.LOG.error(ex.getMessage(), ex);
	    throw new ServiceException("Could not delete file from db");
	} finally {
	    em.close();
	}
    }

    private FileItem getFileItem(HttpServletRequest request) {
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	try {
	    List<?> items = upload.parseRequest(request);
	    Iterator<?> it = items.iterator();
	    while (it.hasNext()) {
		FileItem item = (FileItem) it.next();
		if (!item.isFormField() && UploadFormConstants.UPLOADER_FORM_ELEMENT_ID.equals(item.getFieldName())) {
		    return item;
		}
	    }
	} catch (FileUploadException e) {
	    return null;
	}
	return null;
    }

    private void insertFile(String path, long userId) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    transaction.begin();
	    Upload file = new Upload();
	    FsServiceDao dao = new FsServiceDao(em);
	    file.setPath(path);
	    file.setUser(dao.get(User.class, userId));
	    dao.insert(file);
	    transaction.commit();
	} catch (DaoException e) {
	    FileUploaderServlet.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not delete file from db");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	    em.close();
	}
    }
}
