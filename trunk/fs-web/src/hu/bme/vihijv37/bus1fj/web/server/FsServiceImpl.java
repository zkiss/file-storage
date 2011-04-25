package hu.bme.vihijv37.bus1fj.web.server;

import java.io.File;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hu.bme.vihijv37.bus1fj.web.client.FsService;
import hu.bme.vihijv37.bus1fj.web.server.converter.Converter;
import hu.bme.vihijv37.bus1fj.web.server.converter.ConverterException;
import hu.bme.vihijv37.bus1fj.web.server.dao.DaoException;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.FileDto;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FsServiceImpl extends RemoteServiceServlet implements FsService {

    private static final long serialVersionUID = -8112597734192747655L;
    private static final Log LOG = LogFactory.getLog(FsServiceImpl.class);

    @Override
    public Set<FileDto> getUserFiles(UserDto userDto) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	User user;
	try {
	    user = new FsServiceDao(emf.createEntityManager()).findUserById(userDto.getId());
	    user.getFiles().size();
	    UserDto convertedUser = Converter.convert(user);
	    return convertedUser.getFiles();
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not execute database query");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not convert Entity to DTO");
	}

    }

    @Override
    public UserDto login(String userName, String password) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	User user;
	try {
	    user = new FsServiceDao(emf.createEntityManager()).getUser(userName, password);
	    if (user == null) {
		throw new ServiceException("No such user");
	    }
	    return Converter.convert(user);
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not execute database query");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not convert Entity to DTO");
	}
    }

    @Override
    public UserDto register(String name, String email, String password) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    // TODO pass hash
	    FsServiceDao dao = new FsServiceDao(em);
	    User user = new User();
	    user.setEmail(email);
	    user.setName(name);
	    user.setPassword(password);
	    transaction.begin();
	    user = dao.insertUser(user);
	    transaction.commit();
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
	}
    }

    @Override
    public void removeFile(FileDto file) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    transaction.begin();
	    new FsServiceDao(em).removeFile(file.getId());
	    File uploadedFile = new File(file.getPath());
	    if (uploadedFile.isFile() && !uploadedFile.delete()) {
		throw new ServiceException("Could not delete file");
	    }
	    transaction.commit();
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("Could not remove file");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	}
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    transaction.begin();
	    // TODO pass hash
	    User user = new FsServiceDao(em).updateUser(userDto.getId(), userDto.getEmail(), userDto.getName(), userDto.getPassword());
	    transaction.commit();
	    return Converter.convert(user);
	} catch (DaoException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("An internal server error occured");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    throw new ServiceException("An internal server error occured");
	} finally {
	    if (transaction.isActive()) {
		transaction.rollback();
	    }
	}
    }
}
