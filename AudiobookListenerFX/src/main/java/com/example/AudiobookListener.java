package com.example;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import java.io.File;
public class AudiobookListener extends Application {
    public static ListView<String> audiobookList;
    public static ListView<String> fileList;
    public static File audiobookBaseFolder;
    public static File selectedAudiobookFolder;
    public static MediaPlayer mediaPlayer;
    public static Slider progressSlider;
    public static Button playPauseButton;
    public static boolean isPaused = false;
    public static Label currentTimeLabel;
    public static Label totalTimeLabel;
    private static String selectedPath;
    public static LoadAudiobooksAndFiles loadAudiobooksAndFiles;
//    public static String selectedPath;


    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(10);

        audiobookList = new ListView<>();
        fileList = new ListView<>();
        playPauseButton = new Button("Odtwórz");
        Button stopButton = new Button("Stop");

        progressSlider = new Slider();
        progressSlider.setDisable(true);

        currentTimeLabel = new Label("0:00");
        totalTimeLabel = new Label("0:00");

        HBox progressBox = new HBox(10);
        progressBox.getChildren().addAll(currentTimeLabel, progressSlider, totalTimeLabel);
        progressBox.setSpacing(10);
        progressBox.setPrefWidth(500);
        progressSlider.setMinWidth(350);

        LoadAudiobooksAndFiles loadAudiobooksAndFiles = new LoadAudiobooksAndFiles();
        audiobookList.setOnMouseClicked(event -> loadAudiobooksAndFiles.loadAudioFiles());
        fileList.setOnMouseClicked(event -> Play.playSelectedFile());

        playPauseButton.setOnAction(event -> Play.togglePlayPause());
        stopButton.setOnAction(event -> Play.stopPlayback());

        root.getChildren().addAll(audiobookList, fileList, progressBox, playPauseButton, stopButton);

        primaryStage.setTitle("AudiobookListener");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();

        initializeAudiobookFolder();
    }
     //Ustawia domyślną ścieżkę do folderu z audiobookami.
     private void initializeAudiobookFolder() {
         // 1️⃣ Pobieramy zapisany `selectedPath`
         LoadPath loadPath = new LoadPath();
         selectedPath = loadPath.load(); // 🔄 Teraz poprawnie zapisuje do `AudiobookListener.selectedPath`

         // 2️⃣ Sprawdzamy, czy `selectedPath` nie jest null
         if (selectedPath == null || selectedPath.isEmpty()) {
             showError("Nie wybrano folderu z audiobookami!");
             Platform.exit();
             return;
         }

         // 3️⃣ Tworzymy folder na podstawie `selectedPath`
         audiobookBaseFolder = new File(selectedPath);
         LoadAudiobooksAndFiles loadAudiobooksAndFiles = new LoadAudiobooksAndFiles();

         if (audiobookBaseFolder.exists() && audiobookBaseFolder.isDirectory()) {
             loadAudiobooksAndFiles.loadAudiobooks();
         } else {
             showError("Folder audiobooków nie istnieje lub nie jest katalogiem:\n" + audiobookBaseFolder.getAbsolutePath());
         }
     }

    /**
     * Wyświetla błąd w oknie dialogowym.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void runWithPath(String path) {
        selectedPath = path;

        // Jeśli JavaFX już działa, uruchamiamy nową scenę w tym samym wątku
        if (Platform.isFxApplicationThread()) {
            Stage stage = new Stage();
            try {
                new AudiobookListener().start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                Stage stage = new Stage();
                try {
                    new AudiobookListener().start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

//    public static void runWithPath(String path) {
//        selectedPath = path;
//        launch();
//    }
}