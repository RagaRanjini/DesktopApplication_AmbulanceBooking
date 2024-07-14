package project.common.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.screen.dailyrecords.DailyRecordsData;
import project.screen.dailyrecords.DailyrecController;
import project.screen.hospitals.HospitalData;
import project.screen.hospitals.HospitalsController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Methods {
    private static Methods methods = new Methods();
    public static Methods getMethodsInstance(){
        return methods;
    }
    public String sqlStatement;
    public void globalSearch(String searchValue, VBox dataVBox, String tableName, String columnName){
        if(Objects.equals(tableName, GlobalVariables.Hospital_Table)){
            if(searchValue.isEmpty()){
                sqlStatement = "SELECT * FROM " + tableName;
            }else{
                sqlStatement = "SELECT * FROM " + tableName + " WHERE LOWER(" + columnName + ") LIKE ?";
            }
        }else if(Objects.equals(tableName, GlobalVariables.Daily_Record)){
            if(searchValue.isEmpty()){
                sqlStatement ="select * from "+GlobalVariables.Daily_Record+", "+GlobalVariables.Hospital_Table+" where Hospital_Id = Id";
            }else{
                sqlStatement ="select * from "+GlobalVariables.Daily_Record+", "+GlobalVariables.Hospital_Table+" where Hospital_Id = Id AND LOWER(" + columnName + ") LIKE ?";
            }
        }
        try(PreparedStatement preparedStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(sqlStatement)){
            if(!searchValue.isEmpty()){
                preparedStatement.setString(1, "%" + searchValue + "%");
            }
            dataVBox.getChildren().clear();
            ObservableList<Data> dataList = null;
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                dataList = FXCollections.observableArrayList();
                if(Objects.equals(tableName, GlobalVariables.Hospital_Table)){
                    while(resultSet.next()){
                        String hospitalName = resultSet.getString(GlobalVariables.Hospital_Name_Column);
                        String hospitalContact = resultSet.getString(GlobalVariables.Contact_Column);
                        String hospitalLocation = resultSet.getString(GlobalVariables.Location_Column);
                        dataList.add(new HospitalData(hospitalName, hospitalContact, hospitalLocation));
                    }
                    HospitalsController.getHospitalsController().bindDataToVBox(dataList, dataVBox);
                }else if(Objects.equals(tableName, GlobalVariables.Daily_Record)){
                    while(resultSet.next()){
                        String DailyDate = resultSet.getString(GlobalVariables.Date_Column);
                        String DailyPatient = resultSet.getString(GlobalVariables.Patient_Name_Column);
                        String DailyContact = resultSet.getString(GlobalVariables.Daily_Contact_Column);
                        String DailyTripStart = resultSet.getString(GlobalVariables.Trip_Start_Column);
                        String DailyTripEnd = resultSet.getString(GlobalVariables.Trip_End_Column);
                        String HospitalId = resultSet.getString(GlobalVariables.Hospital_Name_Column);
                        int AmbulanceId = resultSet.getInt(GlobalVariables.Ambulance_Column);
                        String Ambid = String.valueOf(AmbulanceId);
                        String DriverId = resultSet.getString(GlobalVariables.Driver_Column);
                        String AttenderId = resultSet.getString(GlobalVariables.Attender_Column);

                        dataList.add(new DailyRecordsData(DailyDate, DailyPatient, DailyContact, DailyTripStart, DailyTripEnd, HospitalId, Ambid, DriverId, AttenderId));
                    }
                    DailyrecController.getdailyRecController().bindDataToDailyVBox(dataList, dataVBox);
                }
            }
        }catch (SQLException e){
            System.out.println("Exception while performing search operation: "+e.getMessage());
        }
    }

    public void triggerAlert(String alertText, String alertImageURL){
        Dialog<ButtonType> alertDialog = new Dialog<>();
        alertDialog.setTitle("System Alert");
        HBox alertHBox = new HBox();
        ImageView alertImage = new ImageView(alertImageURL);
        alertImage.setFitWidth(45);
        alertImage.setFitHeight(45);
        Label alert = new Label(alertText);
        alert.setStyle("-fx-padding: 10px 0 0 0;");
        alertHBox.getChildren().addAll(alertImage, alert);
        alertDialog.getDialogPane().setContent(alertHBox);
        alertDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        alertHBox.setSpacing(20.0);
        alertDialog.showAndWait();
    }
}
