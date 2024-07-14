package project.common.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.screen.ambulance.AmbulanceApplication;
import project.screen.ambulance.AmbulanceController;
import project.screen.ambulance.AmbulanceData;
import project.screen.dailyrecords.DailyRecordsData;
import project.screen.dailyrecords.DailyrecApplication;
import project.screen.dailyrecords.DailyrecController;
import project.screen.hospitals.HospitalData;
import project.screen.hospitals.HospitalsController;

import java.io.IOException;
import java.sql.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DataSource {
    private static DataSource DBInstance = new DataSource(new Stage());
    public static DataSource getDBInstance(){
        return DBInstance;
    }
    private Stage stage;
    public static String passwordString;
    public DataSource(Stage stage){
        this.stage = stage;
    }
    private Connection DBconnection;
    public Connection getDBconnection(){
        return DBconnection;
    }
//    public static final String DB_NAME = GlobalVariables.DBName;
//    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\JavaProject\\" + DB_NAME;
    public static final String url = "jdbc:oracle:thin:@//localhost:1521/xe";
    public static final String username = "SYSTEM";
    public static final String password = "DB@Oracle";
    public static String sqlStatement;
    public boolean openDB(){
        try{
            DBconnection = DriverManager.getConnection(url, username, password);
//            DBconnection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        }catch (SQLException e) {
            System.out.println(GlobalVariables.DBError + e.getMessage());
            return false;
        }
    }
    public boolean closeDB(){
        try{
            if(DBconnection != null){
                DBconnection.close();
                return true;
            }
            return false;
        }catch (SQLException e){
            System.out.println(GlobalVariables.DBClosureError + e.getMessage());
            return false;
        }
    }
    public static void setHeaderText(Label label, String textValue){
        if(!textValue.isEmpty()){
            label.setText(textValue);
        }
    }
    public void retrieveHospitalsData(VBox hospitalDataVBox, ComboBox locationDropdownBox){
        sqlStatement = "SELECT *" + " FROM " + GlobalVariables.Hospital_Table;
        try (Statement statement = DBconnection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {

            ObservableList<Data> hospitalDataList = FXCollections.observableArrayList();
            Set<String> distinctLocations = new HashSet<>();
            distinctLocations.add("All Locations");

            while (resultSet.next()) {
                String hospitalName = resultSet.getString(GlobalVariables.Hospital_Name_Column);
                String hospitalContact = resultSet.getString(GlobalVariables.Contact_Column);
                String hospitalLocation = resultSet.getString(GlobalVariables.Location_Column);

                hospitalDataList.add(new HospitalData(hospitalName, hospitalContact, hospitalLocation));
                distinctLocations.add(hospitalLocation);
            }

            ObservableList<String> distinctLocationsList = FXCollections.observableArrayList(distinctLocations);
            distinctLocationsList.sort(Comparator.comparing(String::toLowerCase));
            locationDropdownBox.setItems(distinctLocationsList);
            locationDropdownBox.getSelectionModel().select("All Locations");

            HospitalsController.getHospitalsController().bindDataToVBox(hospitalDataList, hospitalDataVBox);
        } catch (SQLException e) {
            System.out.println(GlobalVariables.DBTableError + e.getMessage());
        }
    }

    public void changeHospitalsData(VBox hospitalDataVBox, String thisLocation){
        if(thisLocation.equalsIgnoreCase(GlobalVariables.All_Locations)){
            thisLocation = "[A-Z][a-z]*";
//            sqlStatement = "SELECT *" + " FROM " + GlobalVariables.Hospital_Table+ " WHERE " + GlobalVariables.Location_Column + " GLOB ?";
            sqlStatement = "SELECT * FROM " + GlobalVariables.Hospital_Table + " WHERE REGEXP_LIKE(" + GlobalVariables.Location_Column + ", ?)";
        }else{
            sqlStatement = "SELECT * FROM " + GlobalVariables.Hospital_Table + " WHERE " + GlobalVariables.Location_Column + " = ?";
        }
        try (PreparedStatement preparedStatement = DBconnection.prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, thisLocation);
            hospitalDataVBox.getChildren().clear();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                ObservableList<Data> hospitalDataListOfThisLocation = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String hospitalName = resultSet.getString(GlobalVariables.Hospital_Name_Column);
                    String hospitalContact = resultSet.getString(GlobalVariables.Contact_Column);
                    String hospitalLocation = resultSet.getString(GlobalVariables.Location_Column);

                    hospitalDataListOfThisLocation.add(new HospitalData(hospitalName, hospitalContact, hospitalLocation));
                }

                HospitalsController.getHospitalsController().bindDataToVBox(hospitalDataListOfThisLocation, hospitalDataVBox);
            }catch (SQLException e) {
                System.out.println(GlobalVariables.DBTableError + e.getMessage());
            }
        }catch (SQLException e){
            System.out.println(GlobalVariables.DBTableError + e.getMessage());
        }
    }

    public void hospitalSelected(Data hospitalData){
        String selectedHospitalName = hospitalData.getName();
        sqlStatement = "SELECT Id FROM " + GlobalVariables.Hospital_Table + " WHERE " + GlobalVariables.Hospital_Name_Column + " = ?";
        try (PreparedStatement preparedStatement = DBconnection.prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, selectedHospitalName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String selectedHospitalId = resultSet.getString(GlobalVariables.Id_Column);
                    AmbulanceData.setSelectedAmbulanceId(null);
                    try{
                        AmbulanceApplication ambulanceApplication = new AmbulanceApplication(stage, selectedHospitalId, selectedHospitalName);

                    }catch (IOException e){
                        System.out.println("Error while loading Ambulance screen: "+e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Hospital not found: " + selectedHospitalName);
                }
            }
        } catch (SQLException e) {
            System.out.println(GlobalVariables.DBError + e.getMessage());
        }
    }

    public void retrieveAmbulanceData(VBox ambulanceDataVBox, String thisHospitalID){
        sqlStatement = "SELECT * FROM " + GlobalVariables.Ambulance_Table + " WHERE " + GlobalVariables.Hospital_id + " = ? AND Availability = 'Yes'" ;
        try (PreparedStatement preparedStatement = DBconnection.prepareStatement(sqlStatement)) {
            preparedStatement.setString(1, thisHospitalID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<AmbulanceData> AmbulanceDataList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String ambulanceType = resultSet.getString(GlobalVariables.Ambulance_category);
                    String hospitalId = resultSet.getString(GlobalVariables.Hospital_id);
                    String ambulanceId = resultSet.getString(GlobalVariables.Ambulance_id);
                    boolean ambulanceTypeAlreadyExists = AmbulanceDataList.stream().anyMatch(data -> data.getAmbulanceType().equalsIgnoreCase(ambulanceType));
                    if(!ambulanceTypeAlreadyExists){
                        AmbulanceDataList.add(new AmbulanceData(ambulanceType, hospitalId, ambulanceId));
                    }
                }
               AmbulanceController.getAmbulanceController().bindDataToVBox(AmbulanceDataList, ambulanceDataVBox);
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(GlobalVariables.DBTableError + GlobalVariables.Ambulance_Table);
        }
    }

    public void openDailyRecords(){
        sqlStatement = "SELECT * FROM " + GlobalVariables.Daily_Record;
        try (PreparedStatement preparedStatement = DBconnection.prepareStatement(sqlStatement)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    try{
                        DailyrecApplication dailyrecApplication = new DailyrecApplication(stage);
                    }catch(IOException e){
                        System.out.println("Error while loading Daily Records Screen: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (SQLException e) {
            System.out.println(GlobalVariables.DBError + e.getMessage());
        }
    }

    public void retrieveDailyRecordsData(VBox DailyRecordsDataVBox) {
        sqlStatement ="select * from "+GlobalVariables.Daily_Record+", "+GlobalVariables.Hospital_Table+" where Hospital_Id = Id";
        try (Statement statement = DBconnection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            ObservableList<Data> dailyRecordsDataList = FXCollections.observableArrayList();
            while (resultSet.next()) {
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
                dailyRecordsDataList.add(new DailyRecordsData(DailyDate, DailyPatient, DailyContact, DailyTripStart, DailyTripEnd, HospitalId, Ambid, DriverId, AttenderId));
            }
            DailyrecController.getdailyRecController().bindDataToDailyVBox(dailyRecordsDataList, DailyRecordsDataVBox);
        } catch (SQLException e) {
            System.out.println(GlobalVariables.DBTableError + e.getMessage());
        }
    }

    public boolean retrievePasswordData(String pswd){
        sqlStatement = "SELECT *" + " FROM " + GlobalVariables.Password;
        try (Statement statement = DBconnection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            while (resultSet.next()) {
                String PasswordData = resultSet.getString(GlobalVariables.Password_Column);

                if(Objects.equals(PasswordData, pswd)){
                    passwordString = "true";
                    openDailyRecords();
                    break;
                }
                else{
                    passwordString = "false";
                }
            }
        } catch (SQLException e) {
            System.out.println(GlobalVariables.DBTableError + e.getMessage());
        }
        return Objects.equals(passwordString, "true");
    }
}
