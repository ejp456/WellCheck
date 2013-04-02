/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


/**
 * FXML Controller class
 *
 * @author etaipinckney
 */
public class ForgotScreenController implements Initializable {
    @FXML private Button getPass;
    @FXML private Button getUser;
    @FXML private TextField enterFirstName;
    @FXML private TextField enterLastName;
    @FXML private Label secretQuestion;
    String secretAnswer;
    String password;
    String username;
    String lName;
    String fName;
    Database db = new Database();
    
    
    
    @FXML protected void forgotPassword(ActionEvent e){
    if(getPass.getText().equalsIgnoreCase("Answer")){
        if(secretAnswer.equalsIgnoreCase(enterFirstName.getText())){
            secretQuestion.setText(password);
            secretQuestion.setTextFill(Color.RED);
            enterFirstName.setVisible(false);
            getPass.setVisible(false);
        }
    }else{
    db.Connect();
    String lName = enterLastName.getText();
    String fName = enterFirstName.getText();
    String secret = db.getSecretQuestion(fName, lName);
    System.out.println();
    secretAnswer = db.getSecretAnswer(fName, lName);
    password = db.getPassword(fName, lName);
    if(secret == null){
        secretQuestion.setText("Name doesn't exist.");
        secretQuestion.setTextFill(Color.RED);
    }else{
        secretQuestion.setText(secret);
        getPass.setText("Answer");
        enterFirstName.clear();
        enterFirstName.setPromptText("");
        enterLastName.setVisible(false);
        getUser.setVisible(false);
        }
    }
    
    
    }
    @FXML protected void forgotUserName(ActionEvent e){
           lName = enterLastName.getText();
           fName = enterFirstName.getText();
           username = db.getUserName(fName, lName);
           System.out.println(username);
           if(username == null){
            secretQuestion.setText("Name doesn't exist.");
            secretQuestion.setTextFill(Color.RED);
            }else{
            secretQuestion.setText(username);
            secretQuestion.setTextFill(Color.RED);
            getPass.setVisible(false);
            enterFirstName.setVisible(false);
            enterLastName.setVisible(false);
            getUser.setVisible(false);
            }
           
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
