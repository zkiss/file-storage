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
	    User user = new User();
	    user.setEmail(SchemaGen.DEFAULT_USER_MAIL);
	    user.setName(SchemaGen.DEFAULT_USER_NAME);
	    user.setPassword(SchemaGen.DEFAULT_USER_PASSWORD);

	    em.getTransaction().begin();
	    em.persist(user);
	    em.getTransaction().commit();
	    System.out.println("A user was successfully created with the following credentials:\n" //
		    + "Loginname (email): " + user.getEmail() + "\n" //
		    + "Username: " + user.getName() + "\n" //
		    + "Password: " + user.getPassword());
	} finally {
	    if (em.getTransaction().isActive()) {
		em.getTransaction().rollback();
	    }
	    em.close();
	}
    }

}
