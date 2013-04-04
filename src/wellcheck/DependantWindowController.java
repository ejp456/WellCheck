/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author Kent
 */
public class DependantWindowController implements Initializable {
    @FXML private Button confirm1;
    @FXML private Button cancel1;
    @FXML private Button confirm2;
    @FXML private Button cancel2;
    @FXML private ComboBox dependee;
    @FXML private ComboBox depender;
    @FXML private Label confirmtext;
    private String confirmstring = "Are you sure you want to add %s %s as"
            + "a dependant to Patient 2? Patient 1 will be able to view all of "
            + "%s %s's records.";    
    private Database db;
    
    @FXML protected void close(ActionEvent event) throws Exception{
        Node  source = (Node)  event.getSource(); 
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Database();
        db.Connect();

        ArrayList plist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList(plist.size());
    
        for(int i = 0; i < plist.size(); i++){
            patientname.add((String) ((ArrayList) plist.get(i)).get(0) + " " + (String) ((ArrayList) plist.get(i)).get(1));
        }
        
        ObservableList<String> olist = FXCollections.observableList(patientname);;
        dependee.getItems().addAll(olist);
        depender.getItems().addAll(olist);
      
        db.closeConnection();
    }  
}
