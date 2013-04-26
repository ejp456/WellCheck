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
public class dataTable {
   private final SimpleStringProperty Blood = new SimpleStringProperty("");
   private final SimpleStringProperty weight = new SimpleStringProperty("");
   private final SimpleStringProperty sugar = new SimpleStringProperty("");
   private final SimpleStringProperty date = new SimpleStringProperty("");
   private final SimpleStringProperty comments = new SimpleStringProperty("");
public dataTable() {
        this("", "","","","");
    }    

public dataTable(String Blood, String weight,String sugar, String date,String comments){
    setBlood(Blood);
    setWeight(weight);
    setSugar(sugar);
    setDate(date);
    setComments(comments);
    }
public void setBlood(String p){
    Blood.set(p);
    }
public void setWeight(String d){
    weight.set(d);
    }
public void setSugar(String p){
    sugar.set(p);
    }
public void setDate(String d){
    date.set(d);
    }
public void setComments(String p){
    comments.set(p);
    }
public String getBlood(){
    return Blood.get();
    }
public String getWeight(){
    return weight.get();
    }
public String getSugar(){
    return sugar.get();
    }
public String getDate(){
    return date.get();
    }
public String getComments(){
    return comments.get();
    }

}
