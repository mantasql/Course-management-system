package com.lab.course_management_system.fxControllers;

import com.lab.course_management_system.StartGUI;
import com.lab.course_management_system.control.DatabaseUtils;
import com.lab.course_management_system.control.MessageHandler;
import com.lab.course_management_system.dataStructures.Course;
import com.lab.course_management_system.dataStructures.CourseManagementSystem;
import com.lab.course_management_system.dataStructures.Folder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateFolderForm {
    @FXML
    public TextField folderNameField;

    private CourseManagementSystem courseManagementSystem;
    private Course course;
    private Folder parentFolder;
    private Folder subfolder;

    public void createFolder(ActionEvent actionEvent) throws SQLException, IOException {
        if(parentFolder != null){
            subfolder = new Folder(folderNameField.getText(),parentFolder);
        } else {
            subfolder = new Folder(folderNameField.getText(),null);
        }


        try{
            Connection connection = DatabaseUtils.connect();
            String sql = "INSERT INTO folders (`title`,`date_created`,`date_modified`) VALUES (?,?,?)";
            PreparedStatement insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insert.setString(1,subfolder.getTitle());
            insert.setDate(2, Date.valueOf(subfolder.getDateCreated()));
            insert.setDate(3, Date.valueOf(subfolder.getDateModified()));
            insert.execute();

            ResultSet resultSet = insert.getGeneratedKeys();
            int folderID = 0;
            if(resultSet.next()){
                folderID = resultSet.getInt(1);
            }
            //!!!!
            courseManagementSystem.getCourses().remove(course);
            subfolder.setId(folderID);
            course.getFolders().add(subfolder);
            course.getAllFolders().add(subfolder);
            //!!!!
            DatabaseUtils.disconnectFromDatabase(connection,insert);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(parentFolder == null){
            try{
                Connection connection = DatabaseUtils.connect();
                String sql = "INSERT INTO course_folder (`course_id`,`folder_id`) VALUES (?,?)";
                PreparedStatement insert = connection.prepareStatement(sql);
                insert.setInt(1,course.getId());
                insert.setInt(2,subfolder.getId());
                insert.execute();

                DatabaseUtils.disconnectFromDatabase(connection,insert);

                //!!!!
                courseManagementSystem.getCourses().add(course);
                //!!!!
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                Connection connection = DatabaseUtils.connect();
                String sql = "INSERT INTO folder_subfolder (`folder_id`,`subfolder_id`) VALUES (?,?)";
                PreparedStatement insert = connection.prepareStatement(sql);
                insert.setInt(1,parentFolder.getId());
                insert.setInt(2,subfolder.getId());
                insert.execute();

                DatabaseUtils.disconnectFromDatabase(connection,insert);

                parentFolder.getSubFolders().add(subfolder);
                course.getAllFolders().add(subfolder);

            } catch (Exception e){
                e.printStackTrace();
            }
        }



        MessageHandler.alertMessage("Folder created!");

        if(parentFolder == null){
            returnToCourseWindow();
        } else {
            returnToFolderWindow();
        }
    }

    public void cancel(ActionEvent actionEvent) throws SQLException, IOException {
        if(parentFolder == null){
            returnToCourseWindow();
        } else {
            returnToFolderWindow();
        }
    }

    public void setData(CourseManagementSystem courseManagementSystem, Course course, Folder folder){
        this.courseManagementSystem = courseManagementSystem;
        this.course = course;
        this.parentFolder = folder;
    }

    private void returnToCourseWindow() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("course-window.fxml"));
        fxmlLoader.load();

        CourseWindow courseWindow = fxmlLoader.getController();
        courseWindow.setData(courseManagementSystem,course);

        Stage stage = (Stage) folderNameField.getScene().getWindow();
        stage.close();
    }

    private void returnToFolderWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("folder-window.fxml"));
        fxmlLoader.load();

        FolderWindow folderWindow = fxmlLoader.getController();
        folderWindow.setData(courseManagementSystem,parentFolder,course);

        Stage stage = (Stage) folderNameField.getScene().getWindow();
        stage.close();
    }
}
