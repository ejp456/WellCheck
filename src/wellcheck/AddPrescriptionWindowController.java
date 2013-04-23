/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kent
 */
public class AddPrescriptionWindowController implements Initializable {

    @FXML
    private Button add, cancel;
    @FXML
    private TextField prescription, treatment, dateyear, dateday, datemonth, length;
    @FXML
    private Label errorlabel, datelabel;
    @FXML
    private ComboBox doctorbox;
    private Patient selectedpatient;
    private DoctorWindowController parentcontroller;

    @FXML
    protected void add(ActionEvent event) {
        Database db = new Database();
        db.Connect();

        String prescrstring = prescription.getText();
        String lengthstring = length.getText();
        String treatstring = treatment.getText();
        String dystring = dateyear.getText();
        String ddstring = dateday.getText();
        String dmstring = datemonth.getText();
        String[] doctorname;

        int doctorid = -1;
        int patientid = -1;
        int lengthint = -1;
        int dyint = -1;
        int ddint = -1;
        int dmint = -1;
        boolean validentry;

        if (doctorbox.getValue() != null) {
            doctorname = ((String) doctorbox.getValue()).split("\\s+");
            List<List> list = db.dbQuery("SELECT userid FROM users WHERE LastName = '" + doctorname[1] + "'");
            doctorid = (Integer) list.get(0).get(0);
        }
        patientid = selectedpatient.getUserid();



        try {
            lengthint = Integer.parseInt(lengthstring);
            dyint = Integer.parseInt(dystring);
            ddint = Integer.parseInt(ddstring);
            dmint = Integer.parseInt(dmstring);
            validentry = true;
        } catch (NumberFormatException e) {
            validentry = false;
        }

        if (!validentry || doctorid == -1) {
            errorlabel.setText("Please enter valid values.");
            errorlabel.setVisible(true);
        } else if (dyint < 1800 || dyint > 2100 || ddint < 0 || ddint > 31 || dmint < 0 || dmint > 12) {
            errorlabel.setText("Please enter a valid date.");
            errorlabel.setVisible(true);
        } else if (lengthint < 0) {
            errorlabel.setText("Please enter a valid length.");
            errorlabel.setVisible(true);
        } else if (prescrstring.equals("") || treatstring.equals("")) {
            errorlabel.setText("Please fill all fields.");
            errorlabel.setVisible(true);
        } else {
            Prescription insertprescription = new Prescription(0, patientid, doctorid, prescrstring, treatstring, dyint + "-" + dmint + "-" + ddint, lengthint);
            selectedpatient.getPatientprescription().add(insertprescription);
            Prescription.insertPrescriptionRow(insertprescription);

            errorlabel.setText("Prescription added");
            errorlabel.setVisible(true);

            datelabel.setVisible(false);
            prescription.setVisible(false);
            treatment.setVisible(false);
            length.setVisible(false);
            dateyear.setVisible(false);
            dateday.setVisible(false);
            datemonth.setVisible(false);
            doctorbox.setVisible(false);
            add.setVisible(false);
            cancel.setText("Done");
            parentcontroller.updatePrescriptionTable();
        }

        db.closeConnection();
    }

    @FXML
    protected void close(ActionEvent event) throws Exception {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        db.Connect();
        List<List> rlist = db.dbQuery("Select LastName FROM users WHERE usertype = 'Doctor'");
        ArrayList<String> doctorname = new ArrayList();
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            doctorname.add("Dr. " + (String) it2.next());
        }
        doctorbox.getItems().addAll(doctorname);
        errorlabel.setVisible(false);
        db.closeConnection();
    }

    public void setData(Patient p, DoctorWindowController d) {
        selectedpatient = p;
        parentcontroller = d;
    }
}
