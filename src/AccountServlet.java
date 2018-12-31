import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;

public class AccountServlet extends HttpServlet{

    private static final long serialVersionUID = 2L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.sendRedirect("login");  
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter(); 
        
            HttpSession session=request.getSession(false);  
         if(session==null){ 
           response.sendRedirect("login");  
           return;
          }
		String user = (String) session.getAttribute("uname"); 
		
		out.println("<h1 align=\"center\">MediaWorld</h1>");
	    if(request.getParameter("change")!=null){
		  
		  if(HomeServlet.db.updatedPass(request.getParameter("newPassword"),request.getParameter("oldPassword"),user)) {
		    out.println("<p align=\"center\" style=\"background-color:#9BF994;padding:10px;border:2px solid #5DCE69;\">Password aggiornata correttamente!</p>");
			session.setAttribute("psw", request.getParameter("newPassword")); 
		  }else{
	        out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">la tua password attuale non corrisponde</p>");
		  }
		  out.println("<h3 align=\"center\"><a href=\"home\">Back to HomePage</a></h3>");
	    }else{
		  if(HomeServlet.db.deleteUser(request.getParameter("password"),user)) {
		    out.println("<p align=\"center\" style=\"background-color:#D11C1C;padding:10px;border:2px solid #5DCE69;\">Il tuo account non esiste piu'!</p>");
                    out.println("<h3 align=\"center\"><a href=\"index.html\">Go to Login</a></h3>");	
                    session.invalidate(); 
		  }else{
	           out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">la tua password non corrisponde</p>");
		   out.println("<h3 align=\"center\"><a href=\"home\">Back to HomePage</a></h3>");
		  }
		}	           
        out.close(); 	
    }

}
