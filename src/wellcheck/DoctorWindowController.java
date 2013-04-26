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
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
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
    public static ObservableList<PatientTable> patientList;
    @FXML
    public static ObservableList<String> comboList;
    @FXML
    public static ObservableList<dataTable> dataList;
    @FXML
    private TableView patientTable;
    @FXML
    public static ComboBox patientDropDown;
    public static ArrayList<dataTable> data;
    public static Database db = new Database();
    @FXML
    private ObservableList<PrescriptionTable> prescriptionList;
    @FXML
    private ComboBox prescriptioncombobox;
    @FXML
    private TableView<PrescriptionTable> prescriptionTable;
    @FXML
    private Label prescriptionerror;
    @FXML
    private Button addprescriptionbutton, editprescriptionbutton, removeprescriptionbutton;
    int selectedpatientid;
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
    protected void showGraph(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LineChart.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    /*
     * Kent's method
     * Brings up the Add Dependant Window
     */

    @FXML
    protected void addDependant(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DependantWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
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
     * 
     */
    @FXML
    protected void initializePrescriptionTab() {
        db.Connect();
        //Queries the database for patient names
        List<List> rlist = db.dbQuery("Select FirstName, LastName FROM users WHERE usertype = 'Patient'");
        ArrayList<String> patientname = new ArrayList();
        Iterator it1 = rlist.iterator();
        Iterator it2;
        while (it1.hasNext()) {
            it2 = ((List) it1.next()).iterator();
            patientname.add((String) it2.next() + " " + (String) it2.next());
        }

        //Fills the combobox with patient names
        prescriptioncombobox.getItems().addAll(patientname);
        prescriptionerror.setVisible(false);
        addprescriptionbutton.setDisable(true);
        editprescriptionbutton.setDisable(true);
        removeprescriptionbutton.setDisable(true);

        db.closeConnection();
    }

    /*
     * prescriptionComboBoxChanged fills the table with a patient's prescriptions
     * when a patient is chosen from the combobox.
     */
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

    /*
     * updatePrescriptionTable refreshes the table with new data whenever a new
     * patient is selected, or the prescription information is added, edited, or removed.
     */
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

        //This gets the controller for the AddPrescriptionWindow, so that
        //it can be passed the DoctorWindowController's instance
        AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

        //Gets the prescription information from a selected item in the table
        PrescriptionTable selectedprescription = prescriptionTable.getSelectionModel().getSelectedItem();
        //Flags passed to AddPrescriptionWindowController based on whether
        //the add, edit, or remove buttons are pushed
        boolean addflag, editflag, removeflag;
        addflag = true;
        editflag = false;
        removeflag = false;
        //Passing all the data to the AddPrescriptionWindowController
        controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

        stage.show();
    }

    @FXML
    protected void editPrescription() throws Exception {
        //Gets prescription selection from table;
        //If null, displays an error message,
        //Else, brings up edit prescription window
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

            //This gets the controller for the AddPrescriptionWindow, so that
            //it can be passed the DoctorWindowController's instance
            AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

            //Flags passed to AddPrescriptionWindowController based on whether
            //the add, edit, or remove buttons are pushed
            boolean addflag, editflag, removeflag;
            addflag = false;
            editflag = true;
            removeflag = false;
            //Passing all the data to the AddPrescriptionWindowController
            controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

            stage.show();
        }
    }

    @FXML
    protected void removePrescription() throws Exception {
        //Gets prescription selection from table;
        //If null, displays an error message,
        //Else, brings up edit prescription window
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

            //This gets the controller for the AddPrescriptionWindow, so that
            //it can be passed the DoctorWindowController's instance
            AddPrescriptionWindowController controller = fxmlLoader.<AddPrescriptionWindowController>getController();

            //Flags passed to AddPrescriptionWindowController based on whether
            //the add, edit, or remove buttons are pushedboolean addflag, editflag, removeflag;
            boolean addflag, editflag, removeflag;
            addflag = false;
            editflag = false;
            removeflag = true;
            //Passing all the data to the AddPrescriptionWindowController
            controller.setData(getDoctorWindowController(), addflag, editflag, removeflag, selectedprescription);

            stage.show();
        }
    }

    //Returns the current instance of DoctorWindowController
    //Useful to pass information back and forth between windows
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

    public static void refresh() {
        db.Connect();
        dataList.clear();
        String name = (String) patientDropDown.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);
        data = db.dataTable(tokens[0], tokens[1]);
        dataList.addAll(data);
        db.closeConnection();
    }

    public static String getSelectedPatient() {
        String name = (String) patientDropDown.getSelectionModel().getSelectedItem();
        String delims = "[ ]";
        String[] tokens = name.split(delims);

        return tokens[0];
    }

    public static String getPatientName() {
        String name = (String) patientDropDown.getSelectionModel().getSelectedItem();
        return name;
    }
}
