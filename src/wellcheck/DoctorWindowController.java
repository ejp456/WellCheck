/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Etai
 */
public class DoctorWindowController implements Initializable, ControlledScreen {

    @FXML
    public static ObservableList<PatientTable> patientList;
    @FXML
    private TableView patientTable;
    @FXML
    TableView prescriptionTable;
    @FXML
    private TableColumn patient, doctor, prescription, treatment, doctorname, date, length;
    @FXML
    ComboBox prescriptioncombobox;
    @FXML
    private Label prescriptionerror;
    @FXML
    private Button addprescriptionbutton, editprescriptionbutton, removeprescriptionbutton;
    private Database db = new Database();
    private ResultSet rs;
    User currentuser;
    Patient selectedpatient;
    ScreenController myController;

    @FXML
    protected void addPatientButton(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AddPatientWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void logOut(ActionEvent event) {
        patientList.clear();
        myController.setScreen(WellCheck.screenID1);
    }

    @FXML
    protected void editPatient(ActionEvent event) throws Exception {
        //patientList.add(new PatientTable("girl", "doctor Koo"));
        Parent root = FXMLLoader.load(getClass().getResource("EditPatientInformation.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void newEntry(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("addNewEntry.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void removePatient(ActionEvent event) {
        db.Connect();
        PatientTable patient = (PatientTable) patientTable.getSelectionModel().getSelectedItem();
        String name[] = patient.getPatient().split(" ");
        if (name.length >= 2) {
            patientList.remove(patient);
            db.deleteUser(name[0], name[1]);
        }
    }

    @FXML
    protected void addDependant(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DependantWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializePrescriptionTab();
    }

    /*Kent's Methods
     * The Prescription tab methods starts here;
     */
    @FXML
    protected void initializePrescriptionTab() {
        db.Connect();
        List<List> rlist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList();
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            patientname.add((String) it2.next() + " " + (String) it2.next());
        }
        prescriptioncombobox.getItems().addAll(patientname);
        prescriptionerror.setVisible(false);
        addprescriptionbutton.setDisable(true);
        editprescriptionbutton.setDisable(true);
        removeprescriptionbutton.setDisable(true);

        db.closeConnection();
    }

    @FXML
    protected void prescriptionComboBoxChanged(ActionEvent event) {
        db.Connect();
        String[] patientname = ((String) prescriptioncombobox.getValue()).split("\\s+");

        List<List> rlist;
        rlist = db.dbQuery("SELECT userid FROM users WHERE FirstName = '" + patientname[0] + "' AND LastName = '" + patientname[1] + "'");

        int pid = (Integer) rlist.get(0).get(0);
        if (pid == -1) {
            prescriptionerror.setText("Patient not found.");
            prescriptionerror.setVisible(true);
        }

        selectedpatient = Patient.getRow(pid);
        ArrayList prlist = selectedpatient.getPatientprescription();
        updatePrescriptionTable();
        db.closeConnection();

        System.out.println(selectedpatient.getUserid());
    }

    void updatePrescriptionTable() {
        ArrayList prlist = selectedpatient.getPatientprescription();
        if (prlist.isEmpty()) {
            prescriptionerror.setText("Patient has no prescriptions.");
            prescriptionerror.setVisible(true);
            addprescriptionbutton.setDisable(false);
        } else {
            prescriptionTable.getItems().addAll(prlist);
            prescriptionerror.setVisible(false);
            addprescriptionbutton.setDisable(false);
            editprescriptionbutton.setDisable(false);
            removeprescriptionbutton.setDisable(false);
        }
    }

    @FXML
    protected void addPrescription() throws Exception {
        URL location = getClass().getResource("AddPrescriptionWindow.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) fxmlLoader.load(location.openStream());


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

        controller.setData(selectedpatient, getDoctorWindowController());

        stage.show();
    }

    @FXML
    protected void editPrescription() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("EditPrescriptionWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void removePrescription() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("RemovePrescription.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public DoctorWindowController getDoctorWindowController()
    {
        return this;
    }

    /*End Prescription tab methods*/
    public void setScreenParent(ScreenController screenParent) {
        myController = screenParent;
    }

    public static void addPatient(String p, String d) {
        patientList.add(new PatientTable(p, d));
    }
}
