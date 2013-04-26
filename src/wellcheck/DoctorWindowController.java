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
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 *
 * @author Etai
 */
public class DoctorWindowController implements Initializable, ControlledScreen {

    @FXML
    public static ObservableList<String> comboList;
    @FXML
    private ObservableList<dataTable> dataList;
    @FXML
    private ObservableList<PrescriptionTable> prescriptionList;
    @FXML
    private ComboBox patientDropDown, prescriptioncombobox;
    @FXML
    public static ObservableList<PatientTable> patientList;
    @FXML
    private TableView patientTable;
    @FXML
    private TableView<PrescriptionTable> prescriptionTable;
    @FXML
    private Label prescriptionerror;
    @FXML
    private Button addprescriptionbutton, editprescriptionbutton, removeprescriptionbutton;
    private Database db = new Database();
    private ResultSet rs;
    int selectedpatientid;
    //User currentuser;
    //Patient selectedpatient;
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
            comboList.remove(name[0] + " " + name[1]);
            db.deleteUser(name[0], name[1]);
        }
    }

    @FXML
    protected void dataComboBox(ActionEvent event) {
        db.Connect();
        dataList.clear();
        String name = (String) patientDropDown.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);
        ArrayList<dataTable> data = db.dataTable(tokens[0], tokens[1]);
        dataList.addAll(data);
        db.closeConnection();
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

        db = new Database();
        db.Connect();
        patientDropDown.getItems().clear();

        List<List> plist = db.dbQuery("SELECT FirstName, LastName, Patient.Doctor FROM users JOIN Patient ON (users.userid = Patient.userid)");
        ArrayList<String> patientname = new ArrayList(plist.size());

        for (int i = 0; i < plist.size(); i++) {
            patientname.add((String) plist.get(i).get(0) + " " + (String) plist.get(i).get(1));
        }

        ObservableList<String> olist = FXCollections.observableList(patientname);
        patientDropDown.getItems().addAll(olist);

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

        selectedpatientid = pid;
        updatePrescriptionTable();
        db.closeConnection();
    }

    void updatePrescriptionTable() {
        Database db = new Database();
        db.Connect();
        List<List> list = db.dbQuery("SELECT Prescriptions.*, users.LastName FROM Prescriptions, users WHERE Prescriptions.Patient = " + selectedpatientid + " AND Prescriptions.Doctor = users.userid");
        List<PrescriptionTable> templist = new ArrayList();
        prescriptionList.clear();

        if (list.isEmpty()) {
            prescriptionerror.setText("Patient has no prescriptions.");
            prescriptionerror.setVisible(true);
            addprescriptionbutton.setDisable(false);
            editprescriptionbutton.setDisable(true);
            removeprescriptionbutton.setDisable(true);
        } else {
            for (int i = 0; i < list.size(); i++) {
                PrescriptionTable temppt = new PrescriptionTable(
                        (String) list.get(i).get(3),
                        (String) list.get(i).get(4),
                        (String) list.get(i).get(7),
                        ((Date) list.get(i).get(5)).toString(),
                        "" + list.get(i).get(6),
                        (Integer) list.get(i).get(0));
                templist.add(temppt);
            }
            prescriptionList.addAll(templist);
            prescriptionerror.setVisible(false);
            addprescriptionbutton.setDisable(false);
            editprescriptionbutton.setDisable(false);
            removeprescriptionbutton.setDisable(false);
        }

        db.closeConnection();
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

        PrescriptionTable selectedprescription = prescriptionTable.getSelectionModel().getSelectedItem();
        boolean addflag, editflag, removeflag;
        addflag = true;
        editflag = false;
        removeflag = false;
        controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

        stage.show();
    }

    @FXML
    protected void editPrescription() throws Exception {
        PrescriptionTable selectedprescription = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selectedprescription == null) {
            prescriptionerror.setText("Please select a prescription from the table.");
            prescriptionerror.setVisible(true);
        } else {

            URL location = getClass().getResource("AddPrescriptionWindow.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            Parent root = (Parent) fxmlLoader.load(location.openStream());

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

            boolean addflag, editflag, removeflag;
            addflag = false;
            editflag = true;
            removeflag = false;
            controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

            stage.show();
        }
    }

    @FXML
    protected void removePrescription() throws Exception {
        PrescriptionTable selectedprescription = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selectedprescription == null) {
            prescriptionerror.setText("Please select a prescription from the table.");
            prescriptionerror.setVisible(true);
        } else {

            URL location = getClass().getResource("AddPrescriptionWindow.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            Parent root = (Parent) fxmlLoader.load(location.openStream());

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

            boolean addflag, editflag, removeflag;
            addflag = false;
            editflag = false;
            removeflag = true;
            controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

            stage.show();
        }
    }

    public DoctorWindowController getDoctorWindowController() {
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
