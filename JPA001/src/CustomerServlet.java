

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
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
	            	out.printf("%s", "<p>Customer exists</p>");
	            }else{
	            	out.printf("%s", "<p>Customer does not exist</p>");
	            }
	            
	            //get the next customerId
	            Long nextCustomerId = em.createQuery("SELECT MAX(c.customerId) FROM DemoCustomer c", Long.class).getSingleResult();
	            nextCustomerId += 1L;
	            out.printf("<p>The next customer id will be %s</p>", nextCustomerId);
	            
	          //find with a parameter
	          
	           
	           //add a new customer
	           //create a new customer class
	            DemoCustomer customer = new DemoCustomer();
	            customer.setCustomerId(nextCustomerId);
	            customer.setCustFirstName("Bart");
	            customer.setCustLastName("Simpson");
	           EntityTransaction trans = em.getTransaction();
	           trans.begin();
	           try{
	        	   em.persist(customer);
	        	   trans.commit();
	           }catch (Exception e){
	        	   trans.rollback();
	           }finally{
	        	   //em.close();
	           }
	           
	          
	           //use a named parameter
	           
	           //update customer 
	           
	           //delete customer
	           
	           //create a new table
	           
	           //select from multiple tables
	            qString = "SELECT o from DemoOrder o where o.demoCustomer.customerId in (1,3,5)";
			       TypedQuery<DemoOrder> corders = em.createQuery(qString, DemoOrder.class);

			       List<DemoOrder> cust_orders;
			       
			       cust_orders = corders.getResultList();
		           if (cust_orders == null || cust_orders.isEmpty())
		           {
		        	   out.printf("<h3>customer list is null</h3>\r");
		        	   cust_orders = null;
		           }
		           
		           
		           // SimpleDateFormat can be used to control the date/time display format:
		           //   E (day of week): 3E or fewer (in text xxx), >3E (in full text)
		           //   M (month): M (in number), MM (in number with leading zero)
		           //              3M: (in text xxx), >3M: (in full text full)
		           //   h (hour): h, hh (with leading zero)
		           //   m (minute)
		           //   s (second)
		           //   a (AM/PM)
		           //   H (hour in 0 to 23)
		           //   z (time zone)
		           SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
		           
		           out.print("<h1>Get all orders</h1>\r");
		           out.printf("<ul>\r");
		           for (DemoOrder o:cust_orders){
		        	   out.printf("<li>%s %s placed an order for $%,5.2f on %s</li>\r", 
		        			   o.getDemoCustomer().getCustFirstName(),
		        			   o.getDemoCustomer().getCustLastName(),
		        			   o.getOrderTotal(),
		        			   dateFormatter.format(o.getOrderTimestamp()));
		           }
		           out.printf("</ul>\r");
	           
		        //select aggregate functions   
		           String sql = "SELECT o.orderId, sum(o.orderTotal) from DemoOrder o group by o.orderId";
		           Query qry = em.createQuery(sql);
		           List<Object[]> results = qry.getResultList();//returns a list of Object[]
		           //http://stackoverflow.com/questions/13700565/jpa-query-getresultlist-use-in-a-generic-way
		           out.printf("<h3>Aggregate Functions</h3>\r");
		           out.print("<table border='1'>\r");
		           out.print("<tr><th>OrderId</th><th>Order Total</th></tr>");
		           for(Object[] result : results){
		        	   //results[x].object[0]
		        	  
		        			Long orderID = (Long)result[0];
		        			java.math.BigDecimal orderTotal = (java.math.BigDecimal) result[1];
		        			out.printf("<tr><td>%s</td><td>%s</td></tr>\r",orderID, orderTotal);
		        		}
		           out.print("</table>\r");
		        			
		}catch(Exception e){
			System.out.println(e);
		}finally{
			out.print("</body>\r</html>");
			out.close();
			em.close();
			
		}
		
	
	}

}
