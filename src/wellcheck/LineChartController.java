/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

/**
 *
 * @author Alan
 */
public class LineChartController implements Initializable {
    
    @FXML private LineChart lineChart;
    private Database db = new Database();
    private String[] array;
    
    private void setArray() {
        db.Connect();
        array = db.getInfo(db.getId(DoctorWindowController.getSelectedPatient()));
        
        String temp;
        
        for (int count = 0; array[count + 1] != null; count++) {
            if (adjustedDate(getDate(array[count])) > adjustedDate(getDate(array[count + 1]))) {
                temp = array[count];
                array[count] = array[count + 1];
                array[count + 1] = temp;
            }
        }
    }
    
    private String getDate(String info) {
        return info.substring(0, 10);
    }
    
    private double getBloodPressure(String info) {
        int index1, index2, count = 0;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index1 = count;
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index2 = count;
        
        return Double.parseDouble(info.substring(index1 + 1, index2));
    }
    
    private double getSugarLevel(String info) {
        int index1, index2, count = 0;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index1 = count;
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index2 = count;
        
        return Double.parseDouble(info.substring(index1 + 1, index2));
    }
    
    private double getWeight(String info) {
        int index1, index2, count = 0;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index1 = count;
        count++;
        
        while (count < info.length() && info.charAt(count) != ' ')
            count++;
        
        index2 = count;
        
        return Double.parseDouble(info.substring(index1 + 1, index2));
    }
    
    private double adjustedDate(String date) {
        int index1, index2, count = 0;
        
        while (count < date.length() && date.charAt(count) != '-') 
            count++;
        
        index1 = count;
        count++;
        
        while (count < date.length() && date.charAt(count) != '-')
            count++;
        
        index2 = count;
        
        return Double.parseDouble(date.substring(index1 + 1, index1 + 3)) + Double.parseDouble(date.substring(index2 + 1, index2 + 3)) / 32;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double date;
        String data;
        
        setArray();
        
        XYChart.Series bloodPressure = new XYChart.Series();
        XYChart.Series sugarLevel = new XYChart.Series();
        XYChart.Series weight = new XYChart.Series();
        
        bloodPressure.setName("Blood Pressure");
        sugarLevel.setName("Sugar Level");
        weight.setName("Weight");
        
        for (int count = 0; array[count] != null; count++) {
            data = array[count];
            date = adjustedDate(getDate(data));
            
            bloodPressure.getData().add(new XYChart.Data(date, getBloodPressure(data)));
            sugarLevel.getData().add(new XYChart.Data(date, getSugarLevel(data)));
            weight.getData().add(new XYChart.Data(date, getWeight(data)));
        }
        
        lineChart.getData().add(bloodPressure);
        lineChart.getData().add(sugarLevel);
        lineChart.getData().add(weight);
    }  
}
