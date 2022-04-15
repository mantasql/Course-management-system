package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CourseWindow implements Initializable{
    @FXML
    public Label courseNameLabel;
    @FXML
    public Menu adminMenuBar;
    @FXML
    public ListView<String> studentsTreeView;
    @FXML
    public TreeView<String> folderTreeView;

    private CourseManagementSystem courseManagementSystem;
    private Course course;

    private ChangeListener<TreeItem> changeListener;


    //TODO: REMOVE TREEVIEW AND CHANGE TO VIEWLIST

    public void goToMenu(ActionEvent actionEvent) throws IOException {
        goBackToMenu();
    }

    public void exitApplication(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void addNewModerator(ActionEvent actionEvent) {
    }

    public void addNewStudent(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("add-student-form.fxml"));
        Parent root = fxmlLoader.load();

        AddStudentForm addStudentForm = fxmlLoader.getController();
        addStudentForm.setData(courseManagementSystem,course);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add student form");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fillTables();
    }

    public void removeStudent(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("remove-student-form.fxml"));
        Parent root = fxmlLoader.load();

        RemoveStudentForm removeStudentForm = fxmlLoader.getController();
        removeStudentForm.setData(courseManagementSystem,course);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Remove student form");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fillTables();
    }

    public void deleteThisCourse(ActionEvent actionEvent) throws SQLException, IOException {
        Connection connection = null;
        Statement statement = null;

        connection = DatabaseUtils.connect();
        statement = connection.createStatement();
        String sql = "DELETE FROM courses WHERE id = "+ course.getId();
        statement.executeUpdate(sql);

        DatabaseUtils.disconnectFromDatabase(connection,statement);
        courseManagementSystem.getCourses().remove(course);
        MessageHandler.alertMessage("Course deleted successfully");
        goBackToMenu();
    }

    private void goBackToMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("main-menu-window.fxml"));
        Parent root = fxmlLoader.load();

        MainMenuWindow mainMenuWindow = fxmlLoader.getController();
        mainMenuWindow.setCourseManagementSystem(courseManagementSystem);

        Scene scene = new Scene(root);
        Stage stage = (Stage) courseNameLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setData(CourseManagementSystem courseManagementSystem,Course course) throws SQLException {
        this.courseManagementSystem = courseManagementSystem;
        this.course = course;
        courseNameLabel.setText(course.getId() +": "+ course.getCourseName());

        adminMenuBar.setVisible(courseManagementSystem.getLoggedInUser().getUserType() != UserType.STUDENT);

        fillTables();
        fillFolders();
    }

    private void addFolderTreeListener(){
        folderTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem> observableValue, TreeItem treeItem, TreeItem t1) {
                try {
                    openFolderWindow(t1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTreeListener(){
        changeListener = new ChangeListener<TreeItem>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem> observableValue, TreeItem treeItem, TreeItem t1) {
                try {
                    openFolderWindow(t1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        folderTreeView.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void removeTreeListener(){
        folderTreeView.getSelectionModel().selectedItemProperty().removeListener(changeListener);
    }

    private void fillTables() {
        studentsTreeView.getItems().clear();
        for(Person person : course.getStudents()){
            studentsTreeView.getItems().add(person.getId() + ": "+ person.getName() + " "+ person.getSurname());
        }
    }

    public void createNewFolder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("create-folder-form.fxml"));
        Parent root = fxmlLoader.load();

        CreateFolderForm createFolderForm= fxmlLoader.getController();
        createFolderForm.setData(courseManagementSystem,course,null);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New folder");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        removeTreeListener();
        fillFolders();
        setTreeListener();
    }

    private void fillFolders(){
        TreeItem<String> rootItem = new TreeItem<>(course.getCourseName());

        for (Folder file : course.getFolders()) {
            createTree(rootItem,file);
        }

        folderTreeView.setRoot(rootItem);
        folderTreeView.getRoot().setExpanded(true);
    }

    private void createTree(TreeItem<String> parent, Folder folder) {
        TreeItem<String> treeItem = new TreeItem<>(folder.getTitle());
        parent.getChildren().add(treeItem);
        for (Folder f : folder.getSubFolders()) {
            createTree(treeItem,f);
        }
    }

    private void openFolderWindow(TreeItem item) throws IOException {
        if(item.getValue() != course.getCourseName()){
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("folder-window.fxml"));
            Parent root = fxmlLoader.load();

            Folder newFolder = course.getAllFolders().stream().filter(e -> e.getTitle().equals(item.getValue())).findFirst().orElse(null);

            FolderWindow folderWindow = fxmlLoader.getController();
            folderWindow.setData(courseManagementSystem, newFolder, course);

            Scene scene = new Scene(root);
            Stage stage = (Stage) courseNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //addFolderTreeListener();
        setTreeListener();
    }
}
