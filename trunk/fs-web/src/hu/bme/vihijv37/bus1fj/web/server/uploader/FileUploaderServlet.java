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
import hu.bme.vihijv37.bus1fj.web.server.dao.DaoException;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploaderServlet extends HttpServlet implements Servlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Log LOG = LogFactory.getLog(FileUploaderServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/plain");
	FileItem uploadItem = this.getFileItem(request);
	if (uploadItem == null) {
	    response.getWriter().write("NO-SCRIPT-DATA");
	    return;
	}

	File destinationFile = new File(UploaderUtil.getUploaderDir().getAbsolutePath(), uploadItem.getName());

	try {
	    if (destinationFile.createNewFile()) {
		uploadItem.write(destinationFile);
		String userId = request.getParameter("userId");
		long userIdLong = Long.parseLong(userId);
		this.inserFile(destinationFile.getPath(), userIdLong);
	    }
	} catch (NumberFormatException e) {
	    destinationFile.delete();
	    FileUploaderServlet.LOG.error(e.getMessage(), e);
	} catch (ServiceException e) {
	    destinationFile.delete();
	    FileUploaderServlet.LOG.error(e.getMessage(), e);
	} catch (Exception ex) {
	    destinationFile.delete();
	    FileUploaderServlet.LOG.error(ex.getMessage(), ex);
	}

    }

    private UserDto findUserById(long userId) throws ServiceException {
	throw new UnsupportedOperationException();
    }

    private FileItem getFileItem(HttpServletRequest request) {
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	try {
	    List<?> items = upload.parseRequest(request);
	    Iterator<?> it = items.iterator();
	    while (it.hasNext()) {
		FileItem item = (FileItem) it.next();
		if (!item.isFormField() && "uploadFormElement".equals(item.getFieldName())) {
		    return item;
		}
	    }
	} catch (FileUploadException e) {
	    return null;
	}
	return null;
    }

    private void inserFile(String path, long userId) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    transaction.begin();
	    new FsServiceDao(em).insertFile(path, userId);
	    transaction.commit();
	} catch (DaoException ex) {
	    transaction.rollback();
	    FileUploaderServlet.LOG.error(ex.getMessage(), ex);
	    throw new ServiceException("Could not delete file from db");
	}
    }
}
