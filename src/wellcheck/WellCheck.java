/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wellcheck;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;

/**
 *
 * @author Etai
 */
public class WellCheck extends Application {
    public static String screenID1 = "login";
    public static String screenFile1 = "Sample.fxml";
    public static String screenID2 = "DoctorWindow";
    public static String screenFile2 = "DoctorWindow.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        ScreenController mainContainer = new ScreenController();
        //adds screens to GUI
        mainContainer.loadScreen(WellCheck.screenID1, WellCheck.screenFile1);
         //Kent's change:
         //This method call is disabled. The loading of DoctorWindow now
         //happens in the SampleController class, so that it's possible to
         //set data for the DoctorWindowController instance on login,
         //without resorting to setting the relevant data members to public
         //static.
        //mainContainer.loadScreen(WellCheck.screenID2, WellCheck.screenFile2);
        
        mainContainer.setScreen(WellCheck.screenID1);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        //Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
        
        //shows screen
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
      
    }
}