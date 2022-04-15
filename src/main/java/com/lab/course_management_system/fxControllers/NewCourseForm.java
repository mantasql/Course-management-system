package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.Course;
import com.lab.course_management_system.dataStructures.CourseManagementSystem;
import com.lab.course_management_system.dataStructures.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class NewCourseForm {
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public Label nameLabel;
    @FXML
    public Label descriptionLabel;

    private CourseManagementSystem courseManagementSystem;

    public void setCourseManagementSystem(CourseManagementSystem courseManagementSystem) {
        this.courseManagementSystem = courseManagementSystem;
    }

    public void createCourse(ActionEvent actionEvent) throws SQLException, IOException {
        ArrayList<User> loggedInUser = new ArrayList<>();
        loggedInUser.add(courseManagementSystem.getLoggedInUser());

        Course course = new Course(nameField.getText(),descriptionField.getText(),loggedInUser);

        Connection connection = null;
        PreparedStatement insert = null;

        connection = DatabaseUtils.connect();
        String sql = "INSERT INTO courses(`name`,`description`,`date_created`) VALUES (?,?,?)";
        insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        insert.setString(1,course.getCourseName());
        insert.setString(2,course.getCourseDescription());
        insert.setDate(3, Date.valueOf(course.getDateCreated()));
        insert.execute();

        ResultSet resultSet = insert.getGeneratedKeys();
        int courseID = 0;
        if(resultSet.next()){
            courseID = resultSet.getInt(1);
        }

        //User and course association
        String sql2 = "INSERT INTO user_course(`course_id`,`user_id`) VALUES (?,?)";
        insert = connection.prepareStatement(sql2);
        insert.setInt(1,courseID);
        insert.setInt(2,courseManagementSystem.getLoggedInUser().getId());
        insert.execute();

        DatabaseUtils.disconnectFromDatabase(connection,insert);

        course.setId(courseID);
        MessageHandler.alertMessage("Course created successfully!");
        courseManagementSystem.getCourses().add(course);
        returnToMenu();
    }

    public void goBackToMenu(ActionEvent actionEvent) throws IOException {
        returnToMenu();
    }

    private void returnToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("main-menu-window.fxml"));
        fxmlLoader.load();

        MainMenuWindow mainMenuWindow = fxmlLoader.getController();
        mainMenuWindow.setCourseManagementSystem(courseManagementSystem);

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
