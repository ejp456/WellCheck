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




public class EditPatientInformationWindows implements Initializable {
    @FXML private Button saveInformation;
    @FXML private Button cancel;
    @FXML private TextField getfirstName;
    @FXML private TextField getlastName;
    @FXML private TextField getday;
    @FXML private TextField getmonth;
    @FXML private TextField getyear;
    @FXML private TextField getaddress1;
    @FXML private TextField getaddress2;
    @FXML private TextField getphoneNumber;
    @FXML private TextField getInsuranceProvider;
    @FXML private TextField getmemberID;
    @FXML private TextField getgroupNumber;
    @FXML private TextField getassignedDoctor;
    String firstName;
    String lastName;
    String day;
    String month;
    String year;
    String address;
    //String address2;
    String phoneNumber;
    String InsuranceProvider;
    String memberID;
    String groupNumber;
    String assignedDoctor;
    Database db = new Database();
    
    
    
    

    
    
    
    
    @FXML protected void addDataPatient(ActionEvent e){
        db.Connect();
        String firstName = getfirstName.getText();
        String lastName = getlastName.getText();
        String day = getday.getText();
        String month = getmonth.getText();
        String year = getyear.getText();
        String address = getaddress1.getText() + " " + getaddress2.getText();
        String phoneNumber = getphoneNumber.getText();
        String InsuranceProvider = getInsuranceProvider.getText();
        String memberID = getmemberID.getText();
        String groupNumber = getgroupNumber.getText();
        String assignedDoctor = getassignedDoctor.getText();
        
        
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(day);
        System.out.println(month);
        System.out.println(year);
        System.out.println(address);
        System.out.println(phoneNumber);
        System.out.println(InsuranceProvider);
        System.out.println(memberID);
        System.out.println(groupNumber);
        System.out.println(assignedDoctor);
        
    }
    
    
    @FXML protected void cancelButton(ActionEvent e){
        getfirstName.clear();
        getlastName.clear();
        getday.clear();
        getmonth.clear();
        getyear.clear();
        getaddress1.clear();
        getaddress2.clear();
        getphoneNumber.clear();
        getInsuranceProvider.clear();
        getmemberID.clear();
        getgroupNumber.clear();
        getassignedDoctor.clear();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
