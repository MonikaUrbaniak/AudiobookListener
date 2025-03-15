package com.example.filefolderfinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FileFolderFinderFX extends Application {

    public static TextField searchField;
    public static TextField extensionField;
    public static TextField startDirField;
    public static Button searchButton;
    private Button browseButton;
    private Button saveAndExitButton;
    public static RadioButton fileOption;
    public static RadioButton folderOption;
    public static RadioButton bothOption;
    public static ListView<String> resultList;

    private static final AtomicReference<String> selectedPathRef = new AtomicReference<>();
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("File & Folder Finder");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label searchLabel = new Label("Nazwa pliku/folderu:");
        searchField = new TextField("audiobooks");

        Label extensionLabel = new Label("Rozszerzenie (np. .mp3, opcjonalnie):");
        extensionField = new TextField();

        Label startDirLabel = new Label("Folder startowy:");
        startDirField = new TextField("C:\\Users\\miaus\\Desktop\\JAVA\\ProjectsJava\\AudiobookListenerFXModules");
        browseButton = new Button("Wybierz folder");

        browseButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Wybierz folder startowy");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                startDirField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        Label searchTypeLabel = new Label("Szukaj:");
        fileOption = new RadioButton("Plik");
        folderOption = new RadioButton("Folder");
        bothOption = new RadioButton("Plik i folder");
        folderOption.setSelected(true);

        ToggleGroup toggleGroup = new ToggleGroup();
        fileOption.setToggleGroup(toggleGroup);
        folderOption.setToggleGroup(toggleGroup);
        bothOption.setToggleGroup(toggleGroup);

        searchButton = new Button("Szukaj");
        resultList = new ListView<>();
        saveAndExitButton = new Button("Zapisz i Wyjdź");
        saveAndExitButton.setDisable(true);

        // Obsługa kliknięcia na wynik w liście
        resultList.setOnMouseClicked(event -> {
            String selectedItem = resultList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                selectedPathRef.set(selectedItem);
                System.out.println("Wybrana ścieżka: " + selectedItem);
                saveAndExitButton.setDisable(false);
            }
        });

        // Obsługa przycisku "Zapisz i Wyjdź"
        saveAndExitButton.setOnAction(e -> {
            if (selectedPathRef.get() != null) {
                System.out.println("Ścieżka zapisana: " + selectedPathRef.get());

                // 1️⃣ Zapisujemy ścieżkę do pliku
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("selected_path.txt"))) {
                    writer.write(selectedPathRef.get());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // 2️⃣ Zamykamy okno aplikacji
                stage.close();
            }
        });

        searchButton.setOnAction(e -> StartSearch.startSearch());

        root.getChildren().addAll(
                searchLabel, searchField,
                extensionLabel, extensionField,
                startDirLabel, startDirField, browseButton,
                searchTypeLabel, fileOption, folderOption, bothOption,
                searchButton, resultList, saveAndExitButton
        );

        primaryStage.setScene(new Scene(root, 600, 500));

        // 🔹 Dodajemy nasłuchiwanie zamknięcia okna
        primaryStage.setOnHidden(event -> selectedPathRef.set(getSelectedPath()));

        primaryStage.show();
    }

//    private void startSearch() {
//        resultList.getItems().clear();
//        searchButton.setDisable(true);
//
//        String nameToFind = searchField.getText().trim();
//        String extension = extensionField.getText().trim();
//        String startDirectory = startDirField.getText().trim();
//        boolean searchFiles = fileOption.isSelected() || bothOption.isSelected();
//        boolean searchFolders = folderOption.isSelected() || bothOption.isSelected();
//
//        if (nameToFind.isEmpty()) {
//            resultList.getItems().add("Podaj nazwę pliku lub folderu!");
//            searchButton.setDisable(false);
//            return;
//        }
//
//        File startDir = new File(startDirectory);
//        if (!startDir.exists() || !startDir.isDirectory()) {
//            resultList.getItems().add("Niepoprawny folder startowy!");
//            searchButton.setDisable(false);
//            return;
//        }
//
//        Task<List<String>> searchTask = new Task<>() {
//            @Override
//            protected List<String> call() {
//                return SearchFilesAndFolders.search(startDir, nameToFind, extension, searchFiles, searchFolders);
//            }
//
//            @Override
//            protected void succeeded() {
//                List<String> results = getValue();
//                if (results.isEmpty()) {
//                    resultList.getItems().add("Nie znaleziono plików ani folderów o podanej nazwie.");
//                } else {
//                    resultList.getItems().addAll(results);
//                }
//                searchButton.setDisable(false);
//            }
//        };
//
//        new Thread(searchTask).start();
//    }
//
//
//    private List<String> searchFilesAndFolders(File directory, String name, String extension, boolean searchFiles, boolean searchFolders) {
//        List<String> foundPaths = new ArrayList<>();
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    if (searchFolders && file.getName().equalsIgnoreCase(name)) {
//                        foundPaths.add(file.getAbsolutePath());
//                    }
//                    foundPaths.addAll(searchFilesAndFolders(file, name, extension, searchFiles, searchFolders));
//                } else {
//                    if (searchFiles && (file.getName().equalsIgnoreCase(name) ||
//                            (file.getName().toLowerCase().contains(name.toLowerCase()) &&
//                                    (extension.isEmpty() || file.getName().toLowerCase().endsWith(extension.toLowerCase()))))) {
//                        foundPaths.add(file.getAbsolutePath());
//                    }
//                }
//            }
//        }
//        return foundPaths;
//    }
    public static String runAndWait() {
        AtomicReference<String> result = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1); // Używamy do synchronizacji

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                FileFolderFinderFX finder = new FileFolderFinderFX();
                finder.start(stage); // ⬅ Uruchamiamy Finder jako nowe okno
                stage.setOnHidden(event -> { // ⬅ Zamknięcie okna odblokowuje wynik
                    result.set(selectedPathRef.get());
                    latch.countDown();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            latch.await(); // ⬅ Czekamy, aż użytkownik wybierze folder i zamknie okno
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

//    // ✅ Poprawiona metoda do uruchamiania i zwracania ścieżki
//    public static String runAndWait() {
//        Platform.runLater(() -> {
//            Stage stage = new Stage();
//            FileFolderFinderFX finder = new FileFolderFinderFX();
//            try {
//                finder.start(stage);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//
//        while (selectedPathRef.get() == null) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return selectedPathRef.get();
//    }

    private String getSelectedPath() {
        return selectedPathRef.get();
    }
}
