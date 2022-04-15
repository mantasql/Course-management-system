package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.Course;
import com.lab.course_management_system.dataStructures.CourseManagementSystem;
import com.lab.course_management_system.dataStructures.Person;
import com.lab.course_management_system.dataStructures.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoveStudentForm {
    @FXML
    public TextField studentIdField;

    private CourseManagementSystem courseManagementSystem;
    private Course course;

    public void setData(CourseManagementSystem courseManagementSystem,Course course){
        this.courseManagementSystem = courseManagementSystem;
        this.course = course;
    }

    public void removeStudent(ActionEvent actionEvent) throws SQLException, IOException {
        if (checkIfThereIsSuchUser()) {
            Integer userId = Integer.parseInt(studentIdField.getText());
            User user = new Person(userId);

            Connection connection = DatabaseUtils.connect();
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM user_course where user_id = " + userId +" AND course_id = "+ course.getId();
            statement.executeUpdate(sql);
            DatabaseUtils.disconnectFromDatabase(connection, statement);
            MessageHandler.alertMessage("User was successfully removed");
            courseManagementSystem.getCourses().remove(course);

            user = course.getStudents().stream().filter(e -> e.getId() == userId).findFirst().orElse(null);
            if(user != null){
                course.getStudents().remove(user);
            }
            courseManagementSystem.getCourses().add(course);
            returnToCourseMenu();
        } else {
            MessageHandler.alertMessage("Error. There is no such user id.");
        }
    }

    public void cancel(ActionEvent actionEvent) throws IOException, SQLException {
        returnToCourseMenu();
    }

    private void returnToCourseMenu() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("course-window.fxml"));
        fxmlLoader.load();

        CourseWindow courseWindow = fxmlLoader.getController();
        courseWindow.setData(courseManagementSystem,course);

        Stage stage = (Stage) studentIdField.getScene().getWindow();
        stage.close();
    };

    private boolean checkIfThereIsSuchUser() throws SQLException {
        int id;
        try {
            id = Integer.parseInt(studentIdField.getText());
        } catch (NumberFormatException e){
            return false;
        }
        Connection connection = DatabaseUtils.connect();

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM user_course WHERE user_id = "+ id +" AND course_id = "+ course.getId();
        ResultSet resultSet = statement.executeQuery(sql);
        String userID = null;
        while (resultSet.next()){
            userID = resultSet.getString("user_id");
        }
        DatabaseUtils.disconnectFromDatabase(connection,statement);

        if(userID != null){
            return true;
        } else return false;
    }
}
