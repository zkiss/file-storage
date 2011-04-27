package hu.bme.vihijv37.bus1fj.web.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractDao {

    @SuppressWarnings("unchecked")
    public static <T> List<T> getResultList(Query query, Class<T> clazz) throws DaoException {
	try {
	    return query.getResultList();
	} catch (RuntimeException e) {
	    throw new DaoException("Could not get result list", e);
	}
    }

    private final EntityManager entityManager;

    public AbstractDao(EntityManager entityManager) {
	this.entityManager = entityManager;
    }

    /**
     * Entitás törlése
     * 
     * @param entityClass
     * @param id
     * @throws DaoException
     *             ha nem sikerült törölni
     */
    public void delete(Class<?> entityClass, long id) throws DaoException {
	try {
	    this.getEntityManager().createQuery( //
		    "delete from " + entityClass.getSimpleName() + //
			    " where id = :id"). //
		    setParameter("id", id).executeUpdate();
	} catch (RuntimeException e) {
	    throw new DaoException("Could not delete " + entityClass.getSimpleName() + " #" + id, e);
	}
    }

    /**
     * Entitás lekérdezése ID alapján. Ha nem találja az entitást, exception-t
     * dob.
     * 
     * @param <T>
     * @param entityClass
     *            entitás típusa
     * @param entityId
     *            entitás ID-je
     * @return
     * @throws DaoException
     *             ha nem találja a keresett entitást
     */
    public <T> T get(Class<T> entityClass, long entityId) throws DaoException {
	try {
	    T entity = this.entityManager.find(entityClass, entityId);
	    if (entity == null) {
		throw new DaoException("Could not find " + entityClass.getSimpleName() + " #" + entityId);
	    }
	    return entity;
	} catch (RuntimeException e) {
	    throw new DaoException("Could not execute query", e);
	}
    }

    protected EntityManager getEntityManager() {
	return this.entityManager;
    }

    /**
     * Új entitás mentése
     * 
     * @param <T>
     * @param entity
     * @return
     * @throws DaoException
     *             ha sikertelen a mentés
     */
    public <T> T insert(T entity) throws DaoException {
	try {
	    this.getEntityManager().persist(entity);
	    return entity;
	} catch (RuntimeException e) {
	    throw new DaoException("Could not persist " + entity, e);
	}
    }

    /**
     * Entitás módosításainak mentése
     * 
     * @param <T>
     * @param entity
     * @return
     * @throws DaoException
     *             ha sikertelen a mentés
     */
    public <T> T update(T entity) throws DaoException {
	try {
	    return this.getEntityManager().merge(entity);
	} catch (RuntimeException e) {
	    throw new DaoException("Could not update " + entity, e);
	}
    }

}
