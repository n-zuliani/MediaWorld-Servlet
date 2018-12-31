import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet{

    private static final long serialVersionUID = 3L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
	    PrintWriter out = response.getWriter(); 
        
            HttpSession session=request.getSession(false); 
 
            if(session!=null && request.getParameter("action")==null || session!=null && !request.getParameter("action").equals("logout")){
              response.sendRedirect("home");
              return; 
            }
            request.getRequestDispatcher("index.html").include(request, response);  		 
		 
		 if(request.getParameter("action").equals("logout")){
			  

			if(session!=null){
				HomeServlet.db.saveLogout((int)session.getAttribute("sqlid"));
				session.invalidate();
			}
			out.println("<p align=\"center\" style=\"background-color:#9BF994;padding:10px;border:2px solid #5DCE69;\">Hai effettuato il logout!</p>");
			out.close();   
			return;
		 }
    }

}
