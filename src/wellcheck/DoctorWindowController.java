/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 *
 * @author Etai
 */
public class DoctorWindowController implements Initializable, ControlledScreen {
    @FXML public static ObservableList<PatientTable> patientList;
    @FXML public static ObservableList<String> comboList;
    @FXML public static ObservableList<dataTable> dataList;
    @FXML private TableView patientTable;
    @FXML private TableColumn patient, doctor;
    @FXML public static ComboBox patientDropDown;
    @FXML public static Label typeLabel;
    @FXML public static Button editEntry;
    @FXML public static Button addPatient;
    @FXML public static Button editPatient;
    @FXML public static Button addDep;
    @FXML public static Button removePatient;
    public static ArrayList<dataTable> data;
    public static  Database db = new Database();
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
            comboList.remove(name[0]+" "+name[1]);
            db.deleteUser(name[0], name[1]);
        }
        
        
    }
    @FXML protected void showGraph(ActionEvent event)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LineChart.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
       
        stage.setScene(scene);
        stage.show();
    }
    @FXML protected void addDependant(ActionEvent event) throws Exception{
       Parent root = FXMLLoader.load(getClass().getResource("DependantWindow.fxml"));
       Stage stage = new Stage();
       Scene scene = new Scene(root);
       
       stage.setScene(scene);
       stage.show();
    }
    @FXML protected void dataComboBox(ActionEvent event){
       db.Connect();
       dataList.clear();
       String name = (String)patientDropDown.getSelectionModel().getSelectedItem();
       String delims = "[ ]";
       String[] tokens = name.split(delims);
       data = db.dataTable(tokens[0], tokens[1]);
       dataList.addAll(data);
       db.closeConnection();
    }  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
      

        
    }
    public static void userType(String user){
        typeLabel.setText("Logged in as: "+user);
    }
    public static void addPatient(String p,String d){
        patientList.add(new PatientTable(p,d));
    }
     public static void patientComboBox(String p){
        comboList.add(p);
    }
    public static void refresh(){
       db.Connect();
       dataList.clear();
       String name = (String)patientDropDown.getSelectionModel().getSelectedItem();
       String delims = "[ ]";
       String[] tokens = name.split(delims);
       data = db.dataTable(tokens[0], tokens[1]);
       dataList.addAll(data);
       db.closeConnection();
    }
    public static String getSelectedPatient() {
        String name = (String)patientDropDown.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);
        
        return tokens[0];
    }
    public static boolean isNurse(){
        String type = (String)typeLabel.getText();
        String delims = "[ ]";
        String[] tokens = type.split(delims);
        if(tokens[tokens.length-1].equalsIgnoreCase("nurse")){
            return true;
        }else{
            return false;
        }
    }
    public static void patientWindow(){
       editEntry.setVisible(false);
       addPatient.setVisible(false);
       editPatient.setVisible(false);
       addDep.setVisible(false);
       removePatient.setVisible(false);
    }
    public static String getPatientName(){
        String name = (String)patientDropDown.getSelectionModel().getSelectedItem();
        return name;
    }
     public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
}
