module com.example.hospitals {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens project.screen.hospitals to javafx.fxml;
    exports project.screen.hospitals;
    exports project.screen.ambulance;
    opens project.screen.ambulance to javafx.fxml;
    exports project.screen.dailyrecords;
    opens project.screen.dailyrecords to javafx.fxml;
    exports project.screen.userData;
    opens project.screen.userData to javafx.fxml;
}