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
    String secret;
    Database db = new Database();
    
    
    
    @FXML protected void forgotPassword(ActionEvent e){
    if(getPass.getText().equalsIgnoreCase("Answer")){
        if(secretAnswer.equalsIgnoreCase(enterFirstName.getText())){
            secretQuestion.setText("Password: "+password);
            secretQuestion.setTextFill(Color.RED);
            enterFirstName.setVisible(false);
            getPass.setVisible(false);
        }else{
            secretQuestion.setText("Wrong Answer");
            secretQuestion.setTextFill(Color.RED);
        }
    }else{
    db.Connect();
    lName = enterLastName.getText();
    fName = enterFirstName.getText();
    secret = db.getSecretQuestion(fName, lName);
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
        try{
           if(getUser.getText().equalsIgnoreCase("Answer")){
               if(enterLastName.getText().equalsIgnoreCase(secretAnswer)){
                   secretQuestion.setText("Username: "+username);
                   secretQuestion.setTextFill(Color.RED);
                   enterLastName.setVisible(false);
                   getUser.setVisible(false);
               }else{
                   secretQuestion.setText("Wrong Answer");
                   secretQuestion.setTextFill(Color.RED);
               }
           }else{
           db.Connect();
           lName = enterLastName.getText();
           fName = enterFirstName.getText();
           secret = db.getSecretQuestion(fName, lName);
           secretAnswer = db.getSecretAnswer(fName, lName);
           username = db.getUserName(fName, lName);
           if(username == null){
            secretQuestion.setText("Name doesn't exist.");
            secretQuestion.setTextFill(Color.RED);
            }else{
            secretQuestion.setText(secret);
            getUser.setText("Answer");
            enterLastName.clear();
            enterLastName.setPromptText("");
            enterFirstName.setVisible(false);
            getPass.setVisible(false);
            }
           }
        }catch(NullPointerException ex){
            secretQuestion.setText("All fields must be entered");
        }
           
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
