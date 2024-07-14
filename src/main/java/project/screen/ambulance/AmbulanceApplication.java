package project.screen.ambulance;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AmbulanceApplication {
    private static String thisHospitalID;
    private static String thisHospitalName;
    public AmbulanceApplication(Stage stage, String hospitalId, String hospitalName) throws IOException {
        try {
            thisHospitalID = hospitalId;
            thisHospitalName = hospitalName;
            FXMLLoader fxmlLoader = new FXMLLoader(AmbulanceApplication.class.getResource("ambulance-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 500);
            stage.setTitle("Ambulance Management");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static String getThisHospitalID(){
        return thisHospitalID;
    }
    public  static  String getThisHospitalname(){
        return thisHospitalName;
    }
}
