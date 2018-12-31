import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;

public class CartServlet extends HttpServlet{

    private static final long serialVersionUID = 4L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
	    PrintWriter out = response.getWriter(); 
        
            HttpSession session=request.getSession(false); 

            if(session==null){
              response.sendRedirect("login");
              return; 
            }
            
            if(request.getParameter("product")!=null){	
		       
				HomeServlet.db.addCart((String)session.getAttribute("uname"),Integer.parseInt(request.getParameter("product")));                          			
			}	
            HTMLUtils.getHead();
			out.println("<h1 align=\"center\">MediaWorld</h1>");
			out.println(HTMLUtils.getCartHeader());
            if(!HomeServlet.db.hasCart((String)session.getAttribute("uname"))) {
				out.println("<h3 align=\"center\">Non hai nessun prodotto nel carrello</h3>");
				return;
			}
try{
            out.println(HomeServlet.db.getProducts((String)session.getAttribute("uname")));
 }catch(SQLException e){
	       out.println(e.getMessage());
	   }
        out.close();
    }

}
