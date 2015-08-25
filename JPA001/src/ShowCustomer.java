import javax.persistence.EntityManager;
import customTools.DBUtil;

public class ShowCustomer {

	public static void main(String[] args) {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		try{
			model.DemoCustomer cust = em.find(model.DemoCustomer.class, (long)2);
			System.out.println(cust.getCustFirstName());
		}catch(Exception e){
			System.out.println(e);
		}finally{
			em.close();
			System.out.println("Finish!");
		}
	


	}

}
