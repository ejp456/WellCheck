/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

/**
 *
 * @author Etai
 */
public class DoctorWindowController implements Initializable, ControlledScreen {
    
    
    private Database db = new Database();
    ScreenController myController;

    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
      
        
    }
    @FXML protected void logOut(ActionEvent event){
        myController.setScreen(WellCheck.screenID1);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
}
