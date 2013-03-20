//Controller for the Main Screen
//Handles button clicks to login
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

/**
 *
 * @author Etai
 */
public class SampleController implements Initializable, ControlledScreen {
    
    @FXML private TextField username;
    @FXML private PasswordField password;
    private Database db = new Database();
    ScreenController myController;
   
    
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
                }
        }
        username.clear();
        password.clear();
        System.out.println(username.getText().toString());
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
}
