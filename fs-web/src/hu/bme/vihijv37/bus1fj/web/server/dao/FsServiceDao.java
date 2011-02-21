package hu.bme.vihijv37.bus1fj.web.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;

public class FsServiceDao extends AbstractDao {

    public FsServiceDao(EntityManager entityManager) {
	super(entityManager);
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

}
