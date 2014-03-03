/*
 * Add comment
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.sql.PreparedStatement; Everything seems to work using this statement
import com.mysql.jdbc.PreparedStatement;
import java.util.*;


public class Database {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private ResultSetMetaData rsmd;
    private String url;
    private String user;
    private String password;
    private PreparedStatement statement;
    int temp;
	
    public Database(){
	con = null;
        st = null;
        rs = null;
        rsmd = null;
        statement = null;
        
        url = ;
        user = ;
        password = ;
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
		statement = (PreparedStatement) con.prepareStatement("SELECT username FROM users WHERE username =\""+string1+"\"");
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
    statement = (PreparedStatement) con.prepareStatement("Select password FROM users WHERE username = \""+user+"\"");
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
public boolean patientTable(){
        try {
    ArrayList<String> patient = new ArrayList();
    ArrayList<Integer> doctor = new ArrayList();
    statement = (PreparedStatement) con.prepareStatement("SELECT FirstName, LastName, Patient.Doctor FROM users JOIN Patient ON (users.userid = Patient.userid)");
    rs = statement.executeQuery();
    while(rs.next()){
        patient.add(rs.getString(1)+" "+rs.getString(2));
        doctor.add(Integer.parseInt(rs.getString(3)));
        
    }
    int j = 0;
    for(int i=0;i<doctor.size();i++){
    statement = (PreparedStatement) con.prepareStatement("SELECT FirstName, LastName FROM users JOIN Patient ON (users.userid = Patient.Doctor) WHERE Patient.Doctor = \""+doctor.get(i)+"\"");
    rs = statement.executeQuery();
    if(rs.next()){
        DoctorWindowController.addPatient(patient.get(i), rs.getString(1)+" "+rs.getString(2));
        }
    }
        return false;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return false;
	}
}
public String getSecretQuestion(String fName, String lName){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select SecretQuestion FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}
public String getSecretAnswer(String fName, String lName){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select SecretAnswer FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}
public String getPassword(String fName, String lName){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select Password FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}
public String getUserName(String fName, String lName){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select username FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
        System.out.println(rs.getString(1));
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}
public void initializePatient(String firstName, String lastName, String dateOfBirth, String address1,
        String address2, String city, String state, String zip, String phoneNumber,
        String insuranceProvider, String memberId, String groupNumber, String assignedDoctor, String userId) {
    String empty = "empty";
    try {
    statement = (PreparedStatement) con.prepareStatement(
            "INSERT INTO `1_0362c2e_3`.`Patient` (`userid`, `Address1`, `Address2`, `City`, `State`, "
            + "`ZipCode`, `PhoneNumber`, `InsuranceProvider`, `memberid`, `groupid`, `DependantTo`, `Doctor`) "
            + "VALUES ('" + userId + "', '" + address1 + "', '" + address2 + "', '" + city + "', '" + state + "', '" + zip + "', '" + phoneNumber 
            + "', '" + insuranceProvider + "', '" + memberId + "', '" + groupNumber + "', '0', '" + assignedDoctor + "');");
    statement.execute();
    statement = (PreparedStatement) con.prepareStatement("INSERT INTO `1_0362c2e_3`.`users`(`userid` ,`username` ,`password` ,`usertype` ,"
+"`FirstName` ,`LastName` ,`DOB` ,`SecretQuestion` ,`SecretAnswer`)"
+"VALUES ('" + userId + "', '" + empty + "', '" + empty + "', " + " 'Patient', " + " '" + firstName + "', '" + lastName + "', '" + dateOfBirth 
            + "', '" + empty + "', '" + empty + "');");
    
    statement.execute();

    
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
	}
}
public String getDoctorFirst(String id){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select FirstName FROM users WHERE userid = \""+id+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
        System.out.println(rs.getString(1));
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}
public String getDoctorLast(String id){
     try {
    int count = 0;
    statement = (PreparedStatement) con.prepareStatement("Select LastName FROM users WHERE userid = \""+id+"\"");
    rs = statement.executeQuery();
    while(rs.next()){
        System.out.println(rs.getString(1));
    return rs.getString(1);
    }
        return null;
    } catch (SQLException ex) {
		Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
        return null;
	}
}

/*Kent's method
 * This method queries using an arbitrary query the database and returns
 * an ArrayList of List, with the container arraylist representing rows
 * and the internal List representing columns.
 * 
 */
public ArrayList dbQuery(String qstring){
    try {
        statement = (PreparedStatement) con.prepareStatement(qstring);
        rs = statement.executeQuery();
        rsmd = rs.getMetaData();

        List<Object> objlist;
        ArrayList<List> returnlist = new ArrayList();

        while(rs.next()) {
            objlist = new ArrayList();
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                objlist.add(rs.getObject(i));
            }
            returnlist.add(objlist);
        }
        return returnlist;
}   catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Database.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
    }
}

/*Kent's Method
 * This method modifies a database entry using an arbitrary string.
 */
public void updateDB(String qstring){
    try {
        statement = (PreparedStatement) con.prepareStatement(qstring);
        statement.executeUpdate();            
    }
    catch (SQLException ex) {
        Logger lgr = Logger.getLogger(Database.class.getName());
        lgr.log(Level.SEVERE, ex.getMessage(), ex);
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
