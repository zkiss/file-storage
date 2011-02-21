package hu.bme.vihijv37.bus1fj.web.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class AbstractDao {

    @SuppressWarnings("unchecked")
    public static <T> List<T> getResultList(Query query, Class<T> clazz) {
	return query.getResultList();
    }

    private final EntityManager entityManager;

    public AbstractDao(EntityManager entityManager) {
	this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
	return this.entityManager;
    }

}
