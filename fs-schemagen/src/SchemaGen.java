import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import hu.bme.vihijv37.bus1fj.web.server.entity.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SchemaGen {

    private static final Log LOG = LogFactory.getLog(SchemaGen.class);
    private static final String PERSISTENCE_UNIT_NAME = "FILESERVICE";
    private static final String DEFAULT_USER_PASSWORD = "asd";
    private static final String DEFAULT_USER_NAME = "Zoli";
    private static final String DEFAULT_USER_MAIL = "zoltan.kiss.jr@gmail.com";

    public static void main(String[] args) {
	try {
	    System.out.println("Initializing persistence...");
	    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(SchemaGen.PERSISTENCE_UNIT_NAME);
	    System.out.println("Inserting default data...");
	    new SchemaGen(entityManagerFactory).insertDefaultData();
	} catch (Exception e) {
	    System.out.println("An error has occured during the schema generation process: " + e.getMessage());
	    SchemaGen.LOG.error(e.getMessage(), e);
	}
    }

    private final EntityManagerFactory entityManagerFactory;

    public SchemaGen(EntityManagerFactory entityManagerFactory) {
	this.entityManagerFactory = entityManagerFactory;
    }

    private void insertDefaultData() {
	EntityManager em = this.entityManagerFactory.createEntityManager();
	try {
	    User adminUser = new User();
	    adminUser.setEmail(SchemaGen.DEFAULT_USER_MAIL);
	    adminUser.setName(SchemaGen.DEFAULT_USER_NAME);
	    adminUser.setPassword(SchemaGen.DEFAULT_USER_PASSWORD);

	    em.getTransaction().begin();
	    em.merge(adminUser);
	    em.getTransaction().commit();
	    System.out.println("A user was successfully created with the following credentials:\n" //
		    + "Loginname (email): " + adminUser.getEmail() + "\n" //
		    + "Username: " + adminUser.getName() + "\n" //
		    + "Password: " + adminUser.getPassword());
	} finally {
	    if (em.getTransaction().isActive()) {
		em.getTransaction().rollback();
	    }
	    em.close();
	}
    }

}
