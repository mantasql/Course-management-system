package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class SignUpForm implements Initializable {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userNameField;
    @FXML
    public RadioButton personRadioButton;
    @FXML
    public RadioButton companyRadioButton;
    @FXML
    public TextField personNameField;
    @FXML
    public TextField personSurnameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField companyPhoneNumberField;
    @FXML
    public Label personNameLabel;
    @FXML
    public Label personSurnameLabel;
    @FXML
    public Label addressLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label companyPhoneNumberLabel;
    @FXML
    public Label companyNameLabel;
    @FXML
    public Label companyRepresentativeLabel;
    @FXML
    public TextField companyNameField;
    @FXML
    public TextField companyRepresentativeField;
    public ToggleGroup userType;

    public void createUser(ActionEvent actionEvent) throws IOException {
        if(personRadioButton.isSelected()){

            Person person = new Person(userNameField.getText(),passwordField.getText(),personNameField.getText(),personSurnameField.getText(),addressField.getText(),emailField.getText());

            Connection connection = null;
            PreparedStatement insert = null;

            try{
                connection = DatabaseUtils.connect();
                String sql = "INSERT INTO users(`login`, `password`, `date_created`, `date_modified`, `is_active`, `person_name`, `person_surname`, `address`, `email`, `user_type`) VALUES (?,?,?,?,?,?,?,?,?,?)";
                insert = connection.prepareStatement(sql);
                insert.setString(1,person.getUserName());
                insert.setString(2, person.getPassword());
                insert.setDate(3, Date.valueOf(person.getDateCreated()));
                insert.setDate(4, Date.valueOf(person.getDateModified()));
                insert.setBoolean(5, person.isActive());
                insert.setString(6, person.getName());
                insert.setString(7, person.getSurname());
                insert.setString(8, person.getAddress());
                insert.setString(9, person.getEmail());
                insert.setString(10, person.getUserType().name());
                insert.execute();

                DatabaseUtils.disconnectFromDatabase(connection,insert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MessageHandler.alertMessage("Cannot create company type yet!");
            returnToLogin();
        }
        MessageHandler.alertMessage("User created successfully!");
        returnToLogin();
    }

    public void returnToLoginForm(ActionEvent actionEvent) throws IOException {
        returnToLogin();
    }

    private void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("login-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) userNameField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companyRepresentativeField.setVisible(false);
        companyPhoneNumberField.setVisible(false);
        companyNameField.setVisible(false);
        companyPhoneNumberLabel.setVisible(false);
        companyRepresentativeLabel.setVisible(false);
        companyNameLabel.setVisible(false);
    }

    public void enableFields(ActionEvent actionEvent) {
        if(personRadioButton.isSelected()) {
            personNameField.setVisible(true);
            personNameLabel.setVisible(true);
            personSurnameField.setVisible(true);
            personSurnameLabel.setVisible(true);

            companyRepresentativeField.setVisible(false);
            companyPhoneNumberField.setVisible(false);
            companyNameField.setVisible(false);
            companyPhoneNumberLabel.setVisible(false);
            companyRepresentativeLabel.setVisible(false);
            companyNameLabel.setVisible(false);
        } else {
            personNameField.setVisible(false);
            personNameLabel.setVisible(false);
            personSurnameField.setVisible(false);
            personSurnameLabel.setVisible(false);

            companyRepresentativeField.setVisible(true);
            companyPhoneNumberField.setVisible(true);
            companyNameField.setVisible(true);
            companyPhoneNumberLabel.setVisible(true);
            companyRepresentativeLabel.setVisible(true);
            companyNameLabel.setVisible(true);
        }
    }
}
