import java.sql.ResultSet;
import java.sql.SQLException;

public class HTMLUtils {
    
    public static String getHeader(){
        
        return "<p align=\"center\"><a href=\"javascript:void(0)\" onClick=\"toggle('change','delete')\">Cambia password</a> | <a href=\"javascript:void(0)\" onClick=\"toggle('delete','change')\">Elimina Account</a> | <a href=\"login?action=logout\">Logout</a></p> <form action=\"cart\" method=\"get\" align=\"right\"> <input style=\"padding:30px\" type=\"submit\" name=\"cart\" value=\"Carrello\"/></form>";
        
    }
	
	public static String getCartHeader(){
        
        return "<p align=\"center\"><a href=\"home\" \">Home</a> | <a href=\"javascript:void(0)\" onClick=\"toggle('change','delete')\">Cambia password</a> | <a href=\"javascript:void(0)\" onClick=\"toggle('delete','change')\">Elimina Account</a> | <a href=\"login?action=logout\">Logout</a></p> <form action=\"cart\" method=\"get\" align=\"right\"> <input style=\"padding:30px\" type=\"submit\" name=\"cart\" value=\"Carrello\"/></form>";
        
    }
    
    public static String getHead(){
        return "<style> th, td, tr, table{  border: 1px solid black; border-collapse: collapse;} #change{display: none;} #delete{display: none;}</style><script>function toggle(div1,div2){document.getElementById(div1).style.display='block';document.getElementById(div2).style.display='none';} function hide(id){document.getElementById(id).style.display='none';}</script>";
    }
    
    public static String generateProductTable(ResultSet rs) throws SQLException{
        
        String head = "<table align=\"center\"><tr><th>Nome</th><th>Categoria</th><th>Stack</th><th>Prezzo</th><th>Carrello</th><tr>";
        while(rs.next()){
			if(rs.getInt("quantity")>0)
              head+="<tr><td>"+rs.getString("nome")+"</td><td>"+rs.getString("categoria")+"</td><td>"+rs.getInt("quantity")+"</td><td>$"+rs.getDouble("prezzo")+"</td><td><a href=\"cart?product="+rs.getString("id")+"\">Agg. Carrello</a></td></tr>";
            else
		      head+="<tr><td>"+rs.getString("nome")+"</td><td>"+rs.getString("categoria")+"</td><td>Stack out</td><td>$"+rs.getDouble("prezzo")+"</td><td>Agg. Carrello</td></tr>";
		}
        return head+"</table>";
        
    }
	
	public static String generateCartTable(ResultSet rs) throws SQLException{
        
        String head = "<table align=\"center\"><tr><th>Nome</th><th>Ordinati</th><th>Prezzo Unitario</th><tr>";
        while(rs.next()){	
          head+="<tr><td>"+rs.getString("nome")+"</td><td>x"+rs.getString("quantity")+"</td><td>$"+rs.getDouble("prezzo")+"</td></tr>";
        }
        return head+"</table>";
        
    }
    
    public static String getChangePasswordBody(){
        return "<div align=\"center\" id=\"change\"><form action=\"account\" method=\"post\"><input type=\"password\" name=\"oldPassword\" placeholder=\"Password Attutale\"/><input type=\"password\" name=\"newPassword\" placeholder=\"Nuova Password\"/><input type=\"submit\" value=\"Cambia\" name=\"change\"/></form><a href=\"javascript:void(0)\" onClick=\"hide('change')\">Close</a></div>";
    }
    
     public static String getDeleteBody(){
        return "<div align=\"center\" id=\"delete\"><form action=\"account\" method=\"post\"><input type=\"password\" name=\"password\" placeholder=\"Password\"/><input name=\"delete\" type=\"submit\" value=\"Conferma eliminazione account\"/></form><a href=\"javascript:void(0)\" onClick=\"hide('delete')\">Close</a></div>";
    }
    
}
