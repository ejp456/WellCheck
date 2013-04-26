package wellcheck;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Kent
 *
 * Prescription class for prescription table
 */
public class Prescription {

    private int id;
    private int patientid;
    private int doctorid;
    private String doctorname;
    private String prescription;
    private String treatment;
    private String date;
    private int length;

    public Prescription() {
        id = 0;
        patientid = 0;
        doctorid = 0;
        doctorname = null;
        prescription = null;
        treatment = null;
        date = null;
        length = 0;
    }

    public Prescription(int id, int patientid, int doctorid, String doctorname, String prescription,
            String treatment, String date, int length) {
        this.id = id;
        this.patientid = patientid;
        this.doctorid = doctorid;
        this.prescription = prescription;
        this.treatment = treatment;
        this.date = date;
        this.length = length;
    }

    public Prescription(Prescription p) {
        id = p.id;
        patientid = p.patientid;
        doctorid = p.doctorid;
        doctorname = p.doctorname;
        prescription = p.prescription;
        treatment = p.treatment;
        date = p.date;
        length = p.length;
    }

    static Prescription getPrescriptionRow(int id) {
        Database db = new Database();
        db.Connect();
        Prescription returnprescription = new Prescription();


        List<List> list = db.dbQuery("Select * FROM Prescriptions WHERE id = " + id);
        Iterator pit = list.get(0).iterator();

        returnprescription.setId((int) (Integer) pit.next());
        returnprescription.setPatientid((int) (Integer) pit.next());
        returnprescription.setDoctorid((int) (Integer) pit.next());
        returnprescription.setPrescription((String) pit.next());
        returnprescription.setTreatment((String) pit.next());
        returnprescription.setDate(((Date) pit.next()).toString());
        returnprescription.setLength((int) (Integer) pit.next());

        list = db.dbQuery("SELECT LastName FROM users WHERE userid = '" + returnprescription.getDoctorid() + "'");
        returnprescription.setDoctorname((String) list.get(0).get(0));

        db.closeConnection();
        return returnprescription;
    }

    static void insertPrescriptionRow(Prescription p) {
        Database db = new Database();
        db.Connect();

        //Checks to see if the object is already in the database
        //Naively assumes that if p.id != 0 the object is already inside
        if (p.getId() != 0) {
            return;
        }
        List<List> plist = db.dbQuery("Select id FROM Prescriptions");
        //This loop finds the location to insert the object into the database
        int j = 1;
        if (!plist.isEmpty()) {
            for (int i = 0; i < plist.size() && j == (Integer) plist.get(i).get(0); i++) {
                j++;
            }
        }
        p.setId(j);

        String qstring = "Insert INTO Prescriptions (id, Patient, Doctor, Prescription, Treatment, Date, Length)"
                + "VALUES ("
                + "" + p.getId() + ", "
                + "'" + p.getPatientid() + "', "
                + "'" + p.getDoctorid() + "', "
                + "'" + p.getPrescription() + "', "
                + "'" + p.getTreatment() + "', "
                + "'" + p.getDate() + "', "
                + "'" + p.getLength() + "')";
        db.updateDB(qstring);
        db.closeConnection();
    }

    //This method deletes a user from the database by userid
    static void deletePrescriptionRow(Prescription p) {
        Database db = new Database();
        db.Connect();
        String qstring = "DELETE FROM Prescriptions WHERE id = " + p.getId();
        db.updateDB(qstring);
        db.closeConnection();
    }

    //This method modifies an existing database entry
    static void updatePrescriptionRow(Prescription p) {
        Database db = new Database();
        db.Connect();
        String qstring = "UPDATE Prescriptions SET "
                + "Patient = '" + p.getPatientid() + "', "
                + "Doctor = '" + p.getDoctorid() + "', "
                + "Prescription = '" + p.getPrescription() + "', "
                + "Treatment = '" + p.getTreatment() + "', "
                + "Date = '" + p.getDate() + "', "
                + "Length = '" + p.getLength() + "' "
                + "WHERE id = " + p.getId();
        db.updateDB(qstring);
        db.closeConnection();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientid(int patientid) {
        this.patientid = patientid;
    }

    public void setDoctorid(int doctorid) {
        this.doctorid = doctorid;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public int getPatientid() {
        return patientid;
    }

    public int getDoctorid() {
        return doctorid;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getDate() {
        return date;
    }

    public int getLength() {
        return length;
    }
}
