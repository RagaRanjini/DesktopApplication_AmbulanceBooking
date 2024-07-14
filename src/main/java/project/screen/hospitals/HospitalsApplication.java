package project.screen.hospitals;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.common.files.DataSource;

import java.io.IOException;

public class HospitalsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HospitalsApplication.class.getResource("hospitals-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 500);
        stage.setTitle("Ambulance Management");
        stage.setScene(scene);
        stage.show();

        DataSource dataSource = new DataSource(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}