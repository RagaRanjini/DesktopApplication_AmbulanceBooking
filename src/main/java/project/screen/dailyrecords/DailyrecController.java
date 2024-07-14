package project.screen.dailyrecords;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.common.files.Data;
import project.common.files.DataSource;
import project.common.files.GlobalVariables;
import project.common.files.Methods;

public class DailyrecController {
    @FXML
    public Label headerText;
    public Label dateHeader;
    public Label patientNameHeader;
    public Label contactHeader;
    public Label tripStartHeader;
    public Label tripEndHeader;
    public Label HospitalHeader;
    public Label AmbulanceHeader;
    public Label DriverHeader;
    public Label AttenderHeader;
    Label Date = new Label();
    Label Patient = new Label();
    Label Contact = new Label();
    Label Tripstart = new Label();
    Label Tripend = new Label();
    Label Hospital = new Label();
    Label Ambulance = new Label();
    Label Driver = new Label();
    Label Attender = new Label();

    @FXML
    private ScrollPane dailyRecordsDataScrollPane;
    @FXML
    public VBox dailyRecordsDataVBox;
    @FXML
    public TextField searchBar;
    @FXML
    public ImageView searchIcon;

    @FXML
    private static DailyrecController dailyRecController = new DailyrecController();

    public static DailyrecController getdailyRecController(){
        return dailyRecController;
    }

    @FXML
    public void initialize(){
        applicationLoader();
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue, dailyRecordsDataVBox);
        });
    }

    public void applicationLoader() {
        DataSource.setHeaderText(headerText,"Daily Records");
        setStyles();
        DataSource.getDBInstance().retrieveDailyRecordsData(dailyRecordsDataVBox);
    }


    public void bindDataToDailyVBox(ObservableList<Data> DailyDataObservableList, VBox DailyRecordsDataVBox){
        if(DailyDataObservableList.isEmpty()){
            DailyRecordsDataVBox.getChildren().add(GlobalVariables.noRecords);
        }else{
            for(Data DailyRecordsData: DailyDataObservableList){

                Date = new Label(DailyRecordsData.getDailyDate());
                Patient = new Label(DailyRecordsData.getName());
                Contact = new Label(DailyRecordsData.getContact());
                Tripstart = new Label(DailyRecordsData.getDailyTripStart());
                Tripend = new Label(DailyRecordsData.getDailyTripEnd());
                Hospital = new Label(DailyRecordsData.getDailyHospitalId());
                Ambulance = new Label(DailyRecordsData.getDailyAmbulanceId());
                Driver = new Label(DailyRecordsData.getDailyDriverId());
                Attender = new Label(DailyRecordsData.getDailyAttender());


                HBox DailyDataHBox = new HBox();
                DailyDataHBox.getChildren().addAll(Date, Patient, Contact, Tripstart, Tripend, Hospital, Ambulance, Driver, Attender);

                Date.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Patient.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Contact.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Tripstart.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Tripend.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Hospital.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Ambulance.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Driver.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));
                Attender.minWidthProperty().bind(DailyDataHBox.widthProperty().divide(9));

                DailyRecordsDataVBox.getChildren().add(DailyDataHBox);
            }
        }
    }

    @FXML
    private void setStyles(){
        headerText.setFont(Font.font("Times New Roman",  FontWeight.NORMAL, 24));

        patientNameHeader.setMinHeight(30.0);
        contactHeader.setMinHeight(30.0);
        tripStartHeader.setMinHeight(30.0);
        tripEndHeader.setMinHeight(30.0);
        HospitalHeader.setMinHeight(30.0);
        AmbulanceHeader.setMinHeight(30.0);
        DriverHeader.setMinHeight(30.0);
        dateHeader.setMinHeight(30.0);
        AttenderHeader.setMinHeight(30.0);

        dateHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        patientNameHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        contactHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        tripStartHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        tripEndHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        HospitalHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        AmbulanceHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        DriverHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));
        AttenderHeader.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty().divide(9));

        dateHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;-fx-padding: 0 0 0 7px;");
        patientNameHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        contactHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        tripStartHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        tripEndHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        HospitalHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        AmbulanceHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        DriverHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        AttenderHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");

        dailyRecordsDataVBox.setSpacing(35);
        dailyRecordsDataVBox.prefWidthProperty().bind(dailyRecordsDataScrollPane.widthProperty());

        searchBar.setStyle("-fx-translate-y: 10px;");
        searchBar.setPromptText("Search patient name");
        searchIcon.setStyle("-fx-translate-y: 5px;");
    }

    @FXML
    private void search(String searchValue, VBox dailyRecordsDataVBox){
        Methods.getMethodsInstance().globalSearch(searchValue, dailyRecordsDataVBox, GlobalVariables.Daily_Record, GlobalVariables.Patient_Name_Column);
    }



}
