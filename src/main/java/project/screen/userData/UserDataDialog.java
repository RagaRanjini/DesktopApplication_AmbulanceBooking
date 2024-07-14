package project.screen.userData;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import project.common.files.DataSource;
import project.common.files.GlobalVariables;
import project.common.files.Methods;
import project.screen.ambulance.AmbulanceApplication;
import project.screen.ambulance.AmbulanceData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class UserDataDialog {
    private static UserDataDialog userDataDialog = new UserDataDialog();
    public static UserDataDialog getUserDataDialog(){
        return userDataDialog;
    }

    public String sqlStatement;
    public boolean isNameValid = false;
    public boolean isContactValid = false;
    public void bookAmbulance(){
        if(AmbulanceData.getSelectedAmbulanceId() != null){

            Dialog<ButtonType> bookingDialog = new Dialog<>();
            bookingDialog.setTitle("Patient Data");
            VBox bookDialogVBox = new VBox();

            TextField patientName = new TextField();
            bookDialogVBox.getChildren().addAll(new Label(GlobalVariables.patient_Name), patientName);

            TextField patientContact = new TextField();
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[0-9 ]*")) {
                    return change;
                }
                return null;
            };
            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            patientContact.setTextFormatter(textFormatter);
            bookDialogVBox.getChildren().addAll(new Label(GlobalVariables.contact_number), patientContact);

            bookingDialog.getDialogPane().setContent(bookDialogVBox);
            ButtonType nextButtonType = new ButtonType("Next", ButtonType.OK.getButtonData());
            bookingDialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

            //validating fields
            Pattern alphabeticPattern = Pattern.compile("^[a-zA-Z ]{1,20}$");
//            Pattern contactPattern = Pattern.compile("^(?:0|\\+?44)(?:\\d\\s?){9,10}$");
            Pattern contactPattern = Pattern.compile("^44(?:\\d\\s?){9,10}$");


            patientName.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String name = newValue.trim();
                    if (!name.isEmpty()) {
                        isNameValid = alphabeticPattern.matcher(name).matches();
                    } else {
                        isNameValid = false;
                    }
                }
            });

            patientContact.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String contact = newValue;
                    if (!contact.isEmpty()) {
                        isContactValid = contactPattern.matcher(contact).matches();
                    } else {
                        isContactValid = false;
                    }
                }
            });

            bookingDialog.setResultConverter(buttonType -> {
                if(buttonType == nextButtonType && isNameValid && isContactValid) {
                    isNameValid = false;
                    isContactValid = false;
                    LocalDate currentDate = LocalDate.now();
                    String PatientName = patientName.getText();
                    String PatientContact = patientContact.getText();
                    if (PatientContact != null && PatientContact.length() >= 10) {
                        String countryCode = PatientContact.substring(0, 2);
                        String areaCode = PatientContact.substring(2, 6);
                        String phoneNumber = PatientContact.substring(6);

                        String formattedPhoneNumber = String.format("+%s %s %s", countryCode, areaCode, phoneNumber);
                        System.out.println(formattedPhoneNumber);
                        PatientContact = formattedPhoneNumber;
                    }
                    String HospitalID = AmbulanceApplication.getThisHospitalID();
                    String AmbulanceId = AmbulanceData.getSelectedAmbulanceId();

                    //Driver and Attender random assignment based on availability
                    String assignedDriverID = assignDriver(HospitalID);
                    String assignedAttenderID = assignAttender(HospitalID);

                    if(assignedDriverID == null || assignedAttenderID == null){
                        Methods.getMethodsInstance().triggerAlert(GlobalVariables.noStaffFound, GlobalVariables.alertIcon);
                    }else{
                        BookAmbulance.getBookAmbulance().displayData(currentDate, PatientName, PatientContact, HospitalID, AmbulanceId, assignedDriverID, assignedAttenderID);
                    }
                } else if(buttonType == nextButtonType && (!isNameValid || !isContactValid)){
                    Methods.getMethodsInstance().triggerAlert(GlobalVariables.invalidUserData, GlobalVariables.alertIcon);
                }

                return buttonType;
            });
            bookDialogVBox.setSpacing(10.0);
            bookingDialog.showAndWait();
        }
        else{
            Methods.getMethodsInstance().triggerAlert(GlobalVariables.noAmbulanceSelected_Text, GlobalVariables.alertIcon);
        }
    }
    public String assignDriver(String HospitalID){
        Random random = new Random();

        ObservableList<String> driversAvailable = FXCollections.observableArrayList();
        sqlStatement = "SELECT * FROM "+ GlobalVariables.Staff_Table + " WHERE " + GlobalVariables.Hospital_id + " = ? AND Availability = 'Yes' AND Designation = 'Driver'";
        try(PreparedStatement preparedStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(sqlStatement);){
            preparedStatement.setString(1, HospitalID);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String driverID = resultSet.getString(GlobalVariables.Id_Column);
                    driversAvailable.add(driverID);
                }
            }
        }catch(SQLException e){
            System.out.println("Error while attempting to find available staffs - Driver.");
        }

        if(!driversAvailable.isEmpty()){
            int driversAvailableCount = driversAvailable.size();
            int randomIndex = random.nextInt(driversAvailableCount);
            return driversAvailable.get(randomIndex);
        }
        return null;

    }
    public String assignAttender(String HospitalID){
        Random random = new Random();

        ObservableList<String> attendersAvailable = FXCollections.observableArrayList();
        sqlStatement = "SELECT * FROM "+ GlobalVariables.Staff_Table + " WHERE " + GlobalVariables.Hospital_id + " = ? AND Availability = 'Yes' AND Designation = 'Attender'";
        try(PreparedStatement preparedStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(sqlStatement);){
            preparedStatement.setString(1, HospitalID);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    String attenderID = resultSet.getString(GlobalVariables.Id_Column);
                    attendersAvailable.add(attenderID);
                }
            }
        }catch(SQLException e){
            System.out.println("Error while attempting to find available staffs - Attender.");
        }

        if(!attendersAvailable.isEmpty()){
            int attendersAvailableCount = attendersAvailable.size();
            int randomIndex = random.nextInt(attendersAvailableCount);
            return attendersAvailable.get(randomIndex);
        }
        return null;
    }
}
