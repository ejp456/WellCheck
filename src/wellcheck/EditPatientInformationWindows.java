/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

/**
 *
 * @author Kim
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;


import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;





public class EditPatientInformationWindows implements Initializable {
    @FXML private Button set;
    @FXML private Button saveInformation;
    @FXML private Button cancel;
    @FXML private TextField getDOB;
    @FXML private TextField getaddress1;
    @FXML private TextField getaddress2;
    @FXML private TextField getcity;
    @FXML private TextField getstate;
    @FXML private TextField getzipcode;
    @FXML private TextField getphoneNumber;
    @FXML private TextField getInsuranceProvider;
    @FXML private TextField getmemberID;
    @FXML private TextField getgroupNumber;
    @FXML private TextField getassignedDoctor;
    @FXML private ComboBox combo;
    String firstName;
    String lastName;
    String day;
    String month;
    String year;
    String address1;
    String address2;
    String city;
    String state;
    String zipcode;
    String phoneNumber;
    String InsuranceProvider;
    String memberID;
    String groupNumber;
    String assignedDoctor;
    Database db = new Database();
    
    


    
    

///////////////
    ////////////////
    ////////////////
    ////////////////
    //////////////

    
     
     
     
     protected String getInfo(String plist, int number)
     {
         int index = 0;
         
         for(int i = 0; i < number;i++)
         {
             while(plist.charAt(index) != ',')
             {
                 index++;
             }
             index = index + 2;
             
         }
         
         int index1 = index;
         while(plist.charAt(index1) != ',')
         {
                 index1++;
         }
         String temp = plist.substring(index, index1);
         return temp;
         

         
     }
     
     protected String getInfoDOB(ArrayList plist)
     {
         String temp1 = plist.toString();
         
         
         int index = 0;
         while(temp1.charAt(index) != ']')
         {
                 index++;
         }
         
         
         
         String temp = temp1.substring(2, index);
         return temp;
         
     }//ArrayList + int
     
     //int kk = match2(dbDoctor);
     
     
     
     
     
     
     
     
     protected int match(String fName, String lName, ArrayList dbfName, ArrayList dblName)
     {
         int counter = 0;
         //System.out.println("original name is   " + fName + " " + lName);
         String temp, temp1, temp2;
        for(int i = 0; i < dbfName.size(); i++){
            temp2 = dbfName.get(i).toString();
            temp = temp2.substring(1, temp2.length() - 1);//replace('[');
            //System.out.println("f   " + temp);
            temp2 = dblName.get(i).toString();
            temp1 = temp2.substring(1, temp2.length() - 1);
            //System.out.println("l   " + temp1);
            if(temp.compareTo(fName) == 0)
            {
                if(temp1.compareTo(lName) == 0)
                {
                    return i;
                }
            }
        }
        

         return counter;//error
     }
     
     

    @FXML protected void setPatient(ActionEvent e) {
        db.Connect();
        //System.out.println("works2");
        String y = (String)combo.getSelectionModel().getSelectedItem();
        //System.out.println(y);
        String[] tokens = y.split(" ");
        if(tokens.length!=2){throw new IllegalArgumentException();}
        String fName = tokens[0];
        String lName = tokens[1];
        

        List dbfName = db.dbQuery("Select FirstName FROM users WHERE usertype = 'Patient'");
        List dblName = db.dbQuery("Select LastName FROM users WHERE usertype = 'Patient'");

        
        
        int k = match(fName, lName, (ArrayList) dbfName, (ArrayList) dblName);

        List plist3 = db.dbQuery("Select userid FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
        
        String userId1 = plist3.toString();
        //System.out.println("userID1 is " + userId1);

        int index = 0;
        while(userId1.charAt(index) != ']')
        {
            index++;
        }
        
        
        
        String userId = userId1.substring(2, index);
        
        
        
        
        
        
        List plist1 = db.dbQuery("Select * FROM Patient WHERE userid = \""+userId+"\"");// WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");//");// WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
        List plist2 = db.dbQuery("Select DOB FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
        
        //Select FirstName FROM users WHERE userid = \""+id+"\"");
        
        //System.out.println("lll" + plist1);///////////////////////////
        //System.out.println(plist2);
        
        //String info = plist1.get(k).toString();
        String info = plist1.toString();
        
        //System.out.println("++++" + info);
        
        String tempDOB = getInfoDOB((ArrayList) plist2);//ArrayList + int
        String temp;
        
        getDOB.clear();
        getDOB.insertText(0, tempDOB);
        
        temp = getInfo(info, 1);
        //System.out.println(temp);
        getaddress1.clear();
        getaddress1.insertText(0, temp);
        
        temp = getInfo(info, 2);
        getaddress2.clear();
        getaddress2.insertText(0, temp);
        
        temp = getInfo(info, 3);
        getcity.clear();
        getcity.insertText(0, temp);
        
        temp = getInfo(info, 4);
        getstate.clear();
        getstate.insertText(0, temp);
        
        temp = getInfo(info, 5);
        getzipcode.clear();
        getzipcode.insertText(0, temp);
        
        temp = getInfo(info, 6);
        getphoneNumber.clear();
        getphoneNumber.insertText(0, temp);
        
        temp = getInfo(info, 7);
        getInsuranceProvider.clear();
        getInsuranceProvider.insertText(0, temp);
        
        temp = getInfo(info, 8);
        getmemberID.clear();
        getmemberID.insertText(0, temp);
        
        temp = getInfo(info, 9);
        getgroupNumber.clear();
        getgroupNumber.insertText(0, temp);
        
        temp = getInfo(info, 10);
        getassignedDoctor.clear();
        getassignedDoctor.insertText(0, temp);
        
        /*
         * 
         *     @FXML private TextField getDOB;
    @FXML private TextField getaddress1;
    @FXML private TextField getaddress2;
    @FXML private TextField getphoneNumber;
    @FXML private TextField getInsuranceProvider;
    @FXML private TextField getmemberID;
    @FXML private TextField getgroupNumber;
    @FXML private TextField getassignedDoctor;
         * 
         */
        
        
        
        
        //temp = getInfoDOB(plist2);
        //System.out.println(temp);
        //getDOB.insertText(0, temp);
        
        
        
        
        //System.out.println(plist2.get(k));
        db.closeConnection();
    }
    
    
    @FXML protected void addDataPatient(ActionEvent e){
        //combo.getSelectedItem();
        //value
        db.Connect();
        String DOB = getDOB.getText();
        String address1 = getaddress1.getText();
        String address2 = getaddress2.getText();
        String city = getcity.getText();
        String state = getstate.getText();
        String zipcode = getzipcode.getText();
        String phoneNumber = getphoneNumber.getText();
        String InsuranceProvider = getInsuranceProvider.getText();
        String memberID = getmemberID.getText();
        String groupNumber = getgroupNumber.getText();
        String assignedDoctor = getassignedDoctor.getText();
        
        
        
        /////////////////////////////////////////////////////////
        String y = (String)combo.getSelectionModel().getSelectedItem();
        //System.out.println(y);
        String[] tokens = y.split(" ");
        if(tokens.length!=2){throw new IllegalArgumentException();}
        String fName = tokens[0];
        String lName = tokens[1];
        /////////////////////////////////////////////////////////
        
        //System.out.println("user name is " + fName + " " + lName);
        List plist2 = db.dbQuery("Select userid FROM users WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
        
        
        

        String userId1 = plist2.toString();

        
        
        db.updateDB("UPDATE users SET DOB = '"+DOB+"' WHERE FirstName = \""+fName+"\" AND LastName=\""+lName+"\"");
        
        
        int index = 0;
        while(userId1.charAt(index) != ']')        

        {
            index++;
        }
        
        
        
        String userId = userId1.substring(2, index);
        db.updateDB("UPDATE Patient SET Address1 = '"+address1+"', Address2 = '"+address2+
                "',City = '"+city+"', State = '"+state+"',ZipCode = '"+zipcode+
                "',PhoneNumber = '"+phoneNumber+"',InsuranceProvider = '"+InsuranceProvider+
                "',memberid = '"+memberID+"',groupid = '"+groupNumber+"',Doctor = '"+assignedDoctor+
                "' WHERE userid = '"+userId+"'");

        
        db.closeConnection();
        
    }
    
    
    @FXML protected void cancelButton(ActionEvent e){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       db = new Database();
        db.Connect();
        


        
        
        List plist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList(plist.size());
    
        for(int i = 0; i < plist.size(); i++){
            patientname.add((String) ((List) plist.get(i)).get(0) + " " + (String) ((List) plist.get(i)).get(1));
        }
        
        ObservableList<String> olist = FXCollections.observableList(patientname);
        combo.getItems().addAll(olist);

        
        
        


    

        

      
        db.closeConnection();
        

    }    
}
