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

/**
 * FXML Controller class
 *
 * @author Etai
 */
public class AddNewEntryController implements Initializable {
    @FXML ComboBox patientBox;
    Database db;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       db = new Database();
       db.Connect();
       System.out.println("new entry controller");
        List<List> plist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList(plist.size());
    
        for(int i = 0; i < plist.size(); i++){
            patientname.add((String) ((ArrayList) plist.get(i)).get(0) + " " + (String) ((ArrayList) plist.get(i)).get(1));
        }
        System.out.println("new entry controller");
        ObservableList<String> olist = FXCollections.observableList(patientname);
        patientBox.getItems().addAll(olist);
      
        db.closeConnection();
    }    
}
