package project.screen.userData;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import project.common.files.DataSource;
import project.common.files.GlobalVariables;
import project.common.files.Methods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BookAmbulance {
    public String sqlStatement;
    String currDate;
    Label patientName;
    Label patientContact;
    Label AmbulanceCategoryLabel;
    Label AmbulanceVechicleNoLabel;
    Label AmbulanceDriverLabel;
    Label AmbulanceAttenderLabel;

    Label patientNameHeader = new Label(GlobalVariables.patient_Name);
    Label patientContactHeader= new Label(GlobalVariables.contact_number);
    Label AmbulanceCategoryLabelHeader= new Label(GlobalVariables.Ambulance_Table + " " + GlobalVariables.Ambulance_category + ":");
    Label AmbulanceVechicleNoLabelHeader= new Label(GlobalVariables.Ambulance_VechicleNo + ":");
    Label AmbulanceDriverLabelHeader= new Label("Driver Name: ");
    Label AmbulanceAttenderLabelHeader= new Label("Attender Name: ");

    Dialog<ButtonType> confirmBookingDialog;
    private static BookAmbulance bookAmbulance = new BookAmbulance();
    public static BookAmbulance getBookAmbulance(){
        return bookAmbulance;
    }


    public void displayData(LocalDate currentDate, String PatientName, String ContactNumber, String HospitalID, String AmbulanceID, String driverID, String attenderID){
        confirmBookingDialog = new Dialog<>();
        confirmBookingDialog.setTitle("Confirm Booking");
        VBox confirmDialogVBox = new VBox();

        currDate = currentDate.toString();
        patientName = new Label(PatientName);
        patientContact = new Label(ContactNumber);

        retrieveAmbulanceData(AmbulanceID);

        retrieveDriverName(driverID);

        retrieveAttenderName(attenderID);

        CheckBox checkBox = new CheckBox("Accept the ");
        Hyperlink termsAndConditionsLink = new Hyperlink("Terms and Conditions");
        termsAndConditionsLink.setOnAction(actionEvent -> {
            TermsCondDialog.getTermsCondDialog().openTC();
        });
        termsAndConditionsLink.setStyle("-fx-padding: -1px 0 0 0;");
        HBox checkBoxWithLink = new HBox(checkBox, termsAndConditionsLink);

        HBox patientNameHBox = new HBox();
        patientNameHBox.getChildren().addAll(patientNameHeader, patientName);
        patientNameHBox.setSpacing(20);

        HBox patientContactHBox = new HBox();
        patientContactHBox.getChildren().addAll(patientContactHeader, patientContact);
        patientContactHBox.setSpacing(20);

        HBox AmbulanceCategoryHBox = new HBox();
        AmbulanceCategoryHBox.getChildren().addAll(AmbulanceCategoryLabelHeader, AmbulanceCategoryLabel);
        AmbulanceCategoryHBox.setSpacing(20);

        HBox AmbulanceVehicleNoHBox = new HBox();
        AmbulanceVehicleNoHBox.getChildren().addAll(AmbulanceVechicleNoLabelHeader, AmbulanceVechicleNoLabel);
        AmbulanceVehicleNoHBox.setSpacing(20);

        HBox AmbulanceDriverHBox = new HBox();
        AmbulanceDriverHBox.getChildren().addAll(AmbulanceDriverLabelHeader, AmbulanceDriverLabel);
        AmbulanceDriverHBox.setSpacing(20);

        HBox AmbulanceAttenderHBox = new HBox();
        AmbulanceAttenderHBox.getChildren().addAll(AmbulanceAttenderLabelHeader, AmbulanceAttenderLabel);
        AmbulanceAttenderHBox.setSpacing(20);

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String tripStart = currentTime.format(timeFormatter);
        LocalTime tripEndTime = currentTime.plusMinutes(15);
        String tripEnd = tripEndTime.format(timeFormatter);

        confirmDialogVBox.getChildren().addAll(patientNameHBox, patientContactHBox, AmbulanceCategoryHBox, AmbulanceVehicleNoHBox, AmbulanceDriverHBox, AmbulanceAttenderHBox, checkBoxWithLink);
        confirmBookingDialog.getDialogPane().setContent(confirmDialogVBox);
        confirmBookingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        confirmBookingDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        checkBox.setOnAction(actionEvent -> {
            if(checkBox.isSelected()){
                confirmBookingDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            }else {
                confirmBookingDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            }
        });

        confirmBookingDialog.setResultConverter(buttonType -> {
            if(buttonType == ButtonType.OK){
                processBooking(PatientName, ContactNumber, HospitalID, AmbulanceID, driverID, attenderID, tripStart, tripEnd);
            }
            return buttonType;
        });

        confirmDialogVBox.setSpacing(20.0);
        confirmBookingDialog.show();
    }

    public void retrieveAmbulanceData(String AmbulanceID){
        sqlStatement = "SELECT Category, Vehicle_No" + " FROM " + GlobalVariables.Ambulance_Table + " WHERE Id = " + AmbulanceID;

        try (Statement statement = DataSource.getDBInstance().getDBconnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            while (resultSet.next()) {
                String AmbulanceCategory = resultSet.getString(GlobalVariables.Ambulance_category);
                String AmbulanceVechicleNo = resultSet.getString(GlobalVariables.Ambulance_VechicleNo);
                AmbulanceCategoryLabel = new Label(AmbulanceCategory);
                AmbulanceVechicleNoLabel = new Label(AmbulanceVechicleNo);
            }
        }
        catch (SQLException e) {
            System.out.println("Error while retrieving selected Ambulance details: "+e.getMessage());
        }
    }

    public void retrieveDriverName(String driverID){
        sqlStatement = "SELECT " + GlobalVariables.Staff_Name_Column + " FROM " + GlobalVariables.Staff_Table + " WHERE Id = '" + driverID + "'";

        try (Statement statement = DataSource.getDBInstance().getDBconnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            while (resultSet.next()) {
                String AmbulanceDriver = resultSet.getString(GlobalVariables.Staff_Name_Column);
                AmbulanceDriverLabel = new Label(AmbulanceDriver);
            }
        }
        catch (SQLException e) {
            System.out.println("Error while retrieving assigned Ambulance driver name: "+e.getMessage());
        }
    }

    public void retrieveAttenderName(String attenderID){
        sqlStatement = "SELECT " + GlobalVariables.Staff_Name_Column + " FROM " + GlobalVariables.Staff_Table + " WHERE Id = '" + attenderID + "'";

        try (Statement statement = DataSource.getDBInstance().getDBconnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            while (resultSet.next()) {
                String AmbulanceAttender = resultSet.getString(GlobalVariables.Staff_Name_Column);
                AmbulanceAttenderLabel = new Label(AmbulanceAttender);
            }
        }
        catch (SQLException e) {
            System.out.println("Error while retrieving assigned Ambulance attender name: "+e.getMessage());
        }
    }

    public void processBooking(String PatientName, String ContactNumber, String HospitalID, String AmbulanceID, String driverID, String attenderID, String tripStart, String tripEnd){
        String insertSql ="INSERT INTO Daily_Record (\"Date\", Patient_Name, Contact, Hospital_Id, Ambulance_Id, Driver_Id, Attender_Id, Trip_Start, Trip_End) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(insertSql)){
            preparedStatement.setString(1, currDate);
            preparedStatement.setString(2, PatientName);
            preparedStatement.setString(3, ContactNumber);
            preparedStatement.setString(4, HospitalID);
            preparedStatement.setInt(5, Integer.parseInt(AmbulanceID));
            preparedStatement.setString(6, driverID);
            preparedStatement.setString(7, attenderID);
            preparedStatement.setString(8, tripStart);
            preparedStatement.setString(9, tripEnd);
            try{
                preparedStatement.executeUpdate();
                System.out.println("Insertion done.");
                afterInsertingPatientData(AmbulanceID, driverID, attenderID);
            }catch (SQLException e){
                System.out.println("Couldn't Insert data into Daily_Records Table." + e.getMessage());
            }
        }catch (SQLException e){
            System.out.println("Error while inserting data into Daily_Records Table in DB: "+ e.getMessage());
        }
    }

    public void afterInsertingPatientData(String AmbulanceID, String driverID, String attenderID){
        Methods.getMethodsInstance().triggerAlert(GlobalVariables.successfulBooking, "https://images.vexels.com/media/users/3/151573/isolated/preview/4787603f37806e500ce838b9986b1768-ambulance-icon-by-vexels.png");
        confirmBookingDialog.close();

        //change the availability of driver and attender and ambulance to No.
        String availabilitySql = "UPDATE " + GlobalVariables.Staff_Table + " SET Availability = 'No' WHERE Id = ? OR Id = ?";
        try (PreparedStatement availabilityStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(availabilitySql)) {

            availabilityStatement.setString(1, driverID);
            availabilityStatement.setString(2, attenderID);

            availabilityStatement.executeUpdate();

            String ambulanceAvailabilitySQL = "UPDATE " + GlobalVariables.Ambulance_Table + " SET Availability = 'No' WHERE Id = ?";
            try(PreparedStatement ambAvailStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(ambulanceAvailabilitySQL)){
                ambAvailStatement.setInt(1, Integer.parseInt(AmbulanceID));
                ambAvailStatement.executeUpdate();

                // Schedule an event to occur 15 minutes after trip start
                Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
                    System.out.println("15 minutes have passed since trip start. Perform operations here...");
                    //change the availability of driver, attender and ambulance to Yes
                    String availabilityYesSql = "UPDATE " + GlobalVariables.Staff_Table + " SET Availability = 'Yes' WHERE Id = ? OR Id = ?";
                    try (PreparedStatement availabilityYesStatement = DataSource.getDBInstance().getDBconnection().prepareStatement(availabilityYesSql)) {

                        availabilityYesStatement.setString(1, driverID);
                        availabilityYesStatement.setString(2, attenderID);

                        availabilityYesStatement.executeUpdate();
                        System.out.println("Updated staff table back to yes");

                        String ambAvailabilityYesSql = "UPDATE " + GlobalVariables.Ambulance_Table + " SET Availability = 'Yes' WHERE Id = " + AmbulanceID;
                        try(Statement ambAvailYesStatement = DataSource.getDBInstance().getDBconnection().createStatement()){
                            ambAvailYesStatement.executeUpdate(ambAvailabilityYesSql);
                            System.out.println("Updated ambulance table back to yes");
                            System.exit(0);
                        }catch (SQLException ex){
                            System.out.println("Error updating availability of ambulance: " + ex.getMessage());
                        }
                    } catch (SQLException ex) {
                        System.out.println("Error updating availability of driver and attender: " + ex.getMessage());
                    }
                }));

                timeline.play();

            }catch (SQLException e){
                System.out.println("Error updating availability of ambulance: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error updating availability of driver and attender: " + e.getMessage());
        }
    }
}
