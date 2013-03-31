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

/**
 *
 * @author Etai
 */
public class SampleController implements Initializable, ControlledScreen {
    
    @FXML private TextField username;
    @FXML private PasswordField password;
    private Database db = new Database();
    ScreenController myController;
    private ObservableList<PatientTable> patientList;
   
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        db.Connect();
        boolean test = db.userExist(username.getText().toString());
        if(test){
        System.out.println("User Exists");
        test = db.checkPassword(username.getText().toString(), password.getText().toString());
            if(test){
                //if username and password is correct program moves to main screen
                System.out.println("Password is correct");
                myController.setScreen(WellCheck.screenID2);
                db.patientTable();
                
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
