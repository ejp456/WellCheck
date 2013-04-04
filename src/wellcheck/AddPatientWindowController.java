/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

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
import static wellcheck.DoctorWindowController.patientList;
import javafx.stage.Stage;

/**
 *
 * @author Alan
 */
public class AddPatientWindowController implements Initializable {
        
    @FXML private TextField enterFirstName;
    @FXML private TextField enterLastName;
    @FXML private TextField enterAddress1;
    @FXML private TextField enterAddress2;
    @FXML private TextField enterCity;
    @FXML private TextField enterState;
    @FXML private TextField enterZip;
    @FXML private TextField enterPhoneNumber;
    @FXML private TextField enterMonth;
    @FXML private TextField enterDay;
    @FXML private TextField enterYear;
    @FXML private TextField enterInsuranceProvider;
    @FXML private TextField enterMemberId;
    @FXML private TextField enterGroupNumber;
    @FXML private TextField enterAssignedDoctor;
    @FXML private TextField enterUserId;
    @FXML private javafx.scene.control.Button cancel;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String dateOfBirth;
    private String insuranceProvider;
    private String memberId;
    private String groupNumber;
    private String assignedDoctor;
    private String userId;
    private String userType = "Patient";
    private Database db = new Database();
    
    
    
    @FXML protected void addPatient(ActionEvent e){
        firstName = enterFirstName.getText();
        lastName = enterLastName.getText();
        dateOfBirth = enterYear.getText() + "-" + enterMonth.getText() + "-" + enterDay.getText();
        address1 = enterAddress1.getText();
        address2 = enterAddress2.getText();
        city = enterCity.getText();
        state = enterState.getText();
        zip = enterZip.getText();
        phoneNumber = enterPhoneNumber.getText();
        insuranceProvider = enterInsuranceProvider.getText();
        memberId = enterMemberId.getText();
        groupNumber = enterGroupNumber.getText();
        assignedDoctor = enterAssignedDoctor.getText();
        userId = enterUserId.getText();
        
        db.Connect();
        db.initializePatient(firstName, lastName, dateOfBirth, address1, address2, city, state, zip,
                phoneNumber, insuranceProvider, memberId, groupNumber, assignedDoctor, userId);
        
        patientList.add(new PatientTable(firstName + " " + lastName, db.getDoctorFirst(assignedDoctor) + " " + db.getDoctorLast(assignedDoctor)));
        
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void cancel(ActionEvent e){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
