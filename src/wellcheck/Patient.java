/*
 * Class created by Kent Ehrlich
 * 
 * These are classes with limited utility, mostly to make it convenient
 * to add or remove things from the database. They were created with the
 * expectation they would have more utility than they currently do.
 */
package wellcheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Kent
 */
public class Patient extends User {

    private String address1;
    private String address2;
    private String city;
    private String state;
    private int zipcode;
    private String phonenumber;
    private String insprovider;
    private int memberid;
    private int groupid;
    private int dependantid;
    private int doctorid;
    private ArrayList<Record> patientrecord;
    private ArrayList<Prescription> patientprescription;

    Patient() {
        super();
        address1 = null;
        address2 = null;
        city = null;
        state = null;
        zipcode = 0;
        phonenumber = null;
        insprovider = null;
        memberid = 0;
        groupid = 0;
        dependantid = 0;
        doctorid = 0;
        patientrecord = null;
        patientprescription = null;
    }

    Patient(int userid, String username, String password,
            String usertype, String firstname, String lastname,
            String dateofbirth, String secretquestion, String secretanswer,
            String address1, String address2, String city, String state, int zipcode,
            String phonenumber, String insprovider, int memberid,
            int groupid, int dependantid, int doctorid,
            ArrayList<Record> patientrecord,
            ArrayList<Prescription> patientprescription) {
        super(userid, username, password, usertype, firstname, lastname,
                dateofbirth, secretquestion, secretanswer);
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phonenumber = phonenumber;
        this.insprovider = insprovider;
        this.memberid = memberid;
        this.groupid = groupid;
        this.dependantid = dependantid;
        this.doctorid = doctorid;
        this.patientrecord = new ArrayList<>(patientrecord);
        this.patientprescription = new ArrayList<>(patientprescription);
    }

    Patient(Patient p) {
        super(p);
        address1 = p.address1;
        address2 = p.address2;
        city = p.city;
        state = p.state;
        zipcode = p.zipcode;
        phonenumber = p.phonenumber;
        insprovider = p.insprovider;
        memberid = p.memberid;
        groupid = p.groupid;
        dependantid = p.dependantid;
        doctorid = p.doctorid;
        patientrecord = new ArrayList<>(p.getPatientrecord());
        patientprescription = new ArrayList<>(p.getPatientprescription());

    }

    //Instantiates an Patient object from data in the database,
    //including prescriptions and health records associated with the patient.
    //Extremely slow performance
    static Patient getRow(int userid) {
        Database db = DoctorWindowController.db;
        db.Connect();
        Patient returnpatient = new Patient();

        String querystring = "Select * FROM users WHERE userid = " + userid;
        List<List> list = db.dbQuery(querystring);
        Iterator it = list.get(0).iterator();

        returnpatient.setUserid((int) (Integer) it.next());
        returnpatient.setUsername((String) it.next());
        returnpatient.setPassword((String) it.next());
        returnpatient.setUsertype((String) it.next());
        returnpatient.setFirstname((String) it.next());
        returnpatient.setLastname((String) it.next());
        returnpatient.setDateofbirth(((Date) it.next()).toString());
        returnpatient.setSecretquestion((String) it.next());
        returnpatient.setSecretanswer((String) it.next());

        querystring = "Select * FROM Patient WHERE userid = " + userid;
        list = db.dbQuery(querystring);
        it = list.get(0).iterator();

        returnpatient.setUserid((Integer) it.next());
        returnpatient.setAddress1((String) it.next());
        returnpatient.setAddress2((String) it.next());
        returnpatient.setCity((String) it.next());
        returnpatient.setState((String) it.next());
        returnpatient.setZipcode((int) (Integer) it.next());
        returnpatient.setPhonenumber((String) it.next());
        returnpatient.setInsprovider((String) it.next());
        returnpatient.setMemberid((Integer) it.next());
        returnpatient.setGroupid((Integer) it.next());
        returnpatient.setDependantid((int) (Integer) it.next());
        returnpatient.setDoctorid((int) (Integer) it.next());
        returnpatient.setPatientrecord(new ArrayList<Record>());
        returnpatient.setPatientprescription(new ArrayList<Prescription>());

        querystring = "Select id FROM Records WHERE PatientID = " + userid;
        list = db.dbQuery(querystring);
        Iterator it2;
        it = list.iterator();
        while (it.hasNext()) {
            it2 = ((List) it.next()).iterator();
            returnpatient.getPatientrecord().add(Record.getRecordRow((Integer) it2.next()));
        }

        querystring = "Select id FROM Prescriptions WHERE Patient = " + userid;
        list = db.dbQuery(querystring);
        it = list.iterator();
        while (it.hasNext() && !list.isEmpty()) {
            it2 = ((List) it.next()).iterator();
            returnpatient.getPatientprescription().add(Prescription.getPrescriptionRow((Integer) it2.next()));
        }

        db.closeConnection();
        return returnpatient;
    }

    static void insertRow(Patient insertpatient) {
        Database db = DoctorWindowController.db;
        db.Connect();

        //Checks to see if the object is already in the database
        //Naively assumes that if userid =/= 0 the object is already inside
        if (insertpatient.getUserid() != 0) {
            System.out.println("User already in database");
            return;
        }

        String qstring = "Select userid FROM users";
        List<List> plist = db.dbQuery(qstring);

        //This loop finds the location to insert the object into the database
        int j = 1;
        for (int i = 0; i < plist.size() && j == (Integer) plist.get(i).get(0); i++) {
            j++;
        }

        insertpatient.setUserid(j);

        qstring = "Insert INTO users (userid, username, password, usertype, "
                + "FirstName, LastName, DOB, secretquestion, "
                + "secretanswer) "
                + "VALUES ("
                + "" + insertpatient.getUserid() + ", "
                + "'" + insertpatient.getUsername() + "', "
                + "'" + insertpatient.getPassword() + "', "
                + "'" + insertpatient.getUsertype() + "', "
                + "'" + insertpatient.getFirstname() + "', "
                + "'" + insertpatient.getLastname() + "', "
                + "'" + insertpatient.getDateofbirth() + "', "
                + "'" + insertpatient.getSecretquestion() + "', "
                + "'" + insertpatient.getSecretanswer() + "')";

        db.updateDB(qstring);

        qstring = "Insert INTO Patient (address1, address2, city, state, zipcode, phonenumber, insprovider, memberid, groupid, dependantid, doctorid) "
                + "VALUES ("
                + insertpatient.getAddress1() + ", "
                + insertpatient.getAddress2() + ", "
                + insertpatient.getCity() + ", "
                + insertpatient.getState() + ", "
                + insertpatient.getZipcode() + ", "
                + insertpatient.getPhonenumber() + ", "
                + insertpatient.getInsprovider() + ", "
                + insertpatient.getMemberid() + ", "
                + insertpatient.getGroupid() + ", "
                + insertpatient.getDependantid() + ", "
                + insertpatient.getDoctorid() + ")";
        db.updateDB(qstring);

        for (int i = 0; i < insertpatient.getPatientrecord().size(); i++) {
            Record.insertRecordRow(insertpatient.getPatientrecord().get(i));
        }

        for (int i = 0; i < insertpatient.getPatientprescription().size(); i++) {
            Prescription.insertPrescriptionRow(insertpatient.getPatientprescription().get(i));
        }

        db.closeConnection();

    }

    //Modifies and existing entry in the database
    static void updateRow(Patient p) {
        User.updateRow(p);

        Database db = DoctorWindowController.db;
        db.Connect();

        String qstring = "UPDATE Patient SET "
                + "Address1 = '" + p.getAddress1() + "', "
                + "Address2 = '" + p.getAddress2() + "', "
                + "City = '" + p.getCity() + "', "
                + "State = '" + p.getState() + "', "
                + "ZipCode = '" + p.getZipcode() + "', "
                + "PhoneNumber = '" + p.getPhonenumber() + "', "
                + "InsuranceProvider = '" + p.getInsprovider() + "', "
                + "memberid = '" + p.getMemberid() + "', "
                + "groupid = '" + p.getGroupid() + "', "
                + "DependantTo = '" + p.getDependantid() + "', "
                + "Doctor = '" + p.getDoctorid() + "', ";
        db.updateDB(qstring);
        db.closeConnection();

    }

    //Deletes a patient from the database, including all information from the
    //users, Patient, Records, and Prescriptions tables
    static void deleteRow(Patient p) {

        Database db = DoctorWindowController.db;
        db.Connect();

        String qstring = "DELETE FROM users WHERE userid = " + p.getUserid();
        db.updateDB(qstring);
        qstring = "DELETE FROM Patient WHERE userid = " + p.getUserid();
        db.updateDB(qstring);
        qstring = "DELETE FROM Records WHERE Patient = " + p.getUserid();
        db.updateDB(qstring);
        qstring = "DELETE FROM Prescriptions WHERE PatientID = " + p.getUserid();
        db.updateDB(qstring);

        db.closeConnection();
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String State) {
        this.state = State;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setInsprovider(String insprovider) {
        this.insprovider = insprovider;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public void setDependantid(int dependantid) {
        this.dependantid = dependantid;
    }

    public void setDoctorid(int doctorid) {
        this.doctorid = doctorid;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getInsprovider() {
        return insprovider;
    }

    public int getMemberid() {
        return memberid;
    }

    public int getGroupid() {
        return groupid;
    }

    public int getDependantid() {
        return dependantid;
    }

    public int getDoctorid() {
        return doctorid;
    }

    public ArrayList<Record> getPatientrecord() {
        return patientrecord;
    }

    public ArrayList<Prescription> getPatientprescription() {
        return patientprescription;
    }

    public void setPatientrecord(ArrayList<Record> patientrecord) {
        this.patientrecord = patientrecord;
    }

    public void setPatientprescription(ArrayList<Prescription> patientprescription) {
        this.patientprescription = patientprescription;
    }
}
