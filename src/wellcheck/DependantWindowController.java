/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Kent
 */
public class DependantWindowController implements Initializable {

    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Button confirm1;
    @FXML
    private Button cancel1;
    @FXML
    private Button confirm2;
    @FXML
    private Button cancel2;
    @FXML
    private Button donebutton;
    @FXML
    private ComboBox dependee;
    @FXML
    private ComboBox depender;
    @FXML
    private Label confirmtext;
    private Database db;

    @FXML
    protected void close(ActionEvent event) throws Exception {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void confirm1(ActionEvent event) {
        if (dependee.getValue() == null || depender.getValue() == null) {
            confirmtext.setText("Please select a valid value.");
            confirm2.setDisable(true);
        } else if (dependee.getValue().equals(depender.getValue())) {
            confirmtext.setText("Cannot make someone their own dependent.");
            confirm2.setDisable(true);
        } else {
            confirmtext.setText("Are you sure you want to add " + dependee.getValue()
                    + " as a dependant to " + depender.getValue()
                    + "? " + depender.getValue()
                    + " will be able to view all of " + dependee.getValue()
                    + "'s records.");
        }

        text1.setVisible(false);
        text2.setVisible(false);
        confirm1.setVisible(false);
        confirm2.setVisible(true);
        cancel1.setVisible(false);
        cancel2.setVisible(true);
        dependee.setVisible(false);
        depender.setVisible(false);
        confirmtext.setVisible(true);
    }

    @FXML
    protected void confirm2(ActionEvent event) {
        String[] provider = ((String) dependee.getValue()).split("\\s+");
        String[] dependant = ((String) depender.getValue()).split("\\s+");

        db = new Database();
        db.Connect();


        List<List> rlist;

        rlist = db.dbQuery("SELECT userid FROM users WHERE FirstName = '" + provider[0] + "' AND LastName = '" + provider[1] + "'");
        int providerid = (Integer) rlist.get(0).get(0);
        rlist = db.dbQuery("SELECT userid FROM users WHERE FirstName = '" + dependant[0] + "' AND LastName = '" + dependant[1] + "'");
        int dependantid = (Integer) rlist.get(0).get(0);
        if (dependantid != -1 && providerid != -1) {
            db.updateDB("UPDATE Patient SET DependantTo = " + providerid + " WHERE userid = " + dependantid);
            confirmtext.setText("" + depender.getValue() + " set as a dependant to "
                    + dependee.getValue() + ".");
        } else {
            confirmtext.setText("" + depender.getValue() + " NOT set as a dependant to "
                    + dependee.getValue() + ".");
        }

        confirmtext.setText("" + depender.getValue() + " set as a dependant to "
                + dependee.getValue() + ".");
        confirmtext.setVisible(true);
        donebutton.setVisible(true);
        confirm2.setVisible(false);
        cancel2.setVisible(false);
        
        db.closeConnection();
    }

    @FXML
    protected void cancel2(ActionEvent event) {
        text1.setVisible(true);
        text2.setVisible(true);
        confirm1.setVisible(true);
        confirm2.setVisible(false);
        cancel1.setVisible(true);
        cancel2.setVisible(false);
        dependee.setVisible(true);
        depender.setVisible(true);
        confirmtext.setVisible(false);
        confirm2.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Database();
        db.Connect();

        List<List> rlist;
        rlist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList();
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            patientname.add((String) it2.next() + " " + (String) it2.next());
        }

        dependee.getItems().addAll(patientname);
        depender.getItems().addAll(patientname);

        db.closeConnection();

        confirm2.setVisible(false);
        cancel2.setVisible(false);
        confirmtext.setVisible(false);
        donebutton.setVisible(false);
    }
}
