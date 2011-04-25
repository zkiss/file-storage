package hu.bme.vihijv37.bus1fj.web.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import hu.bme.vihijv37.bus1fj.web.server.entity.File;
import hu.bme.vihijv37.bus1fj.web.server.entity.User;

public class FsServiceDao extends AbstractDao {

    public FsServiceDao(EntityManager entityManager) {
	super(entityManager);
    }

    /**
     * Felhasználó keresése ID alapján
     * 
     * @param id
     * @return
     * @throws DaoException
     */
    public User findUserById(long id) throws DaoException {
	try {
	    User user = (User) this.getEntityManager().createQuery( //
		    "select u from " + User.class.getSimpleName() + " u" + //
			    " where u.id = :id"). //
		    setParameter("id", id).getSingleResult();
	    return user;
	} catch (RuntimeException ex) {
	    throw new DaoException("Could not find User by id", ex);
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
	    throw new DaoException("Could not get User by email and password", e);
	}
    }

    /**
     * Új {@link File} mentése
     * 
     * @param file
     * @throws DaoException
     */
    public File insertFile(File file) throws DaoException {
	try {
	    this.getEntityManager().persist(file);
	    return file;
	} catch (RuntimeException e) {
	    throw new DaoException("Could not persist " + file, e);
	}
    }

    /**
     * Új {@link User} mentése
     * 
     * @param user
     * @return
     * @throws DaoException
     */
    public User insertUser(User user) throws DaoException {
	try {
	    this.getEntityManager().persist(user);
	    return user;
	} catch (RuntimeException e) {
	    throw new DaoException("Could not persist " + user, e);
	}
    }

    /**
     * {@link File} rekord törlése
     * 
     * @param fileId
     *            a File azonosítója
     * @throws DaoException
     */
    public void removeFile(long fileId) throws DaoException {
	try {
	    this.getEntityManager().createQuery( //
		    "delete from " + File.class.getSimpleName() + //
			    " where id = :id"). //
		    setParameter("id", fileId).executeUpdate();
	} catch (RuntimeException e) {
	    throw new DaoException("Could not delete File #" + fileId, e);
	}
    }

    public User updateUser(long userId, String email, String name, String newPasswordHash) throws DaoException {
	try {
	    User user = this.findUserById(userId);
	    user.setEmail(email);
	    user.setName(name);
	    if (newPasswordHash != null) {
		user.setPassword(newPasswordHash);
	    }
	    return this.getEntityManager().merge(user);
	} catch (RuntimeException e) {
	    throw new DaoException("Could not update User #" + userId, e);
	}
    }
}
