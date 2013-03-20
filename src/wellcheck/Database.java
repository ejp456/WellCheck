/*
 * Add comment
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.jdbc.PreparedStatement;


public class Database {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String url;
    private String user;
    private String password;
    private PreparedStatement statement;
    int temp;
	
    public Database(){
		con = null;
        st = null;
        rs = null;
        statement = null;
        
        url = "jdbc:mysql://localhost:3306/clinic";
        user = "root";
        password = "password";
	}
	public void Connect(){
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);        
        }
	}
public boolean userExist(String string1){
	try {
		statement = (PreparedStatement) con.prepareStatement("SELECT user FROM testdb WHERE user =\""+string1+"\"");
		rs = statement.executeQuery();
		while(rs.next()){
			if(rs.getString(1).equals(string1)){
				return true;
			}
		}
	} catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
	}
	
	return false;
	}
public boolean checkPassword(String user,String password){
    try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select password FROM testdb WHERE user = \""+user+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
    if(rs.getString(1).equals(password)){
        return true;
        }
    }
        return false;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return false;
	}
}
public void closeConnection(){
    try {
        if (rs != null) {
            rs.close();
        }
        if (st != null) {
            st.close();
        }
        if (con != null) {
            con.close();
        }

		    } catch (SQLException ex) {
		        Logger lgr = Logger.getLogger(Database.class.getName());
		        lgr.log(Level.WARNING, ex.getMessage(), ex);
		    }
	}
}
