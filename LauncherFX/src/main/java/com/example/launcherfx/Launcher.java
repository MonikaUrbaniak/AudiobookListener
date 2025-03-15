package com.example.launcherfx;

import com.example.filefolderfinder.FileFolderFinderFX;
import com.example.AudiobookListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LauncherFX - Zarządzanie aplikacjami");

        Button startFileFinder = new Button("📂 Wybierz folder audiobooków");
        Button startAudiobookPlayer = new Button("🎧 Odtwarzacz audiobooków");
        startAudiobookPlayer.setDisable(true); // Najpierw trzeba wybrać folder

        startFileFinder.setOnAction(e -> {
            new Thread(() -> { // Uruchamiamy w nowym wątku
                String selectedPath = FileFolderFinderFX.runAndWait();
                if (selectedPath != null && !selectedPath.isEmpty()) {
                    Platform.runLater(() -> { // Aktualizacja GUI w wątku JavaFX
                        startAudiobookPlayer.setDisable(false);
                        startAudiobookPlayer.setUserData(selectedPath);
                    });
                }
            }).start();
        });

        startAudiobookPlayer.setOnAction(e -> {
            String path = (String) startAudiobookPlayer.getUserData();
            if (path != null) {
                AudiobookListener.runWithPath(path); // 📌 Poprawne uruchomienie AudiobookListener
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(startFileFinder, startAudiobookPlayer);

        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
