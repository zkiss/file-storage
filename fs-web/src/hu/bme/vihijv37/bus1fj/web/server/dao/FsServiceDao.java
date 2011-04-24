package hu.bme.vihijv37.bus1fj.web.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;
import hu.bme.vihijv37.bus1fj.web.shared.dto.UserDto;

public class FsServiceDao extends AbstractDao {

    public FsServiceDao(EntityManager entityManager) {
	super(entityManager);
    }

    public User findUserById(long id) throws DaoException {
	try {
	    User user = AbstractDao.getResult(this.getEntityManager().createQuery(//
		    "select u from " + User.class.getSimpleName() + " u" //
			    + " where u.id = :id")//
		    .setParameter("id", id), User.class);
	    return user;
	} catch (RuntimeException ex) {
	    throw new DaoException(ex.getMessage(), ex);
	}
    }

    /**
     * Felhasználó keresése email és jelszó hash alapján
     * 
     * @param email
     *            a felhasználó e-mail címe
     * @param passwordHash
     *            a felhasználó jelszavának MD5 hash-e
     * @return
     * @throws DaoException
     *             ha a JPA lekérdezés nem futott le sikeresen
     */
    public User getUser(String email, String passwordHash) throws DaoException {
	try {
	    List<User> users = AbstractDao.getResultList(this.getEntityManager().createQuery( //
		    "select u from " + User.class.getSimpleName() + " u" + //
			    " where u.email = :email" + //
			    " and u.password = :pwHash"). //
		    setParameter("email", email). //
		    setParameter("pwHash", passwordHash), User.class);
	    return users.size() == 1 ? users.get(0) : null;
	} catch (RuntimeException e) {
	    throw new DaoException(e.getMessage(), e);
	}
    }

    public User insertUser(String name, String email, String password) throws DaoException {
	try {
	    User user = new User();
	    user.setEmail(email);
	    user.setName(name);
	    user.setPassword(password);
	    this.getEntityManager().persist(user);
	    return user;
	} catch (RuntimeException e) {
	    throw new DaoException(e.getMessage(), e);
	}
    }

    public User updateUser(UserDto userDto) throws DaoException {
	try {
	    User user = this.findUserById(userDto.getId());
	    user.setEmail(userDto.getEmail());
	    user.setName(userDto.getName());
	    if (userDto.getPassword() != null) {
		user.setPassword(userDto.getPassword());
	    }
	    this.getEntityManager().merge(user);
	    return user;
	} catch (RuntimeException ex) {
	    throw new DaoException(ex.getMessage(), ex);
	}
    }
}
