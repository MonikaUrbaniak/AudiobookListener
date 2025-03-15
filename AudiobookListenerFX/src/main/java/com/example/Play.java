package com.example;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.File;
import static com.example.AudiobookListener.*;


public class Play {
    //Odtwarza wybrany plik audio.
    public static void playSelectedFile() {


        String selectedFile = fileList.getSelectionModel().getSelectedItem();
        if (selectedFile == null) return;

        File audioFile = new File(selectedAudiobookFolder, selectedFile);
        if (!audioFile.exists()) {
            loadAudiobooksAndFiles.showError("Plik nie istnieje: " + audioFile.getAbsolutePath());
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(audioFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
//        MediaView mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setOnReady(() -> {
            progressSlider.setDisable(false);
            double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
            progressSlider.setMax(totalDuration);
            totalTimeLabel.setText(formatTime(Duration.seconds(totalDuration)));
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newValue.toSeconds());
                currentTimeLabel.setText(formatTime(newValue));
            }
        });

        progressSlider.setOnMouseReleased(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            playPauseButton.setText("Odtwórz");
            isPaused = false;
        });

        mediaPlayer.play();
        playPauseButton.setText("Pauza");
        isPaused = false;
    }

    //Przełącza odtwarzanie i pauzę.
    public static void togglePlayPause() {
        if (mediaPlayer == null) return;

        if (isPaused) {
            mediaPlayer.play();
            playPauseButton.setText("Pauza");
        } else {
            mediaPlayer.pause();
            playPauseButton.setText("Odtwórz");
        }

        isPaused = !isPaused;
    }
    //Zatrzymuje odtwarzanie.
    public static void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playPauseButton.setText("Odtwórz");
            progressSlider.setValue(0);
            currentTimeLabel.setText("0:00");
            isPaused = false;
        }
    }
    private static String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
