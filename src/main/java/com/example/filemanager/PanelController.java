package com.example.filemanager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {

    @FXML
    TableView<FileInfo> filesTable;
    @FXML
    ComboBox<String> disksBox;
    @FXML
    TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()){
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getType().getName()));

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getFileName()));

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(p->new SimpleObjectProperty<>((p.getValue().getSize())));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>(){
                @Override
                protected void updateItem(Long item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L)
                            text = "[DIR]";
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Date");
        fileDateColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getLastModified().format(dtf)));

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        filesTable.getSortOrder().add(fileTypeColumn);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText())
                            .resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
                    if (Files.isDirectory(path))
                        updateList(path);
                }
            }
        });

        updateList(Paths.get("/home/user"));
    }

    public void updateList(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Error!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null)
            updateList(upperPath);
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFilename(){
        if(!filesTable.isFocused()){
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }
    public String getCurrentPath(){
        return pathField.getText();
    }
}
