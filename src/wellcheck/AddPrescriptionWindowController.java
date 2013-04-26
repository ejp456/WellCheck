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
    private Button add, edit, remove, cancel;
    @FXML
    private TextField prescription, treatment, dateyear, dateday, datemonth, length;
    @FXML
    private Label errorlabel, datelabel;
    @FXML
    private ComboBox doctorbox;
    private DoctorWindowController parentcontroller;
    private List<List> doctorname;
    private boolean addflag, editflag, removeflag;
    private PrescriptionTable selectedprescription;
    
    @FXML
    protected void addedit(ActionEvent event) {
        String prescrstring = prescription.getText();
        String lengthstring = length.getText();
        String treatstring = treatment.getText();
        String dystring = dateyear.getText();
        String ddstring = dateday.getText();
        String dmstring = datemonth.getText();
        String dname;
        
        int doctorid = -1;
        int patientid = -1;
        int lengthint = -1;
        int dyint = -1;
        int ddint = -1;
        int dmint = -1;
        dname = null;
        boolean validentry;
        
        if (doctorbox.getValue() != null) {
            for (int i = 0; i < doctorname.get(0).size(); i++) {
                if ((doctorname.get(0).get(i)).equals(doctorbox.getValue())) {
                    doctorid = (Integer) doctorname.get(1).get(i);
                    String[] tempstring = ((String) doctorname.get(0).get(i)).split("\\s+");
                    dname = tempstring[1];
                }
            }
        }
        patientid = parentcontroller.selectedpatientid;
        
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
            Prescription updateprescription = new Prescription(0, patientid, doctorid, dname, prescrstring, treatstring, dyint + "-" + dmint + "-" + ddint, lengthint);
            if (addflag) {
                Prescription.insertPrescriptionRow(updateprescription);
            }
            if (editflag) {
                updateprescription.setId(selectedprescription.getId());
                Prescription.updatePrescriptionRow(updateprescription);
            }
            
            if (addflag) {
                errorlabel.setText("Prescription added.");
            }
            if (editflag) {
                errorlabel.setText("Prescription edited.");
            }
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
            edit.setVisible(false);
            cancel.setText("Done");
            parentcontroller.updatePrescriptionTable();
        }
    }
    
    @FXML
    protected void remove(ActionEvent event) throws Exception {
        Prescription p = new Prescription();
        p.setId(selectedprescription.getId());
        Prescription.deletePrescriptionRow(p);
        errorlabel.setText("Prescription removed.");
        remove.setVisible(false);
        cancel.setText("Done");
        parentcontroller.updatePrescriptionTable();
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
        List<List> rlist = db.dbQuery("Select LastName, userid FROM users WHERE usertype = 'Doctor'");
        doctorname = new ArrayList();
        doctorname.add(0, new ArrayList());
        doctorname.add(1, new ArrayList());
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            doctorname.get(0).add("Dr. " + (String) it2.next());
            doctorname.get(1).add(it2.next());
        }
        doctorbox.getItems().addAll(doctorname.get(0));
        errorlabel.setVisible(false);
        db.closeConnection();
    }
    
    public void setData(DoctorWindowController d, boolean a, boolean e, boolean r, PrescriptionTable sp) {
        parentcontroller = d;
        addflag = a;
        editflag = e;
        removeflag = r;
        
        if (addflag) {
            selectedprescription = null;
        } else {
            selectedprescription = sp;
        }
        
        add.setVisible(addflag);
        edit.setVisible(editflag);
        remove.setVisible(removeflag);
        
        if (addflag) {
            prescription.clear();
            treatment.clear();
            dateyear.clear();
            datemonth.clear();
            dateday.clear();
            length.clear();
            doctorbox.setValue(null);
        }
        if (editflag) {
            prescription.setText(selectedprescription.getPrescription());
            treatment.setText(selectedprescription.getTreatment());
            String[] date = selectedprescription.getIssuedate().split("-");
            dateyear.setText(date[0]);
            datemonth.setText(date[1]);
            dateday.setText(date[2]);
            length.setText(selectedprescription.getLength());
            doctorbox.setValue(selectedprescription.getPrescriber());
        }
        if (removeflag) {
            prescription.setVisible(false);
            treatment.setVisible(false);
            dateyear.setVisible(false);
            datemonth.setVisible(false);
            dateday.setVisible(false);
            length.setVisible(false);
            doctorbox.setVisible(false);
            datelabel.setVisible(false);
            errorlabel.setText("Remove " + selectedprescription.getPrescription() + " prescription?");
            errorlabel.setVisible(true);
        }
    }
}