/*
 * By Kent Ehrlich
 * This class controls the prescription tab popup windows for adding, editing,
 * and removing prescription information to and from the database and
 * to and from the table in the main UI window.
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
    
    //Adds or edits a prescription entry in the database
    //Once added, updates the prescription table in DoctorWindowController
    @FXML
    protected void addedit(ActionEvent event) {
        String prescrstring = prescription.getText();
        String lengthstring = length.getText();
        String treatstring = treatment.getText();
        String dystring = dateyear.getText();
        String ddstring = dateday.getText();
        String dmstring = datemonth.getText();
        String dname;
        
        //Initialization to nonsense values for error checking
        int doctorid = -1;
        int patientid = -1;
        int lengthint = -1;
        int dyint = -1;
        int ddint = -1;
        int dmint = -1;
        dname = null;
        boolean validentry;
        
        //Retreives doctorid without a database hit;
        //Useful for performance
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
        
        //Begin error checking
        
        //If the fields have valid integers, move on
        //Else, display an error message
        try {
            lengthint = Integer.parseInt(lengthstring);
            dyint = Integer.parseInt(dystring);
            ddint = Integer.parseInt(ddstring);
            dmint = Integer.parseInt(dmstring);
            validentry = true;
        } catch (NumberFormatException e) {
            validentry = false;
        }
        
        //If doctorid is not valid display an error message
        if (!validentry || doctorid == -1) {
            errorlabel.setText("Please enter valid values.");
            errorlabel.setVisible(true);
        }
        //Else if the date is not valid, display an error message
        else if (dyint < 1800 || dyint > 2100 || ddint < 0 || ddint > 31 || dmint < 0 || dmint > 12) {
            errorlabel.setText("Please enter a valid date.");
            errorlabel.setVisible(true);
        }
        //Else if the length is nonsensical, display an error message
        else if (lengthint < 0) {
            errorlabel.setText("Please enter a valid length.");
            errorlabel.setVisible(true);
        }
        //Else if the fields are empty, display an error message
        else if (prescrstring.equals("") || treatstring.equals("")) {
            errorlabel.setText("Please fill all fields.");
            errorlabel.setVisible(true);
        }
        //Else, add/edit the prescription
        else {
            //Creates a Prescription object to utilize static add/edit methods in the class
            Prescription updateprescription = new Prescription(0, patientid, doctorid, dname, prescrstring, treatstring, dyint + "-" + dmint + "-" + ddint, lengthint);
            if (addflag) {
                //Inserts a new entry into the database
                Prescription.insertPrescriptionRow(updateprescription);
            }
            if (editflag) {
                //Edits an existing entry
                updateprescription.setId(selectedprescription.getId());
                Prescription.updatePrescriptionRow(updateprescription);
            }
            
            //Window displays a confirmation screen, and hides all UI elements
            //Except for a button that closes the window
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
            //Updates the table in the main window
            parentcontroller.updatePrescriptionTable();
        }
    }
    
    //Removes an entry selected from the table
    @FXML
    protected void remove(ActionEvent event) throws Exception {
        //Creates a prescription object to utilize static delete method in the class
        Prescription p = new Prescription();
        p.setId(selectedprescription.getId());
        //Deletes the entry from the database
        Prescription.deletePrescriptionRow(p);
        errorlabel.setText("Prescription removed.");
        remove.setVisible(false);
        cancel.setText("Done");
        //Updates the table in the main window
        parentcontroller.updatePrescriptionTable();
    }
    
    //Closes the window
    @FXML
    protected void close(ActionEvent event) throws Exception {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    //Initializes the window
    //Primarily fills the prescriber combobox with names from the database
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Database db = new Database();
        db.Connect();
        //Querying the database for doctors' last names
        List<List> rlist = db.dbQuery("Select LastName, userid FROM users WHERE usertype = 'Doctor'");
        //Creates an array list containing two arraylists
        //One with Doctors' lastnames
        //The other with the corresponding doctor userid in the same index
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
        //Adds the list of doctor names to the doctor combobox
        doctorbox.getItems().addAll(doctorname.get(0));
        errorlabel.setVisible(false);
        db.closeConnection();
    }
    
    //Sets the data needed by the AddPrescriptionWindowController.
    //This method is called and supplied with data from the action buttons
    //for add, edit, and remove prescription from DoctorWindowController
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
        
        //Shows/hides buttons based on whether add, edit, or remove action was called
        add.setVisible(addflag);
        edit.setVisible(editflag);
        remove.setVisible(removeflag);
        
        //If add flag, clear all the text fields and remove any selection in the doctor combobox
        if (addflag) {
            prescription.clear();
            treatment.clear();
            dateyear.clear();
            datemonth.clear();
            dateday.clear();
            length.clear();
            doctorbox.setValue(null);
        }
        //If edit flag, fill fields with information from the table selection from the Main Window
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
        //If remove flag, hide all UI elements except a confirmation message
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