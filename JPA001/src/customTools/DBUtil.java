package customTools;

//import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {
	public static final EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("JPA001");
	public static EntityManagerFactory getEmFactory() {
		return emf;
	}

}
