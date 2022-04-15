module com.lab.mantas_miliukas_lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.lab.course_management_system to javafx.fxml;
    opens com.lab.course_management_system.fxControllers to javafx.fxml;
    exports com.lab.course_management_system.fxControllers;
    exports com.lab.course_management_system;
}