package com.lab.course_management_system.control;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class MessageHandler {
    public static void alertMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
