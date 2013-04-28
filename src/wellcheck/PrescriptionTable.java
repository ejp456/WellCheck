/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package wellcheck;

import javafx.beans.property.SimpleStringProperty;

/**
*
* @author Kent
*/
public class PrescriptionTable {
    private SimpleStringProperty prescription = new SimpleStringProperty("");
    private SimpleStringProperty treatment = new SimpleStringProperty("");
    private SimpleStringProperty prescriber = new SimpleStringProperty("");
    private SimpleStringProperty issuedate = new SimpleStringProperty("");
    private SimpleStringProperty length = new SimpleStringProperty("");
    private int id;
    
    public PrescriptionTable()
    {
        this("","","","","", 0);
    }
    
    public PrescriptionTable(String prescription, String treatment, String prescriber, String issuedate, String length, int id)
    {
        setPrescription(prescription);
        setTreatment(treatment);
        setPrescriber("Dr. " + prescriber);
        setIssuedate(issuedate);
        setLength(length);
        this.id = id;
    }
    
    public PrescriptionTable(Prescription p)
    {
        setPrescription(p.getPrescription());
        setTreatment(p.getTreatment());
        setPrescriber("Dr. " + p.getDoctorname());
        setIssuedate(p.getDate());
        setLength("" + p.getLength());
        id = p.getId();
    }

    public String getPrescription() {
        return prescription.get();
    }

    public String getTreatment() {
        return treatment.get();
    }

    public String getPrescriber() {
        return prescriber.get();
    }

    public String getIssuedate() {
        return issuedate.get();
    }

    public String getLength() {
        return length.get();
    }

    public int getId() {
        return id;
    }

    public void setPrescription(String p) {
        prescription.set(p);
    }

    public void setTreatment(String t) {
        treatment.set(t);
    }

    public void setPrescriber(String p) {
        prescriber.set(p);
    }

    public void setIssuedate(String i) {
        issuedate.set(i);
    }

    public void setLength(String l) {
        length.set(l);
    }

    public void setId(int id) {
        this.id = id;
    }
}