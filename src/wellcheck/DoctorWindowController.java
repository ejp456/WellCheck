/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Etai
 */
public class DoctorWindowController implements Initializable, ControlledScreen {
    @FXML public static ObservableList<PatientTable> patientList;
    @FXML private TableView patientTable;
    @FXML private TableColumn patient, doctor;
    private Database db = new Database();
    ScreenController myController;
    

    
    @FXML protected void addPatientButton(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AddPatientWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
       
        stage.setScene(scene);
        stage.show();
    }
    @FXML protected void logOut(ActionEvent event){
        patientList.clear();
        myController.setScreen(WellCheck.screenID1);
    }
    @FXML protected void editPatient(ActionEvent event) throws Exception{
       //patientList.add(new PatientTable("girl", "doctor Koo"));
       Parent root = FXMLLoader.load(getClass().getResource("EditPatientInformation.fxml"));
       Stage stage = new Stage();
       Scene scene = new Scene(root);
       
       stage.setScene(scene);
       stage.show();
    }
    @FXML protected void newEntry(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("addNewEntry.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
       
        stage.setScene(scene);
        stage.show();
    }
    @FXML protected void removePatient(ActionEvent event){
        db.Connect();
        PatientTable patient =(PatientTable) patientTable.getSelectionModel().getSelectedItem();
        String name[] = patient.getPatient().split(" ");
        if(name.length>=2){
            patientList.remove(patient);
            db.deleteUser(name[0], name[1]);
        }
        
        
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
    public static void addPatient(String p,String d){
        patientList.add(new PatientTable(p,d));
    }
}
