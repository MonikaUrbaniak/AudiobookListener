package com.example.filefolderfinder;

import javafx.concurrent.Task;

import java.io.File;
import java.util.List;

import static com.example.filefolderfinder.FileFolderFinderFX.*;

public class StartSearch {
    public static void startSearch() {
        resultList.getItems().clear();
        searchButton.setDisable(true);

        String nameToFind = searchField.getText().trim();
        String extension = extensionField.getText().trim();
        String startDirectory = startDirField.getText().trim();
        boolean searchFiles = fileOption.isSelected() || bothOption.isSelected();
        boolean searchFolders = folderOption.isSelected() || bothOption.isSelected();

        if (nameToFind.isEmpty()) {
            resultList.getItems().add("Podaj nazwę pliku lub folderu!");
            searchButton.setDisable(false);
            return;
        }

        File startDir = new File(startDirectory);
        if (!startDir.exists() || !startDir.isDirectory()) {
            resultList.getItems().add("Niepoprawny folder startowy!");
            searchButton.setDisable(false);
            return;
        }

        Task<List<String>> searchTask = new Task<>() {
            @Override
            protected List<String> call() {
                return SearchFilesAndFolders.search(startDir, nameToFind, extension, searchFiles, searchFolders);
            }

            @Override
            protected void succeeded() {
                List<String> results = getValue();
                if (results.isEmpty()) {
                    resultList.getItems().add("Nie znaleziono plików ani folderów o podanej nazwie.");
                } else {
                    resultList.getItems().addAll(results);
                }
                searchButton.setDisable(false);
            }
        };

        new Thread(searchTask).start();
    }
}
