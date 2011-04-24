package hu.bme.vihijv37.bus1fj.web.server;

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
	User user;
	try {
	    transaction.begin();
	    // TODO pass hash
	    user = new FsServiceDao(em).insertUser(name, email, password);
	    transaction.commit();
	    return Converter.convert(user);
	} catch (DaoException ex) {
	    FsServiceImpl.LOG.error(ex.getMessage(), ex);
	    transaction.rollback();
	    throw new ServiceException("Could not insert user to db");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    transaction.rollback();
	    throw new ServiceException("Could not convert Entity to DTO");
	}
    }

    @Override
    public void removeFile(FileDto file) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	try {
	    transaction.begin();
	    new FsServiceDao(em).removeFile(file);
	    transaction.commit();
	} catch (DaoException ex) {
	    FsServiceImpl.LOG.error(ex.getMessage(), ex);
	    transaction.rollback();
	    throw new ServiceException("Could not remove file from db");
	}
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws ServiceException {
	EntityManagerFactory emf = JpaManager.getInstance().getEntityManagerFactory();
	EntityManager em = emf.createEntityManager();
	EntityTransaction transaction = em.getTransaction();
	User user;
	try {
	    transaction.begin();
	    // TODO pass hash
	    user = new FsServiceDao(em).updateUser(userDto);
	    transaction.commit();
	    return Converter.convert(user);
	} catch (DaoException ex) {
	    FsServiceImpl.LOG.error(ex.getMessage(), ex);
	    transaction.rollback();
	    throw new ServiceException("Could not insert user to db");
	} catch (ConverterException e) {
	    FsServiceImpl.LOG.error(e.getMessage(), e);
	    transaction.rollback();
	    throw new ServiceException("Could not convert Entity to DTO");
	}
    }
}
