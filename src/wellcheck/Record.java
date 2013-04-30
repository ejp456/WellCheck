/*
 * Class created by Kent Ehrlich
 * 
 * These are classes with limited utility, mostly to make it convenient
 * to add or remove things from the database. They were created with the
 * expectation they would have more utility than they currently do.
 */
package wellcheck;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Kent
 */
public class Record {

    private int id;
    private int patientid;
    private String bloodpressure;
    private double sugarlevel;
    private double weight;
    private String date;
    private String comment;

    public Record() {
    }

    public Record(int id, int patientid, String bloodpressure, double sugarlevel,
            double weight, String date, String comment) {
        this.id = id;
        this.patientid = patientid;
        this.bloodpressure = bloodpressure;
        this.sugarlevel = sugarlevel;
        this.weight = weight;
        this.date = date;
        this.comment = comment;
    }

    public Record(Record r) {
        id = r.id;
        patientid = r.patientid;
        bloodpressure = r.bloodpressure;
        sugarlevel = r.sugarlevel;
        weight = r.weight;
        date = r.date;
        comment = r.comment;
    }

    //This method instantiates an object of the Record class by querying the
    //database.
    static Record getRecordRow(int id) {
        Database db = DoctorWindowController.db;
        db.Connect();
        Record returnrecord = new Record();


        List<List> list = db.dbQuery("Select * FROM Records WHERE id = " + id);
        Iterator it = list.get(0).iterator();

        returnrecord.setId((int) (Integer) it.next());
        returnrecord.setPatientid((int) (Integer) it.next());
        returnrecord.setBloodpressure((String) it.next());
        returnrecord.setSugarlevel((double) (Double) it.next());
        returnrecord.setWeight((double) (Double) it.next());
        returnrecord.setDate(((Date) it.next()).toString());
        returnrecord.setComment((String) it.next());

        db.closeConnection();
        return returnrecord;
    }

    //This method inserts an Record object into the database.
    //To successfully insert, be sure to set id = 0
    static void insertRecordRow(Record r) {
        Database db = DoctorWindowController.db;
        db.Connect();

        //Checks to see if the object is already in the database
        //Naively assumes that if id != 0 the object is already inside
        if (r.getId() != 0) {
            return;
        }
        List<List> list = db.dbQuery("Select id FROM Records");
        //This loop finds the location to insert the object into the database
        int j = 1;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size() && j == (Integer) list.get(i).get(0); i++) {
                j++;
            }
        }
        r.setId(j);

        String qstring = "Insert INTO Records (id, PatientId, BloodPressure, SugarLevel, Weight, Date, Comment)"
                + "VALUES ("
                + "" + r.getId() + ", "
                + "'" + r.getPatientid() + "', "
                + "'" + r.getBloodpressure() + "', "
                + "'" + r.getSugarlevel() + "', "
                + "'" + r.getWeight() + "', "
                + "'" + r.getDate() + "', "
                + "'" + r.getComment() + "')";
        db.updateDB(qstring);
        db.closeConnection();
    }

    //This method modifies an existing entry in the database
    static void updateRecordRow(Record r) {
        Database db = DoctorWindowController.db;
        db.Connect();
        String qstring = "UPDATE Records SET "
                + "PatientId = '" + r.getPatientid() + "', "
                + "BloodPressure = '" + r.getBloodpressure() + "', "
                + "SugarLevel = '" + r.getSugarlevel() + "', "
                + "Weight = '" + r.getWeight() + "', "
                + "Date = '" + r.getDate() + "', "
                + "Comment = '" + r.getComment() + "', "
                + "WHERE id = " + r.getId();
        db.updateDB(qstring);
        db.closeConnection();
    }

    //This method deletes a record from the database by id
    static void deleteRecordRow(Record r) {
        Database db = DoctorWindowController.db;
        db.Connect();
        String qstring = "DELETE FROM Records WHERE id = " + r.getId();
        db.updateDB(qstring);
        db.closeConnection();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBloodpressure(String bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public void setSugarlevel(double sugarlevel) {
        this.sugarlevel = sugarlevel;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getPatientid() {
        return patientid;
    }

    public String getBloodpressure() {
        return bloodpressure;
    }

    public double getSugarlevel() {
        return sugarlevel;
    }

    public double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public void setPatientid(int patientid) {
        this.patientid = patientid;
    }
}
