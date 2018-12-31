
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.sql.Time;

public class DataBase {

	private String url;
	private Connection connection;

	public DataBase(String address, String dbname, String dbuser, String password) {
		this.url = "jdbc:sqlserver://" + address + ";databaseName=" + dbname + ";user=" + dbuser + ";password="
				+ password;
		connection = null;
	}

	public boolean openConnection(){
          try{
		   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           this.connection = DriverManager.getConnection(this.url);
		   return true;
		  }catch(Exception e){
		   e.printStackTrace();
		  }
		  return false;
	}

	public void closeConnection() {
		if (this.connection == null)
			return;
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.connection = null;
	}
	
	private int getLastId(String table) throws SQLException{
	    String sql = "select MAX(id) as max from "+table+";";
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql);
              rs.next();
	      return rs.getInt("max");
	}

        public boolean existsUser(String user){
	    String sql = "select top 1 username from mw_users where username = '"+user+"';";
	   try{
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return rs.next();
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return false;
	}

        public boolean existsEmail(String email){
	    String sql = "select top 1 mail from mw_users where mail = '"+email+"';";
	   try{
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return rs.next();
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return false;
	}
	
        public boolean login(String username, String password){
	   String sql = "select top 1 mail from mw_users where username = '"+username+"' AND psw = '"+password+"';";
	   try{
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return rs.next();
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return false;
	}

	public void saveUser(String name, String surname, String username, String email, String password) throws SQLException{

		String sql = "INSERT INTO mw_users(id,nome,cognome,mail,psw,username) VALUES(?,?,?,?,?,?)";
		    PreparedStatement st = this.connection.prepareStatement(sql); 
		    st.setInt(1, getLastId("mw_users") + 1);
			st.setString(2, name);
			st.setString(3, surname);
			st.setString(4, email);
			st.setString(5, password);
			st.setString(6, username);
			st.executeUpdate();
	}

        public String getProducts(){
	   String sql = "select * from mw_prodotti;";
	   try{
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return HTMLUtils.generateProductTable(rs);
	   }catch(SQLException e){
	       e.printStackTrace();
	   }  
          return null;
	}
	
	public String getProducts(String name) throws SQLException{
	   String sql = "select mw_cart.quantity, mw_prodotti.nome, mw_prodotti.prezzo from mw_prodotti inner join mw_cart on mw_prodotti.id = mw_cart.idProd and mw_cart.idUser = (select id from mw_users where username = '"+name+"');";
	  
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return HTMLUtils.generateCartTable(rs);
	}
        
       public boolean updatedPass(String newPass, String password, String username){
	   if(!login(username,password))
	    return false;
		
	   String sql = "UPDATE mw_users SET psw = ? WHERE username = ?;";
	   try{
		    PreparedStatement st = this.connection.prepareStatement(sql); 
		    st.setString(1, newPass);
			st.setString(2, username);
			st.executeUpdate();		         
	      return true;
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return false;
	}

      public boolean deleteUser(String password, String username){
	   if(!login(username,password))
	    return false;
	   try{
	   String sql = "DELETE FROM mw_users WHERE username = ?;";
	   
		    PreparedStatement st = this.connection.prepareStatement(sql); 
			st.setString(1, username);
			st.executeUpdate();		         
	      return true;
	   }catch(SQLException e){
	     e.printStackTrace();
           }  
	   return false;
	}
	
	public boolean hasCart(String username){
	   String sql = "select idUser from mw_cart where idUser = (select id from mw_users where username = '"+username+"');";
	   try{
	      Statement st = this.connection.createStatement(); 
	      ResultSet rs = st.executeQuery(sql); 
              
	      return rs.next();
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return false;
	}
	
	public void addCart(String username, int id){
	   String query = "select quantity from mw_cart where idUser = (select id from mw_users where username = '"+username+"') and idProd = "+id+";";
	   String sql = "INSERT INTO mw_cart (idUser,IdProd,quantity) VALUES((select id from mw_users where username = ?),?,?);";
	  try{
		Statement st = this.connection.createStatement(); 
	        ResultSet rs = st.executeQuery(query); 
                if(!rs.next()){
		    PreparedStatement pst = this.connection.prepareStatement(sql); 
		    pst.setString(1, username);
	            pst.setInt(2, id);
                    pst.setInt(3,0);
		    pst.executeUpdate();              
                 }

                    updateStack(id, username);	
                   }catch(SQLException e){
                       e.printStackTrace();
              }	         
	  		
	}
	
	 private void updateStack(int id, String user){
		
	   String sql = "UPDATE mw_prodotti SET quantity = (select quantity from mw_prodotti where id = ?) - 1 WHERE id = ?;";
           String sql2 = "UPDATE mw_cart SET quantity = (select quantity from mw_cart where idProd = ? and idUser = (select id from mw_users where username = ?)) + 1 WHERE idUser = (select id from mw_users where username = ?) AND idProd = ?;";
	   try{
		    PreparedStatement st = this.connection.prepareStatement(sql);
                    PreparedStatement st2 = this.connection.prepareStatement(sql2); 
		    st.setInt(1, id);
			st.setInt(2, id);
			st.executeUpdate();	
                   
            st2.setInt(1, id);
			st2.setString(2, user);
            st2.setString(3, user);
            st2.setInt(4, id);
			st2.executeUpdate();
                   
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	}
	 
    @SuppressWarnings("deprecation")
    public int saveLogin(String user){
		
	   String sql = "INSERT INTO mw_log(id,idUser,logintime,logindate) VALUES(?,(select id from mw_users where username = ?),?,?);";
 
	   try{
		    PreparedStatement st = this.connection.prepareStatement(sql);
			int id = getLastId("mw_log")+1;
			st.setInt(1, id);
		    st.setString(2, user);
			st.setTime(3, new Time(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond()));
			st.setDate(4, new Date(System.currentTimeMillis()));
			st.executeUpdate();	 
            return id;		
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	   return -1;
	}
	
        @SuppressWarnings("deprecation")
	public void saveLogout(int id){
		
	   String sql = "UPDATE mw_log SET logouttime = ?, logoutdate = ? WHERE id = ?";
 
	   try{
		    PreparedStatement st = this.connection.prepareStatement(sql);	
			st.setTime(1, new Time(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond()));
			st.setDate(2, new Date(System.currentTimeMillis()));
			st.setInt(3, id);
			st.executeUpdate();	 		
	   }catch(SQLException e){
	       e.printStackTrace();
	   } 
	}
}
