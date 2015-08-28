

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DemoCustomer;
import model.DemoOrder;
import customTools.DBUtil;

/**
 * Servlet implementation class CustomerServlet
 */
@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("<html>\r<head>\r<title>JPA Test</title>\r</head>\r<body>\r");
		//print one customer
		try{
			model.DemoCustomer cust = em.find(model.DemoCustomer.class, (long)2);
			
			out.print("<h1>Get one customer</h1>");
			out.printf("<h3>%s %s</h3>\r",cust.getCustFirstName(),cust.getCustLastName());
			
			
		//print list of customers
			String qString = "SELECT c from DemoCustomer c";
		       TypedQuery<DemoCustomer> q = em.createQuery(qString, DemoCustomer.class);

		       List<DemoCustomer> custs;
		       
	           custs = q.getResultList();
	           if (custs == null || custs.isEmpty())
	           {
	        	   out.printf("<h3>customer list is null</h3>\r");
	        	   custs = null;
	           }
	           out.print("<h1>Get all customers</h1>\r");
	           out.printf("<ul>\r");
	           for (DemoCustomer c:custs){
	        	   out.printf("<li>%s %s</li>\r", c.getCustFirstName(),c.getCustLastName());
	           }
	           out.printf("</ul>\r");
	           
	           //see if customer exists
	           TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM DemoCustomer c WHERE c.customerId = 2L", Long.class);
	            long total = query.getSingleResult();
	            if (total>0)
	            {
	            	out.printf("%s", "Customer exists");
	            }else{
	            	out.printf("%s", "Customer does not exist");
	            }
	            
	          //find with a parameter
	          
	           
	           //add a new customer
	            //create a new customer class
	            DemoCustomer customer = new DemoCustomer();
	            customer.setCustFirstName("dave");
	            customer.setCustLastName("Wolf");
	           EntityTransaction trans = em.getTransaction();
	           trans.begin();
	           try{
	        	   em.persist(customer);
	        	   trans.commit();
	           }catch (Exception e){
	        	   trans.rollback();
	           }finally{
	        	   em.close();
	           }
	           
	          
	           //use a named parameter
	           
	           //update customer 
	           
	           //delete customer
	           
	           //create a new table
	           
	           //select from multiple tables
	            qString = "SELECT o from DemoOrder o";
			       TypedQuery<DemoOrder> corders = em.createQuery(qString, DemoOrder.class);

			       List<DemoOrder> cust_orders;
			       
			       cust_orders = corders.getResultList();
		           if (cust_orders == null || cust_orders.isEmpty())
		           {
		        	   out.printf("<h3>customer list is null</h3>\r");
		        	   cust_orders = null;
		           }
		           out.print("<h1>Get all orders</h1>\r");
		           out.printf("<ul>\r");
		           for (DemoOrder o:cust_orders){
		        	   out.printf("<li>%s %s placed an order on %s</li>\r", o.getDemoCustomer().getCustFirstName(),o.getDemoCustomer().getCustLastName(),o.getOrderTimestamp());
		           }
		           out.printf("</ul>\r");
	           
	           
	           
			
		}catch(Exception e){
			System.out.println(e);
		}finally{
			out.print("</body>\r</html>");
			out.close();
			em.close();
			
		}
			
	
	}

}
