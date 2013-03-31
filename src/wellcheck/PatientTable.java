/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author etaipinckney
 */
public class PatientTable {
   private final SimpleStringProperty patient = new SimpleStringProperty("");
   private final SimpleStringProperty doctor = new SimpleStringProperty("");
public PatientTable() {
        this("", "");
    }    

public PatientTable(String patient, String doctor){
    setPatient(patient);
    setDoctor(doctor);
    }
public void setPatient(String p){
    patient.set(p);
    }
public void setDoctor(String d){
    doctor.set(d);
    }
public String getDoctor(){
    return doctor.get();
    }
public String getPatient(){
    return patient.get();
    }
}
