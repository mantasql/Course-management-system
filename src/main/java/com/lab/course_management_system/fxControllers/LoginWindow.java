package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class LoginWindow {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;

    private CourseManagementSystem courseManagementSystem;

    public void validateAndLoadCourses(ActionEvent actionEvent) throws SQLException, IOException {
        Connection connection = DatabaseUtils.connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users WHERE login = '" + loginField.getText() + "' AND password = '" + passwordField.getText() + "'";
        ResultSet resultSet = statement.executeQuery(sql);

        int userId = 0;
        String userName = null;
        String password = null;
        UserType userType = null;

        while(resultSet.next()){
            userName = resultSet.getString("login");
            userId = resultSet.getInt("id");
            password = resultSet.getString("password");
            userType = UserType.valueOf(resultSet.getString("user_type"));
        }
        User user = new Person(userId,userName,password,userType);

        DatabaseUtils.disconnectFromDatabase(connection,statement);

        if(userName != null){
            courseManagementSystem = new CourseManagementSystem(user,getUserCourses(userId));
            loadCourseWindow();
        } else {
            MessageHandler.alertMessage("Wrong input data\nEntered wrong username or password");
        }
    }

    public void openNewUserForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("sign-up-form.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void loadCourseWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("main-menu-window.fxml"));
        Parent root = fxmlLoader.load();

        MainMenuWindow mainMenuWindow = fxmlLoader.getController();
        mainMenuWindow.setCourseManagementSystem(courseManagementSystem);

        Scene scene = new Scene(root);
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private ArrayList<Course> getUserCourses(int userID) throws SQLException {
        Connection connection = DatabaseUtils.connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT course_id FROM user_course WHERE user_id = " + userID;
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<Integer> courseIds = new ArrayList<>();

        while(resultSet.next()){
            courseIds.add(resultSet.getInt("course_id"));
        }

        ArrayList<Course> courses = new ArrayList<>();
        if(courseIds.size() == 0){
            return courses;
        }

        String sql2 = "SELECT * FROM courses WHERE id IN "+courseIds.toString().replace("[","(").replace("]",")");
        resultSet = statement.executeQuery(sql2);

        int id;
        String name;
        String description;
        LocalDate dateCreated;

        while (resultSet.next()){
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            description = resultSet.getString("description");
            dateCreated = resultSet.getDate("date_created").toLocalDate();

            Course course = new Course(id,name,description,dateCreated);

            setAllAttendingUsersInCourse(course);
            setAllFoldersInCourse(course);

            courses.add(course);
        }

        DatabaseUtils.disconnectFromDatabase(connection,statement);
        return courses;
    }

    private void setAllAttendingUsersInCourse(Course course) throws SQLException {
        Connection connection = DatabaseUtils.connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT user_id FROM user_course WHERE course_id = "+ course.getId();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Integer> allResponsibleUsers = new ArrayList<>();
        while(resultSet.next()){
            allResponsibleUsers.add(resultSet.getInt("user_id"));
        }
        String sql2 = "SELECT * FROM users WHERE id IN "+ allResponsibleUsers.toString().replace("[","(").replace("]",")");
        resultSet = statement.executeQuery(sql2);

        Integer id = null;
        String name = null;
        String surname = null;


        while(resultSet.next()){
            id = resultSet.getInt("id");
            name = resultSet.getString("person_name");
            surname = resultSet.getString("person_surname");

            if(resultSet.getString("user_type").equals("STUDENT")){
                course.getStudents().add(new Person(id,name,surname));
            } else {
                course.getModerators().add(new Person(id,name,surname));
            }
        }
        DatabaseUtils.disconnectFromDatabase(connection,statement);
    }

    private void setAllFoldersInCourse(Course course) throws SQLException {
        Connection connection = DatabaseUtils.connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT folder_id FROM course_folder WHERE course_id = "+ course.getId();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Integer> allFoldersID = new ArrayList<>();

        while (resultSet.next()){
            allFoldersID.add(resultSet.getInt("folder_id"));
        }

        if(allFoldersID.size() == 0){
            DatabaseUtils.disconnectFromDatabase(connection,statement);
            return;
        }

        String sql2 = "SELECT * FROM folders WHERE id IN "+ allFoldersID.toString().replace("[","(").replace("]",")");
        resultSet = statement.executeQuery(sql2);

        Integer id = null;
        String title = null;
        LocalDate dateCreated = null;
        LocalDate dateModified = null;

        while (resultSet.next()){
            id = resultSet.getInt("id");
            title = resultSet.getString("title");
            dateCreated = resultSet.getDate("date_created").toLocalDate();
            dateModified = resultSet.getDate("date_modified").toLocalDate();

            Folder folder = new Folder(id,title,dateCreated,dateModified,null);
            course.getFolders().add(folder);
            setAllFolderSubFolders(folder,course);
            course.getAllFolders().add(folder);
        }
        DatabaseUtils.disconnectFromDatabase(connection,statement);
    }

    private void setAllFolderSubFolders(Folder folder,Course course) throws SQLException {
        Connection connection = DatabaseUtils.connect();
        Statement statement = connection.createStatement();
        String sql = "SELECT subfolder_id FROM folder_subfolder WHERE folder_id = "+ folder.getId();
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<Integer> allSubFolderIDs = new ArrayList<>();

        while(resultSet.next()){
            allSubFolderIDs.add(resultSet.getInt("subfolder_id"));
        }
        if(allSubFolderIDs.size() == 0){
            DatabaseUtils.disconnectFromDatabase(connection,statement);
            return;
        }


        String sql2 = "SELECT * FROM folders WHERE id IN "+ allSubFolderIDs.toString().replace("[","(").replace("]",")");
        resultSet = statement.executeQuery(sql2);

        Integer id = null;
        String title = null;
        LocalDate dateCreated = null;
        LocalDate dateModified = null;

        ArrayList<Folder> allSubFolders = new ArrayList<>();
        while (resultSet.next()){
            id = resultSet.getInt("id");
            title = resultSet.getString("title");
            dateCreated = resultSet.getDate("date_created").toLocalDate();
            dateModified = resultSet.getDate("date_modified").toLocalDate();

            Folder subfolder = new Folder(id,title,dateCreated,dateModified,folder);
            allSubFolders.add(subfolder);
            folder.getSubFolders().add(subfolder);
            course.getAllFolders().add(subfolder);
        }
        DatabaseUtils.disconnectFromDatabase(connection,statement);

        for(int i = 0; i < allSubFolders.size(); i++){
            setAllFolderSubFolders(allSubFolders.get(i),course);
        }
    }
}
