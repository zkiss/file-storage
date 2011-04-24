package hu.bme.vihijv37.bus1fj.web.server;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JpaManager {

    private static final String PERSISTENCE_UNIT_NAME = "FILESERVICE";

    private static final JpaManager INSTANCE = new JpaManager();

    public static JpaManager getInstance() {
	return JpaManager.INSTANCE;
    }

    private final EntityManagerFactory entityManagerFactory;

    private JpaManager() {
	this.entityManagerFactory = Persistence.createEntityManagerFactory(JpaManager.PERSISTENCE_UNIT_NAME);
    }

    @Override
    protected void finalize() throws Throwable {
	super.finalize();
	this.entityManagerFactory.close();
    }

    public EntityManagerFactory getEntityManagerFactory() {
	return this.entityManagerFactory;
    }

}
