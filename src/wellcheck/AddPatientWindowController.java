/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package wellcheck;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import static wellcheck.DoctorWindowController.patientList;
import static wellcheck.DoctorWindowController.comboList;
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
    @FXML private ComboBox doctorSelection;
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
    private String assignedDoctorId;
    private String assignedDoctorFirst;
    private String assignedDoctorLast;
    private String userId;
    private Database db = DoctorWindowController.db;
    
    
    
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
        assignedDoctorFirst = getDoctorFirst();
        assignedDoctorLast = getDoctorLast();
        userId = " ";
        
        db.Connect();
        assignedDoctorId = db.getId(assignedDoctorFirst);
        db.initializePatient(firstName, lastName, dateOfBirth, address1, address2, city, state, zip,
                phoneNumber, insuranceProvider, memberId, groupNumber, assignedDoctorId, userId);
        
        patientList.add(new PatientTable(firstName + " " + lastName, assignedDoctorFirst + " " + assignedDoctorLast));
        comboList.add(firstName + " " + lastName);
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML protected void cancel(ActionEvent e){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    private String getDoctorFirst() {
        String name = (String) doctorSelection.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);
        
        return tokens[0];
    }
    
    private String getDoctorLast() {
        String name = (String) doctorSelection.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);
        
        return tokens[1];
    }
    
    private void initializeDoctorSelection() {
        db.Connect();
        List<List> rlist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Doctor'");
        ArrayList<String> doctorList = new ArrayList();
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            doctorList.add((String) it2.next() + " " + (String) it2.next());
        }
        doctorSelection.getItems().clear();
        doctorSelection.getItems().addAll(doctorList);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeDoctorSelection();
    }
}