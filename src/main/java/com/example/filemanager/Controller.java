package com.example.filemanager;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    VBox leftPanel, rightPanel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void btnCopyAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename() != null){
           srcPC = leftPC;
           dstPC = rightPC;
        }
        if(rightPC.getSelectedFilename() != null){
           srcPC = rightPC;
           dstPC = leftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void btnMoveAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFilename() != null){
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFilename() != null){
            srcPC = rightPC;
            dstPC = leftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.move(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            alert.showAndWait();
            //comment
        }
    }

    public void btnDeleteAction(ActionEvent actionEvent) {

    }

    public void btnRenameAction(ActionEvent actionEvent) {

    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }
}