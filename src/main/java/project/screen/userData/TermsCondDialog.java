package project.screen.userData;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.common.files.GlobalVariables;

public class TermsCondDialog {
    Label userConductTitle;

    Label systemUsageTitle;

    Label accountRegistrationTitle;

    Label intellectualPropertyTitle;

    Label dataPrivacyTitle;

    Label disclaimerWarrantyTitle;

    Label limitationLiabilityTitle;

    Label changesToTCTitle;

    Label governingLawTitle;

    private static TermsCondDialog termsCondDialog = new TermsCondDialog();
    public static TermsCondDialog getTermsCondDialog(){
        return termsCondDialog;
    }
    public void openTC() {
        Dialog<ButtonType> TermsAndConditionsDialog = new Dialog<>();
        TermsAndConditionsDialog.setTitle("Terms & Conditions");
        VBox TermsAndConditionsContent = new VBox();
        VBox conditions = new VBox();

        userConductTitle = new Label(GlobalVariables.userConduct);

        systemUsageTitle = new Label(GlobalVariables.systemUsage);

        accountRegistrationTitle = new Label(GlobalVariables.accountRegistration);

        intellectualPropertyTitle = new Label(GlobalVariables.intellectualProperty);

        dataPrivacyTitle = new Label(GlobalVariables.dataPrivacy);

        disclaimerWarrantyTitle = new Label(GlobalVariables.disclaimerWarranty);

        limitationLiabilityTitle = new Label(GlobalVariables.limitationLiability);

        changesToTCTitle = new Label(GlobalVariables.changesToTC);

        governingLawTitle = new Label(GlobalVariables.governingLaw);

        setStyles();

        conditions.getChildren().addAll(userConductTitle, TermsConditionsData.setDataForTitle(GlobalVariables.userConduct),
                systemUsageTitle, TermsConditionsData.setDataForTitle(GlobalVariables.systemUsage),
                accountRegistrationTitle, TermsConditionsData.setDataForTitle(GlobalVariables.accountRegistration),
                intellectualPropertyTitle, TermsConditionsData.setDataForTitle(GlobalVariables.intellectualProperty),
                dataPrivacyTitle, TermsConditionsData.setDataForTitle(GlobalVariables.dataPrivacy),
                disclaimerWarrantyTitle, TermsConditionsData.setDataForTitle(GlobalVariables.disclaimerWarranty),
                limitationLiabilityTitle, TermsConditionsData.setDataForTitle(GlobalVariables.limitationLiability),
                changesToTCTitle, TermsConditionsData.setDataForTitle(GlobalVariables.changesToTC),
                governingLawTitle, TermsConditionsData.setDataForTitle(GlobalVariables.governingLaw),
                TermsConditionsData.setDataForTitle("Conclusion"));

        ScrollPane conditionsScrollpane = new ScrollPane();
        conditionsScrollpane.setContent(conditions);
        conditionsScrollpane.setFitToWidth(false);
        conditionsScrollpane.setFitToHeight(true);
        conditionsScrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        TermsAndConditionsContent.getChildren().addAll(
                TermsConditionsData.setDataForTitle("Introduction"),
                conditionsScrollpane);

        TermsAndConditionsDialog.getDialogPane().setContent(TermsAndConditionsContent);
        TermsAndConditionsDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        TermsAndConditionsDialog.getDialogPane().setPrefSize(350.0, 300.0);
        TermsAndConditionsDialog.showAndWait();
    }

    private void setStyles(){
        userConductTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        systemUsageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        accountRegistrationTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        intellectualPropertyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dataPrivacyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        disclaimerWarrantyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        limitationLiabilityTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        changesToTCTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        governingLawTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox.setMargin(userConductTitle, new Insets(5, 0, 0, 0));
        VBox.setMargin(systemUsageTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(accountRegistrationTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(intellectualPropertyTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(dataPrivacyTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(disclaimerWarrantyTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(limitationLiabilityTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(changesToTCTitle, new Insets(10, 0, 0, 0));
        VBox.setMargin(governingLawTitle, new Insets(10, 0, 0, 0));
    }
}
