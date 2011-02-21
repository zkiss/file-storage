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
     */
    public User getUser(String email, String passwordHash) {
	List<User> users = AbstractDao.getResultList(this.getEntityManager().createQuery( //
		"select u from " + User.class.getSimpleName() + " u" + //
			" where u.email = :email" + //
			" and u.password = :pwHash"). //
		setParameter("email", email). //
		setParameter("pwHash", passwordHash), User.class);
	return users.size() == 1 ? users.get(0) : null;
    }

}
