package project.screen.dailyrecords;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DailyrecApplication {
    public DailyrecApplication(Stage stage)throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(DailyrecApplication.class.getResource("dailyrecords-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 500);
            stage.setTitle("Ambulance Management");
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
