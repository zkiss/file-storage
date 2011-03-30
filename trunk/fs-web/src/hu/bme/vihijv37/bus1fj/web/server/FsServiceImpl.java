package hu.bme.vihijv37.bus1fj.web.server;

import javax.persistence.EntityManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hu.bme.vihijv37.bus1fj.web.client.FsService;
import hu.bme.vihijv37.bus1fj.web.server.converter.Converter;
import hu.bme.vihijv37.bus1fj.web.server.converter.ConverterException;
import hu.bme.vihijv37.bus1fj.web.server.dao.DaoException;
import hu.bme.vihijv37.bus1fj.web.server.dao.FsServiceDao;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;
import hu.bme.vihijv37.bus1fj.web.shared.exception.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FsServiceImpl extends RemoteServiceServlet implements FsService {

    private static final long serialVersionUID = -8112597734192747655L;

    private static final Log LOG = LogFactory.getLog(FsServiceImpl.class);

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
}
