package project.screen.userData;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TermsConditionsData {
    public static Label setDataForTitle(String title){
        Label dataLabel = new Label();
        switch (title){
            case "Introduction":
                dataLabel.setText("Welcome to the UK Ambulance Management System.");
                break;
            case "User Conduct":
                dataLabel.setText("Users are solely responsible for their actions while using the Ambulance Management System.");
                break;
            case "System Usage":
                dataLabel.setText("Users must not use the system for any illegal activities or purposes unrelated to emergency management.");
                break;
            case "Account Registration":
                dataLabel.setText("Users need not create account to access this system." + "Since Ambulance service is purposefully for emergency circumstances, easy access is provided.");

            case "Intellectual Property":
                dataLabel.setText("The Ambulance Management System, including all software, designs, and content, is the intellectual property of UK Ambulance Management.");
                break;
            case "Data Privacy":
                dataLabel.setText("UK Ambulance Management respects user privacy and is committed to protecting personal data.");
                break;
            case "Disclaimer Of Warranty":
                dataLabel.setText("UK Ambulance Management does not guarantee the accuracy, reliability, or availability of the system.");
                break;
            case "Limitation Of Liability":
                dataLabel.setText("UK Ambulance Management shall not be liable for any indirect, incidental, special, or consequential damages arising out of or in connection with the use of the Ambulance Management System.");
                break;
            case "Changes To Terms & Conditions":
                dataLabel.setText("UK Ambulance Management reserves the right to modify these terms and conditions at any time without prior notice.");
                break;
            case "Governing Law":
                dataLabel.setText("These terms and conditions shall be governed by and construed in accordance with the laws of the United Kingdom.");
                break;
            case "Conclusion":
                dataLabel.setText("By using the Ambulance Management System, you acknowledge that you have read, understood, and agree to be bound by these terms and conditions." + "If you do not agree with any part of these terms and conditions, please refrain from using the system.");
                break;
            default:
                dataLabel.setText("No data");
        }
        dataLabel.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 12));
        return dataLabel;
    }
}
