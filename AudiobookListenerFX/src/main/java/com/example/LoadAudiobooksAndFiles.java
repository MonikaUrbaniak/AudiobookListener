package com.example;
import javafx.scene.control.Alert;

import java.io.File;
import static com.example.AudiobookListener.audiobookList;
import static com.example.AudiobookListener.fileList;
import static com.example.AudiobookListener.audiobookBaseFolder;
import static com.example.AudiobookListener.selectedAudiobookFolder;
public class LoadAudiobooksAndFiles {
    //Wczytuje dostępne audiobooki z folderu bazowego.
    public void loadAudiobooks() {
        audiobookList.getItems().clear();
        fileList.getItems().clear();

        File[] folders = audiobookBaseFolder.listFiles(File::isDirectory);
        if (folders != null) {
            for (File folder : folders) {
                audiobookList.getItems().add(folder.getName());
            }
        } else {
            showError("Brak dostępnych folderów z audiobookami.");
        }
    }
    //Wczytuje pliki audio dla wybranego audiobooka.
    public void loadAudioFiles() {
        String selectedAudiobook = audiobookList.getSelectionModel().getSelectedItem();
        if (selectedAudiobook == null) return;

        selectedAudiobookFolder = new File(audiobookBaseFolder, selectedAudiobook);
        fileList.getItems().clear();

        File[] audioFiles = selectedAudiobookFolder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav"));
        if (audioFiles != null) {
            for (File file : audioFiles) {
                fileList.getItems().add(file.getName());
            }
        } else {
            showError("Brak plików audio w wybranym folderze.");
        }
    }
     //Wyświetla okno dialogowe z błędem.
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
