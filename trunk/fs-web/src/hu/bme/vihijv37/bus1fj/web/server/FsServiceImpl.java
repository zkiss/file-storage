package hu.bme.vihijv37.bus1fj.web.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hu.bme.vihijv37.bus1fj.web.client.FsService;
import hu.bme.vihijv37.bus1fj.web.server.converter.Converter;
import hu.bme.vihijv37.bus1fj.web.server.converter.ConverterException;
import hu.bme.vihijv37.bus1fj.web.server.dao.DaoException;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.server.entity.Upload;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UploadDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FsServiceImpl extends RemoteServiceServlet implements FsService {

    private static final long serialVersionUID = -8112597734192747655L;
    private static final Log LOG = LogFactory.getLog(FsServiceImpl.class);

    @Override
    public List<UploadDto> getUserUploads(long userId) throws ServiceException {
	EntityManager em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	try {
	    FsServiceDao dao = new FsServiceDao(em);
	    User user = dao.get(User.class, userId);
	    List<Upload> userUploads = dao.getUserUploads(userId);
	    List<UploadDto> ret = new ArrayList<UploadDto>(userUploads.size());
	    for (Upload upload : userUploads) {
		UploadDto dto = Converter.convert(upload);
		/*
		 * DB-ben csak a user-dir-relatív útvonalak vannak letárolva, de
		 * kliens oldalon az "abszolút" útvonalra van szükség
		 */
		dto.setUrlPath(ServerUtils.getUploadDirRelativePath(user, upload.getPath()));
		ret.add(dto);
	    }
	    return ret;
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error("Could not get user uploads", e);
	    throw new ServiceException("An internal server error occured");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error("Could not convert Entity to DTO", e);
	    throw new ServiceException("An internal server error occured");
	} finally {
	    em.close();
	}
    }

    @Override
    public UserDto login(String userName, String password) throws ServiceException {
	EntityManager em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	User user;
	try {
	    user = new FsServiceDao(em).getUser(userName, ServerUtils.hashPassword(password));
	    if (user == null) {
		throw new ServiceException("No such user");
	    }
	    FsServiceImpl.LOG.info("User logged in: " + user);
	    return Converter.convert(user);
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error("Could not execute query", e);
	    throw new ServiceException("An internal server error occured");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error("Could not convert Entity to DTO", e);
	    throw new ServiceException("An internal server error occured");
	} finally {
	    em.close();
	}
    }

    @Override
    public UserDto register(String name, String email, String password) throws ServiceException {
	EntityManager em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    FsServiceDao dao = new FsServiceDao(em);
	    User user = new User();
	    user.setEmail(email);
	    user.setName(name);
	    user.setPassword(ServerUtils.hashPassword(password));

	    transaction.begin();
	    user = dao.insert(user);
	    transaction.commit();
	    FsServiceImpl.LOG.info("User registered: " + user);
	    return Converter.convert(user);
	} catch (DaoException ex) {
	    FsServiceImpl.LOG.error(ex.getMessage(), ex);
	    throw new ServiceException("An internal server error occured");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("An internal server error occured");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	    em.close();
	}
    }

    @Override
    public void removeFile(long uploadId) throws ServiceException {
	EntityManager em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    FsServiceDao dao = new FsServiceDao(em);
	    hu.bme.vihijv37.bus1fj.web.server.entity.Upload upload = dao.get(Upload.class, uploadId);
	    transaction.begin();
	    dao.delete(Upload.class, uploadId);
	    File uploadedFile = ServerUtils.getUploadFile(upload.getUser(), upload.getPath());
	    if (uploadedFile.isFile() && !uploadedFile.delete()) {
		throw new ServiceException("Could not delete file");
	    }
	    transaction.commit();
	    FsServiceImpl.LOG.info(upload + " deleted");
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error("Could not remove Upload #" + uploadId, e);
	    throw new ServiceException("Could not remove file");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	    em.close();
	}
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws ServiceException {
	EntityManager em = JpaManager.getInstance().getEntityManagerFactory().createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    FsServiceDao dao = new FsServiceDao(em);
	    User user = Converter.convert(userDto);
	    if (user.getPassword() == null) {
		User userInDb = dao.get(User.class, userDto.getId());
		user.setPassword(userInDb.getPassword());
	    } else {
		/*
		 * DTO-ból jövő jelszó még nincsen hash-elve
		 */
		user.setPassword(ServerUtils.hashPassword(user.getPassword()));
	    }

	    transaction.begin();
	    user = dao.update(user);
	    transaction.commit();
	    FsServiceImpl.LOG.info("User data saved: " + userDto);
	    return Converter.convert(user);
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error("Could not save " + userDto, e);
	    throw new ServiceException("An internal server error occured");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error("Could not convert " + userDto, e);
	    throw new ServiceException("An internal server error occured");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	    em.close();
	}
    }

}
