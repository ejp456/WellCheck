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
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Etai
 */
public class PatientWindowController implements Initializable, ControlledScreen {
    @FXML public static ObservableList<PatientTable> patientList2;
    @FXML public static ObservableList<String> comboList2;
    @FXML public static ObservableList<dataTable> dataList2;
    @FXML private TableView patientTable;
    @FXML private TableColumn patient2, doctor2;
    @FXML public static ComboBox patientDropDown2;
    @FXML public static Label typeLabel2;
    public static ArrayList<dataTable> data2;
    public static  Database db = new Database();
    ScreenController myController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    public static void patientName(String user){
        typeLabel2.setText("Logged in as: "+user);
        }
    @FXML protected void logOut2(ActionEvent event){
        patientList2.clear();
        myController.setScreen(WellCheck.screenID1);
    }
     public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
     public static void addPatient2(String p,String d){
        patientList2.add(new PatientTable(p,d));
    }
    @FXML protected void dataComboBox2(ActionEvent event){
       db.Connect();
       dataList2.clear();
       String name = (String)patientDropDown2.getSelectionModel().getSelectedItem();
       String delims = "[ ]";
       String[] tokens = name.split(delims);
       data2 = db.dataTable(tokens[0], tokens[1]);
       dataList2.addAll(data2);
       db.closeConnection();
    }   
    public static void patientComboBox(String p){
        comboList2.add(p);
    }
}
