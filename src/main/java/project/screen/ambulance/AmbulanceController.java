package project.screen.ambulance;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.common.files.DataSource;
import project.common.files.GlobalVariables;
import project.screen.userData.UserDataDialog;

public class AmbulanceController {
    @FXML
    public Label ambulanceText;
    @FXML
    public Label ambulanceHeader;
    @FXML
    public GridPane DataGridpane;
    @FXML
    public VBox ambulanceDataVBox;
    @FXML
    public Button bookbutton;
    @FXML
    private static AmbulanceController ambulanceController = new AmbulanceController();

    public static AmbulanceController getAmbulanceController(){
        return ambulanceController;
    }
    @FXML
    public void initialize(){
        applicationLoader();
    }

    public void applicationLoader() {
        DataSource.setHeaderText(ambulanceText,"Ambulance Standby at " + AmbulanceApplication.getThisHospitalname() );
        setStyles();
        DataSource.getDBInstance().retrieveAmbulanceData(ambulanceDataVBox, AmbulanceApplication.getThisHospitalID());
    }

    @FXML
    private void setStyles() {
        ambulanceText.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 24));
        ambulanceText.setStyle("-fx-padding: 5px;");
        ambulanceHeader.setMinHeight(30.0);
        ambulanceHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;-fx-padding: 5px;");
        ambulanceHeader.prefWidthProperty().bind(DataGridpane.widthProperty());
        bookbutton.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 14px;");
    }

    public void bindDataToVBox(ObservableList<AmbulanceData> AmbulanceDataObservableList, VBox ambulanceDataVBox){
        if(AmbulanceDataObservableList.isEmpty()){
            ambulanceDataVBox.getChildren().add(GlobalVariables.noRecords);
            bookbutton.setDisable(true);
        }else{
            ToggleGroup toggleGroup = new ToggleGroup();
            for(AmbulanceData ambulanceData: AmbulanceDataObservableList){
                RadioButton ambulanceTypeRadioBtn = new RadioButton(ambulanceData.getAmbulanceType());
                ambulanceTypeRadioBtn.setToggleGroup(toggleGroup);
                ambulanceTypeRadioBtn.setOnAction(e -> {
                    String clickedAmbulanceId = ambulanceData.getAmbulanceId();
                    AmbulanceData.setSelectedAmbulanceId(clickedAmbulanceId);
                });
                ambulanceTypeRadioBtn.setMinWidth(300.0);
                HBox hospitalDataHBox = new HBox();
                hospitalDataHBox.getChildren().addAll( ambulanceTypeRadioBtn );
                ambulanceDataVBox.getChildren().add(hospitalDataHBox);
            }
        }
    }

    public void bookAmbulance(){
        UserDataDialog.getUserDataDialog().bookAmbulance();
    }
}
