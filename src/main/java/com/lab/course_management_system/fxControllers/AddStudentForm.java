package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AddStudentForm {
    @FXML
    public TextField studentIdField;
    @FXML
    public Label studentIdLabel;

    private CourseManagementSystem courseManagementSystem;
    private Course course;

    public void setData(CourseManagementSystem courseManagementSystem, Course course) {
        this.courseManagementSystem = courseManagementSystem;
        this.course = course;
    }

    public void addStudent(ActionEvent actionEvent) throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement insert = null;
        Person user = getUser();
        if(getUser() != null){
            connection = DatabaseUtils.connect();
            String sql = "INSERT INTO user_course(`user_id`,`course_id`) VALUES (?,?)";
            insert = connection.prepareStatement(sql);
            insert.setInt(1,user.getId());
            insert.setInt(2,course.getId());
            insert.execute();

            DatabaseUtils.disconnectFromDatabase(connection,insert);
            MessageHandler.alertMessage("User was successfully added into course");
            courseManagementSystem.getCourses().remove(course);
            course.getStudents().add(user);
            courseManagementSystem.getCourses().add(course);
            returnToCourseMenu();
        } else {
            MessageHandler.alertMessage("Error. There is no such user id in this course.");
        }
    }

    private Person getUser() throws SQLException {
        int id;
        try {
            id = Integer.parseInt(studentIdField.getText());
        } catch (NumberFormatException e){
            return null;
        }
        Connection connection = DatabaseUtils.connect();

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users WHERE id = "+ id;
        ResultSet resultSet = statement.executeQuery(sql);
        String name = null;
        String surname = null;
        String address = null;
        String email = null;
        UserType userType = null;
        Integer ID = null;
        Person person = null;
        while (resultSet.next()){
             name = resultSet.getString("person_name");
             surname = resultSet.getString("person_surname");
             address = resultSet.getString("address");
             email = resultSet.getString("email");
             userType = UserType.valueOf(resultSet.getString("user_type"));
             ID = resultSet.getInt("id");
             person = new Person(ID,userType,name,surname,address,email);
        }
        DatabaseUtils.disconnectFromDatabase(connection,statement);

        if(name != null){
            return person;
        } else return null;
    }

    public void exitWindow(ActionEvent actionEvent) throws IOException, SQLException {
        returnToCourseMenu();
    }

    private void returnToCourseMenu() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("course-window.fxml"));
        fxmlLoader.load();

        CourseWindow courseWindow = fxmlLoader.getController();
        courseWindow.setData(courseManagementSystem,course);

        Stage stage = (Stage) studentIdField.getScene().getWindow();
        stage.close();
    }
}
