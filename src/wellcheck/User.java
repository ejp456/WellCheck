package wellcheck;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

/**
 *
 * @author Kent
 */
public class User {
    private int userid;
    private String username;
    private String password;
    private String usertype;
    private String firstname;
    private String lastname;
    private String dateofbirth;
    private String secretquestion;
    private String secretanswer;
    
    public User() {
        userid = 0;
        username = null;
        password = null;
        usertype = null;
        firstname = null;
        lastname = null;
        dateofbirth = null;
        secretquestion = null;
        secretanswer = null;
    }

    public User(int userid, String username, String password, 
            String usertype, String firstname, String lastname, 
            String dateofbirth, String secretquestion, String secretanswer) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.usertype = usertype;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateofbirth = dateofbirth;
        this.secretquestion = secretquestion;
        this.secretanswer = secretanswer;
    }
    
    public User(User u)
    {
        userid = u.userid;
        username = u.username;
        password = u.password;
        usertype = u.usertype;
        firstname = u.firstname;
        lastname = u.lastname;
        dateofbirth = u.dateofbirth;
        secretquestion = u.secretquestion;
        secretanswer = u.secretanswer;
    }
    
    //This method instantiates an object of the User class by querying the
    //database
    static User getRow(int userid) {
        Database db = new Database();
        db.Connect();
        User returnuser = new User();
        
        
        String querystring = "Select * FROM users WHERE userid = " + userid;
        List<List> plist = db.dbQuery(querystring);
        Iterator pit = ((List) plist.get(0)).iterator();
        
        returnuser.setUserid((int) (Integer) pit.next());
        returnuser.setUsername((String) pit.next());
        returnuser.setPassword((String) pit.next());
        returnuser.setUsertype((String) pit.next());
        returnuser.setFirstname((String) pit.next());
        returnuser.setLastname((String) pit.next());
        returnuser.setDateofbirth(((Date) pit.next()).toString());
        returnuser.setSecretquestion((String) pit.next());
        returnuser.setSecretanswer((String) pit.next());
        
        db.closeConnection();
        return returnuser;
    }
    
    //This method inserts a user object into the database
    //To successfully insert, be sure to set userid = 0
    static void insertRow(User insertuser) {
        Database db = new Database();
        db.Connect();
        
        //Checks to see if the object is already in the database
        //Naively assumes that if userid =/= 0 the object is already inside
        if(insertuser.getUserid() != 0) {
            System.out.println("User already in database");
            return;
        }
                    
        String qstring = "Select userid FROM users";
        List<List> plist = db.dbQuery(qstring);
        
        //This loop finds the location to insert the object into the database
        int j = 1;
        for(int i = 0; i < plist.size() && j != (Integer) plist.get(i).get(0); i++)
        {
            j++;
        }
        
        insertuser.setUserid(j);
        
        qstring = "Insert INTO users (userid, username, password, usertype, "
                + "FirstName, LastName, DOB, secretquestion, "
                + "secretanswer) "
                + "VALUES ("
                + "" + insertuser.getUserid() + ", "
                + "'" + insertuser.getUsername() + "', "
                + "'" + insertuser.getPassword() + "', "
                + "'" + insertuser.getUsertype() + "', "
                + "'" + insertuser.getFirstname() + "', "
                + "'" + insertuser.getLastname() + "', "
                + "'" + insertuser.getDateofbirth() + "', "
                + "'" + insertuser.getSecretquestion() + "', "
                + "'" + insertuser.getSecretanswer() + "')";
        
        db.updateDB(qstring);
        db.closeConnection();
    }
    
    //This method deletes a user from the database by userid
    static void deleteRow(User deleteuser)
    {
        Database db = new Database();
        db.Connect();
        String qstring = "DELETE FROM users WHERE userid = " + deleteuser.getUserid();
        db.updateDB(qstring);        
        db.closeConnection();
    }
    
    //This method modifies an existing database entry
    static void updateRow(User updateuser) {
        Database db = new Database();
        db.Connect();
        
        String qstring = "UPDATE users SET "
                + "username = '" + updateuser.getUsername() + "', "
                + "password = '" + updateuser.getPassword() + "', "
                + "usertype = '" + updateuser.getUsertype() + "', "
                + "FirstName = '" + updateuser.getFirstname() + "', "
                + "LastName = '" + updateuser.getLastname() + "', "
                + "DOB = '" + updateuser.getDateofbirth() + "', "
                + "SecretQuestion = '" + updateuser.getSecretquestion() + "', "
                + "SecretAnswer = '" + updateuser.getSecretanswer() + "' "
                + "WHERE userid = " + updateuser.getUserid();
        
        db.updateDB(qstring);
        db.closeConnection();
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public void setSecretquestion(String secretquestion) {
        this.secretquestion = secretquestion;
    }

    public void setSecretanswer(String secretanswer) {
        this.secretanswer = secretanswer;
    }
    
    @Override
    public String toString()
    {
        String result = getUserid()
                + " " + getUsername()
                + " " + getPassword()
                + " " + getUsertype()
                + " " + getFirstname()
                + " " + getLastname()
                + " " + getDateofbirth()
                + " " + getSecretquestion()
                + " " + getSecretanswer();
        return result;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public String getSecretquestion() {
        return secretquestion;
    }

    public String getSecretanswer() {
        return secretanswer;
    }
}
