package project.screen.hospitals;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.common.files.Data;
import project.common.files.DataSource;
import project.common.files.GlobalVariables;
import project.common.files.Methods;

import java.util.Optional;

public class HospitalsController{
    @FXML
    public Label headerText;
    public Label hospitalsHeader;
    public Label contactHeader;
    public Label locationHeader;
    @FXML
    public Hyperlink dailyRecordsHyperLink;

    public String Password;

    @FXML
    public ComboBox locationDropdownBox;
    @FXML
    public TextField searchBar;
    @FXML
    public ImageView searchIcon;
    @FXML
    private ScrollPane hospitalDataScrollPane;
    @FXML
    public VBox hospitalDataVBox;

    @FXML
    private static HospitalsController hospitalsController = new HospitalsController();

    public static HospitalsController getHospitalsController(){
        return hospitalsController;
    }

    @FXML
    public void initialize() {
        applicationLoader(GlobalVariables.AppLoading);
        locationDropdownBox.setOnAction(event -> handleLocationChange());
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            search(newValue, hospitalDataVBox);
        });
    }
    @FXML
    public void applicationLoader(String loading) {
        try{
            if (DataSource.getDBInstance().openDB()) {
                DataSource.setHeaderText(headerText,"UK Healthcare Directory");
                setStyles();
                DataSource.getDBInstance().retrieveHospitalsData(hospitalDataVBox, locationDropdownBox);
            } else {
                DataSource.setHeaderText(headerText,GlobalVariables.DBError);
            }
        }catch (NullPointerException e){
            DataSource.setHeaderText(headerText,loading);
        }
    }

    @FXML
    private void setStyles(){
        headerText.setFont(Font.font("Times New Roman",  FontWeight.NORMAL, 24));
        hospitalsHeader.prefWidthProperty().bind(hospitalDataScrollPane.widthProperty().divide(3));
        contactHeader.prefWidthProperty().bind(hospitalDataScrollPane.widthProperty().divide(3));
        locationHeader.prefWidthProperty().bind(hospitalDataScrollPane.widthProperty().divide(3));
        hospitalsHeader.setMinHeight(30.0);
        contactHeader.setMinHeight(30.0);
        locationHeader.setMinHeight(30.0);
        hospitalsHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;-fx-padding: 0 0 0 7px;");
        contactHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        locationHeader.setStyle("-fx-background-color: #0086b3;-fx-text-fill: white;-fx-font-size: 16px;");
        dailyRecordsHyperLink.setStyle("-fx-text-fill: red;-fx-font-size: 14px;-fx-border-color: transparent;-fx-underline: false;-fx-translate-y: 13px;");
        hospitalDataVBox.setStyle("-fx-font-size: 14px;");
        hospitalDataVBox.setSpacing(18);
        hospitalDataVBox.prefWidthProperty().bind(hospitalDataScrollPane.widthProperty());
        searchBar.setStyle("-fx-translate-y: 15px;");
        searchBar.setPromptText("Search hospitals");
        searchIcon.setStyle("-fx-translate-y: 10px;");
    }

    @FXML
    public void bindDataToVBox(ObservableList<Data> hospitalDataObservableList, VBox hospitalDataVBox){
        if(hospitalDataObservableList.isEmpty()){
            hospitalDataVBox.getChildren().add(GlobalVariables.noRecords);
        }else{
            for(Data hospitalData: hospitalDataObservableList){
                Hyperlink hospitalLink = new Hyperlink(hospitalData.getName());
                Label Contact = new Label(hospitalData.getContact());
                Label Location = new Label(hospitalData.getLocation());

                HBox hospitalDataHBox = new HBox();
                hospitalDataHBox.getChildren().addAll(hospitalLink, Contact, Location);

                hospitalLink.prefWidthProperty().bind(hospitalDataHBox.widthProperty().divide(3));
                Contact.prefWidthProperty().bind(hospitalDataHBox.widthProperty().divide(3));
                Location.prefWidthProperty().bind(hospitalDataHBox.widthProperty().divide(3));

                hospitalLink.setOnMouseEntered(e -> hospitalLink.setStyle("-fx-text-fill: purple;"));
                hospitalLink.setOnMouseExited(e -> hospitalLink.setStyle("-fx-text-fill: #0099cc;-fx-border-color: transparent;-fx-underline: false;"));
                hospitalLink.setOnAction(event -> {DataSource.getDBInstance().hospitalSelected(hospitalData);});

                hospitalDataVBox.getChildren().add(hospitalDataHBox);
            }
        }
    }

    @FXML
    private void handleLocationChange(){
        Object selectedObject = locationDropdownBox.getValue();
        String selectedLocation;
        if(selectedObject instanceof String){
            selectedLocation = (String) selectedObject;
        }else{
            System.out.println("Location: Unexpected selected value type");
            selectedLocation = GlobalVariables.All_Locations;
        }
        DataSource.getDBInstance().changeHospitalsData(hospitalDataVBox, selectedLocation);
    }

    @FXML
    private void search(String searchValue, VBox hospitalDataVBox){
        Methods.getMethodsInstance().globalSearch(searchValue, hospitalDataVBox, GlobalVariables.Hospital_Table, GlobalVariables.Hospital_Name_Column);
    }

    @FXML
    public void dailyRecHyperlinkClicked(ActionEvent event) {
        Dialog<ButtonType> authDialog = new Dialog<>();
        authDialog.setTitle("User Authentication");

        VBox authDialogVBox = new VBox();
        PasswordField authPassword = new PasswordField();
        authDialogVBox.getChildren().addAll(new Label(GlobalVariables.AuthLabel_Text), authPassword);
        ImageView authImage = new ImageView("https://cdn1.iconfinder.com/data/icons/large-glossy-icons/512/User_login.png");
        authImage.setFitHeight(70);
        authImage.setFitWidth(70);
        HBox authDialogHBox = new HBox();
        authDialogHBox.getChildren().addAll(authImage,authDialogVBox);
        authDialogHBox.setSpacing(10);
        authDialog.getDialogPane().setContent(authDialogHBox);

        authDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        authDialogVBox.setSpacing(20.0);

        Optional<ButtonType> result = authDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Password = authPassword.getText();
            boolean isPasswordCorrect = DataSource.getDBInstance().retrievePasswordData(Password);
            if (isPasswordCorrect) {
                DataSource.getDBInstance().openDailyRecords();
            } else {
                Methods.getMethodsInstance().triggerAlert(GlobalVariables.pswdError, "https://www.pinclipart.com/picdir/big/89-895685_e-mail-wrong-flat-icon-clipart.png");
            }
        }
    }
}