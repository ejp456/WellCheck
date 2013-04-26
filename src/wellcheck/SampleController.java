//Controller for the Main Screen
//Handles button clicks to login
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.ArrayList;
import static wellcheck.DoctorWindowController.db;

/**
 *
 * @author Etai
 */
public class SampleController implements Initializable, ControlledScreen {
    
    @FXML private TextField username;
    @FXML private PasswordField password;
    private Database db = new Database();
    public static String patient = "";
    ScreenController myController;
    private ObservableList<PatientTable> patientList;
   
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        db.Connect();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        boolean test = db.userExist(user);
        String usertype = db.getUserType(user, pass);
        if(test){
        System.out.println("User Exists");
        test = db.checkPassword(user, pass);
            if(test){
                
                if(usertype.equalsIgnoreCase("Nurse")){
                    System.out.println("Password is correct");
                    myController.setScreen(WellCheck.screenID2);
                    db.patientTable();
                }
                else if(usertype.equalsIgnoreCase("Patient")){
                     ArrayList plist = db.dbQuery("SELECT FirstName, LastName FROM users Where username =\""+user+"\"");
                     //patient = (String) ((ArrayList) plist.get(0)).get(0) + " " + (String) ((ArrayList) plist.get(0)).get(1);
                     db.patientScreenTable((String) ((ArrayList) plist.get(0)).get(0), (String) ((ArrayList) plist.get(0)).get(1));
                     myController.setScreen(WellCheck.screenID3);
                }else{
                //if username and password is correct program moves to main screen
                    System.out.println("Password is correct");
                    myController.setScreen(WellCheck.screenID2);
                    db.patientTable();
                    wellcheck.DoctorWindowController.userType(usertype);
                    }
                
                }
        }
        username.clear();
        password.clear();
        System.out.println(username.getText().toString());
        
    }
    @FXML protected void forgotButton(ActionEvent event) throws Exception{
       Parent root = FXMLLoader.load(getClass().getResource("ForgotScreen.fxml"));
       Stage stage = new Stage();
       Scene scene = new Scene(root);
       
       stage.setScene(scene);
       stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
}
