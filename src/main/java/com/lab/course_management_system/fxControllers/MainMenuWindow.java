package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.Course;
import com.lab.course_management_system.dataStructures.CourseManagementSystem;
import com.lab.course_management_system.dataStructures.UserType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainMenuWindow {
    @FXML
    public ListView<String> coursesListView;
    @FXML
    public Label userTypeLabel;

    private CourseManagementSystem courseManagementSystem;

    //TODO: Implement remove student (Inside ListView) (Not important)
    //TODO: Further implement course window.
    //TODO: Course window has to have:
    //TODO: Implement add moderators
    //TODO: Implement add files
    //TODO: *Implement folder TreeView

    public void setCourseManagementSystem(CourseManagementSystem courseManagementSystem) {
        this.courseManagementSystem = courseManagementSystem;

        coursesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    openCourseWindow(t1);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        fillTables();
        userTypeLabel.setText("User type: "+courseManagementSystem.getLoggedInUser().getUserType().name());
    }

    public void returnToLogInWindow(ActionEvent actionEvent) throws IOException {
        returnToLogin();
    }

    public void createNewCourseForm(ActionEvent actionEvent) throws IOException {
        if(courseManagementSystem.getLoggedInUser().getUserType() == UserType.ADMIN || courseManagementSystem.getLoggedInUser().getUserType() == UserType.MODERATOR){
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("new-course-form.fxml"));
            Parent root = fxmlLoader.load();

            NewCourseForm newCourseForm = fxmlLoader.getController();
            newCourseForm.setCourseManagementSystem(courseManagementSystem);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Course creation form");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            fillTables();
        } else {
            MessageHandler.alertMessage("You don't have permission to create a course");
        }
    }

    public void aboutWindow(ActionEvent actionEvent) {
    }

    private void returnToLogin() throws IOException {
        courseManagementSystem = null;
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("login-window.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) coursesListView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void fillTables(){
        coursesListView.getItems().clear();
        for(Course course : courseManagementSystem.getCourses()){
            coursesListView.getItems().add(course.getId()+": "+course.getCourseName());
        }
    }

    private void openCourseWindow(String s) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("course-window.fxml"));
        Parent root = fxmlLoader.load();

        Course course = courseManagementSystem.getCourses().stream().filter(e -> {String temp = e.getId() + ": "+ e.getCourseName();
            return temp.equals(s);
        }).findFirst().orElse(null);

        CourseWindow courseWindow = fxmlLoader.getController();
        courseWindow.setData(courseManagementSystem,course);

        Scene scene = new Scene(root);
        Stage stage = (Stage) coursesListView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
