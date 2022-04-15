package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.dataStructures.Course;
import com.lab.course_management_system.dataStructures.CourseManagementSystem;
import com.lab.course_management_system.dataStructures.Folder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FolderWindow implements Initializable {
    @FXML
    public Label folderName;
    @FXML
    public TreeView<String> folderTreeView;
    private CourseManagementSystem courseManagementSystem;
    private Folder folder;
    private Course course;

    private ChangeListener<TreeItem> changeListener;

    public void setData(CourseManagementSystem courseManagementSystem, Folder folder, Course course){
       this.courseManagementSystem = courseManagementSystem;
       this.folder = folder;
       this.course = course;
       folderName.setText(folder.getTitle());
       fillFolders();
       folderTreeView.refresh();
    }

//    private void addTreeListener(){
//        folderTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
//            @Override
//            public void changed(ObservableValue<? extends TreeItem> observableValue, TreeItem treeItem, TreeItem t1) {
//                try {
//                    openFolderWindow(t1);
//                } catch (IOException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

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

    private void openFolderWindow(TreeItem item) throws IOException, SQLException {
        if(item.getValue() != folder.getTitle()){
            if(item.getValue() == course.getCourseName()){
                FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("course-window.fxml"));
                Parent root = fxmlLoader.load();

                CourseWindow courseWindow = fxmlLoader.getController();
                courseWindow.setData(courseManagementSystem,course);

                Scene scene = new Scene(root);
                Stage stage = (Stage) folderName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("folder-window.fxml"));
                Parent root = fxmlLoader.load();

                Folder newFolder = course.getAllFolders().stream().filter(e -> e.getTitle().equals(item.getValue())).findFirst().orElse(null);

                FolderWindow folderWindow = fxmlLoader.getController();
                folderWindow.setData(courseManagementSystem, newFolder, course);

                Scene scene = new Scene(root);
                Stage stage = (Stage) folderName.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public void createNewFolder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("create-folder-form.fxml"));
        Parent root = fxmlLoader.load();

        CreateFolderForm createFolderForm = fxmlLoader.getController();
        createFolderForm.setData(courseManagementSystem,course,folder);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New folder");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        folderTreeView.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //addTreeListener();
        setTreeListener();
    }

    private void setTreeListener(){
        changeListener = new ChangeListener<TreeItem>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem> observableValue, TreeItem treeItem, TreeItem t1) {
                try {
                    openFolderWindow(t1);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        folderTreeView.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void removeTreeListener(){
        folderTreeView.getSelectionModel().selectedItemProperty().removeListener(changeListener);
    }
}
