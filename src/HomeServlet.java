import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import javax.servlet.http.HttpSession;

public class HomeServlet extends HttpServlet{
    
    public static DataBase db;
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          response.setContentType("text/html");
	  PrintWriter out = response.getWriter();
 
          HttpSession session=request.getSession(false);  
          if(session==null){ 
           response.sendRedirect("login");  
           return;
          }
		 
          if(db.login((String)session.getAttribute("uname"), (String)session.getAttribute("psw"))){
                printHome(out);
          }else{
                out.println("<h1 align=\"center\">MediaWorld</h1>");
                out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">Impossibile recuperare i dati dai Cookies</p>");  
                out.println("<h3 align=\"center\"><a href=\"index.html\">Back to Login</a></h3>");  
          } 
	
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();          
            db = new DataBase("213.140.22.237","zuliani.nicolo","zuliani.nicolo","xxx123#");
            
             if(!db.openConnection()){
			  out.println("<h2>Internal Server Error - SQL Error</h2>");
			  return;
			 }
             if(request.getParameter("login")==null){
              out.println("<h1 align=\"center\">MediaWorld</h1>");
              if(db.existsUser(request.getParameter("username"))){          
               out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">il tuo username esiste gia'</p>");
              }else if(db.existsEmail(request.getParameter("email"))){          
               out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">la tua mail esiste gia'</p>");
              }else{
               try{
                db.saveUser(request.getParameter("nome"),request.getParameter("cognome"),request.getParameter("username"),request.getParameter("email"),request.getParameter("password"));
                out.println("<p align=\"center\" style=\"background-color:#9BF994;padding:10px;border:2px solid #5DCE69;\">Registrazione avvenuta con successo!</p>");
               }catch(SQLException e){
                out.println("<p>"+e.getMessage()+"</p>");
               } 
              }    
              out.println("<h3 align=\"center\"><a href=\"index.html\">Go to Login</a></h3>");  
             }else{
              if(db.login(request.getParameter("LoginUser"), request.getParameter("LoginPassword"))){
			      
				  HttpSession session=request.getSession();  
                  session.setAttribute("uname", request.getParameter("LoginUser")); 
				  session.setAttribute("psw", request.getParameter("LoginPassword")); 
				  
				  session.setAttribute("sqlid", db.saveLogin(request.getParameter("LoginUser")));
				  
                  printHome(out);
              }else{
                out.println("<h1 align=\"center\">MediaWorld</h1>");
                out.println("<p align=\"center\" style=\"background-color:#F99494;padding:10px;border:2px solid #FE2E2E;\">username o password non corretti</p>");  
                out.println("<h3 align=\"center\"><a href=\"index.html\">Back to Login</a></h3>");  
              } 
             }            
             out.close();
            
            
    }
	
	private void printHome(PrintWriter out){
	   out.println(HTMLUtils.getHead());
       out.println("<h1 align=\"center\">MediaWorld</h1>");
       out.println(HTMLUtils.getHeader());
       out.println(HTMLUtils.getChangePasswordBody());
       out.println(HTMLUtils.getDeleteBody());
       out.println("<br>");
       out.println(db.getProducts()); 
	}

}
