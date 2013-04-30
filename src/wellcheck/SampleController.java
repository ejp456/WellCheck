//Controller for the Main Screen
//Handles button clicks to login
package wellcheck;

import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import static wellcheck.DoctorWindowController.db;

/**
 *
 * @author Etai
 */
public class SampleController implements Initializable, ControlledScreen {
    
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label loginerror;
    //private Database db = new Database();
    public static String patient = "";
    ScreenController myController;
    
    private DoctorWindowController dwcontroller;
   
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        db.Connect();
        setDWController();
        loginerror.setVisible(false);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        boolean test = db.userExist(user);
        String usertype = db.getUserType(user, pass);
        if(test){
        test = db.checkPassword(user, pass);
            if(test){
                
                //wellcheck.DoctorWindowController.userType(usertype);
                if(usertype.equalsIgnoreCase("Nurse")){
                    System.out.println("Password is correct");
                    db.patientTable();
                    wellcheck.DoctorWindowController.buttonToggle(true);
                }
                else if(usertype.equalsIgnoreCase("Patient")){
                     List<List> plist = db.dbQuery("SELECT FirstName, LastName FROM users Where username =\""+user+"\"");
                     //patient = (String) ((ArrayList) plist.get(0)).get(0) + " " + (String) ((ArrayList) plist.get(0)).get(1);
                     db.patientScreenTable((String) ((ArrayList) plist.get(0)).get(0), (String) ((ArrayList) plist.get(0)).get(1));
                     wellcheck.DoctorWindowController.buttonToggle(false);
                }else{
                //if username and password is correct program moves to main screen
                    System.out.println("Password is correct");
                    db.patientTable();
                    wellcheck.DoctorWindowController.buttonToggle(true);
                    }
                //Kent's additions
                //Sets data in the DWC class to appropriate values based on login
                //Needed for things like storing what the current user is.
                dwcontroller.currentuserid = (Integer) ((List) db.dbQuery("SELECT userid FROM users WHERE username = '" + user + "' AND password = '" + pass + "'").get(0)).get(0);
                dwcontroller.onLogin();
                myController.setScreen("DoctorWindow");
            }
            else
            {
                loginerror.setText("Password incorrect.");
                loginerror.setVisible(true);
            }
        }
        else
        {
            loginerror.setText("Username does not exist.");
            loginerror.setVisible(true);
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
    
    @FXML protected void close(ActionEvent event) throws Exception {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setScreenParent(ScreenController screenParent){
        myController = screenParent;
    }
    
    /* Kent's method addition
     * This method creates the instance of the DoctorWindowController class
     * that is used by the rest of the program. It's done here instead of
     * WellCheck.java, where it was done previously, so that I can have the
     * instance handy to pass data to, without having to resort to public
     * static date members.
     */
    public void setDWController() {
        if(dwcontroller == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                Node n = (Node) loader.load(getClass().getResource("DoctorWindow.fxml").openStream());
                dwcontroller = (DoctorWindowController) loader.getController();
                myController.addScreen("DoctorWindow", n);
                dwcontroller.setScreenParent(myController);
            } catch (IOException ex) {
                Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
