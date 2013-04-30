/*
 * Kent's Class
 * This class holds methods for the Account Subwindow, which is the popup
 * window that shows up when buttons on the account page are pressed.
 * 
 * These allow editing of account credentials, such as password, challenge question,
 * challenge answer, name, and date of birth.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AccountSubwindowController implements Initializable {

    @FXML
    private Button saveinformationbutton, changequestionbutton, changepasswordbutton, accountcancel;
    @FXML
    private TitledPane accounttitle;
    @FXML
    private AnchorPane informationpane, questionpane, passwordpane;
    @FXML
    private Label accountconfirmlabel;
    @FXML
    private PasswordField oldpasswordfield, newpasswordfield, confirmpasswordfield;
    @FXML
    private TextField fnamefield, lnamefield, datemfield, datedfield, dateyfield, challengeanswerfield;
    @FXML
    private TextArea challengequestionfield;
    private boolean editflag, passwordflag, questionflag;
    private DoctorWindowController parentcontroller;
    private User currentuser;

    /*
     * This method allows editing of a user's first name, last name, and date of birth.
     */
    @FXML
    private void saveinformation() {
        String fstring = fnamefield.getText();
        String lstring = lnamefield.getText();
        int dyint = -1;
        int dmint = -1;
        int ddint = -1;
        boolean validentry;

        //Begin error handling
        try {
            dyint = Integer.parseInt(dateyfield.getText());
            ddint = Integer.parseInt(datedfield.getText());
            dmint = Integer.parseInt(datemfield.getText());
            validentry = true;
        } catch (NumberFormatException e) {
            validentry = false;
        }

        //If dates entered are not integers, display an error message
        if (!validentry) {
            accountconfirmlabel.setText("Please enter valid values.");
            accountconfirmlabel.setVisible(true);
        } //Else if dates are not in valid range, display an error message
        else if (dyint < 1800 || dyint > 2100 || ddint < 0 || ddint > 31 || dmint < 0 || dmint > 12) {
            accountconfirmlabel.setText("Please enter a valid date.");
            accountconfirmlabel.setVisible(true);
        } //Else if first name and last names are empty strings, display an error message
        else if (fstring.equals("") || lstring.equals("")) {
            accountconfirmlabel.setText("Please enter both names.");
            accountconfirmlabel.setVisible(true);
        } //Else save the information to the database and display a confirmation.
        else {
            currentuser.setFirstname(fnamefield.getText());
            currentuser.setLastname(lnamefield.getText());
            currentuser.setDateofbirth(dateyfield.getText() + "-" + datemfield.getText() + "-" + datedfield.getText());
            User.updateRow(currentuser);

            informationpane.setVisible(false);
            accountconfirmlabel.setText("Information saved.");
            accountconfirmlabel.setVisible(true);
            accountcancel.setText("Done");
            parentcontroller.updateAccountPage();
        }
    }

    /*
     * This method allows editing of a user's challenge question and answer
     */
    @FXML
    private void changequestion() {
        String question = challengequestionfield.getText();
        String answer = challengeanswerfield.getText();

        //Begin error handling
        //If challenge question or answer are empty strings, display an error message
        if (question.equals("") || answer.equals("")) {
            accountconfirmlabel.setText("Please enter both a question and an answer.");
            accountconfirmlabel.setVisible(true);
        } //Else, save the information to the database and display a confirmation message.
        else {
            currentuser.setSecretquestion(question);
            currentuser.setSecretanswer(answer);
            User.updateRow(currentuser);

            questionpane.setVisible(false);
            accountconfirmlabel.setText("Challenge question and answer saved.");
            accountconfirmlabel.setVisible(true);
            accountcancel.setText("Done");
        }
    }

    /*
     * This method allows a user to change their password
     */
    @FXML
    private void changepassword() {
        String oldpassword = oldpasswordfield.getText();
        String newpassword = newpasswordfield.getText();
        String confirmpassword = confirmpasswordfield.getText();

        //Begin error handling
        //If the password fields are empty, display an error message.
        if (oldpassword.equals("") || newpassword.equals("") || confirmpassword.equals("")) {
            oldpasswordfield.clear();
            newpasswordfield.clear();
            confirmpasswordfield.clear();
            accountconfirmlabel.setText("Please enter all fields.");
            accountconfirmlabel.setVisible(true);
        } //else if the old password does not match the current password, display an error message
        else if (!oldpassword.equals(currentuser.getPassword())) {
            oldpasswordfield.clear();
            newpasswordfield.clear();
            confirmpasswordfield.clear();
            accountconfirmlabel.setText("Old password is incorrect.");
            accountconfirmlabel.setVisible(true);
        } //Else if the new password and the confirmation password do not match, display an error message
        else if (!newpassword.equals(confirmpassword)) {
            oldpasswordfield.clear();
            newpasswordfield.clear();
            confirmpasswordfield.clear();
            accountconfirmlabel.setText("New passwords do not match.");
            accountconfirmlabel.setVisible(true);
        } //Else, save the information to the database and display a confirmation message
        else {
            oldpasswordfield.clear();
            newpasswordfield.clear();
            confirmpasswordfield.clear();
            currentuser.setPassword(newpassword);
            User.updateRow(currentuser);

            passwordpane.setVisible(false);
            accountconfirmlabel.setText("Password changed.");
            accountconfirmlabel.setVisible(true);
            accountcancel.setText("Done");
        }
    }

    //This method closes the window.
    @FXML
    private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /*
     * This method sets the data needed for the Account Subwindow to function properly.
     * Primarily, it allows passing of data between the DoctorWindowController
     * and this window.
     */
    public void setData(DoctorWindowController d, boolean s, boolean c, boolean q, User cuser) {
        parentcontroller = d;
        editflag = s;
        questionflag = q;
        passwordflag = c;
        currentuser = cuser;

        //Displays or hides anchor panes based on button choice on Account page
        informationpane.setVisible(editflag);
        questionpane.setVisible(questionflag);
        passwordpane.setVisible(passwordflag);
        accountconfirmlabel.setVisible(false);

        //Preloads information into relevant fields.
        fnamefield.setText(currentuser.getFirstname());
        lnamefield.setText(currentuser.getLastname());
        String[] datestring = currentuser.getDateofbirth().split("-");
        dateyfield.setText(datestring[0]);
        datemfield.setText(datestring[1]);
        datedfield.setText(datestring[2]);
        challengequestionfield.setText(currentuser.getSecretquestion());
        challengeanswerfield.setText(currentuser.getSecretanswer());
        
        if(editflag)
            accounttitle.setText("Edit Information");
        if(questionflag)
            accounttitle.setText("Change Challenge Question");
        if(passwordflag)
            accounttitle.setText("Change Password");
    }
}