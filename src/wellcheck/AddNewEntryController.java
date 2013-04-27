package wellcheck;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import static wellcheck.DoctorWindowController.comboList;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Etai
 */
public class AddNewEntryController implements Initializable {
    ComboBox patientBox;
    Database db = new Database();
    @FXML private TextField enterBloodPressure;
    @FXML private TextField enterWeight;
    @FXML private TextField enterSugar;
    @FXML private TextField enterComments;
    @FXML private ChoiceBox date_day;
    @FXML private ChoiceBox date_month;
    @FXML private ChoiceBox date_year;
    @FXML private Label errorLabel;
    @FXML public static Label patientLabel;
    @FXML private javafx.scene.control.Button cancel;
    @FXML private Text Comments;
    private String bloodPressure = " ";
    private String weight = " ";
    private String sugar = " ";
    private String comments = " ";
    private String userType = "Patient";
    private String date = " ";
    @FXML private Button add;
    
    
   @FXML protected void add(ActionEvent e){  
      db.Connect();
      errorLabel.setText("");
      try{
      if(enterBloodPressure.getText().isEmpty()){
          errorLabel.setTextFill(Color.RED);
          errorLabel.setText("Please enter blood pressure.");
      }
      else if(enterWeight.getText().isEmpty()){
          errorLabel.setTextFill(Color.RED);
          errorLabel.setText("Please enter weight.");
      }
      else if(enterSugar.getText().isEmpty()){
          errorLabel.setTextFill(Color.RED);
          errorLabel.setText("Please enter sugar level.");
      }else{
          if(enterComments.getText().isEmpty()){
            comments = "empty";
          }else{
              comments = enterComments.getText();
          }
        bloodPressure = enterBloodPressure.getText();
        weight = enterWeight.getText();
        sugar = enterSugar.getText();
        date = ""+date_year.getSelectionModel().getSelectedItem().toString()+"-"+date_month.getSelectionModel().getSelectedItem().toString()+"-"+date_day.getSelectionModel().getSelectedItem().toString();
        String delims = "[ ]";
        String[] name = patientLabel.getText().split(delims);
        System.out.println("His name is "+name[0]+" "+name[1]+" and his id is ");
        db.addEntry(name[0], name[1], bloodPressure, sugar, weight, date, comments);
        wellcheck.DoctorWindowController.refresh();
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Entry succesfully added.");
        }
      }catch(NullPointerException ex){
          errorLabel.setTextFill(Color.RED);
          errorLabel.setText("Please enter a date.");
        }
      }
      
      
        
    @FXML protected void cancel(ActionEvent e){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       String name = wellcheck.DoctorWindowController.getPatientName();
       patientLabel.setText(name);
       if(wellcheck.DoctorWindowController.isNurse()){
           enterComments.setVisible(false);
           Comments.setVisible(false);
       }
    }  
   
}
