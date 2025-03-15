package com.example.filefolderfinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFilesAndFolders {

    public static List<String> search(File directory, String name, String extension, boolean searchFiles, boolean searchFolders) {
        List<String> foundPaths = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (searchFolders && file.getName().equalsIgnoreCase(name)) {
                        foundPaths.add(file.getAbsolutePath());
                    }
                    foundPaths.addAll(search(file, name, extension, searchFiles, searchFolders));
                } else {
                    if (searchFiles && (file.getName().equalsIgnoreCase(name) ||
                            (file.getName().toLowerCase().contains(name.toLowerCase()) &&
                                    (extension.isEmpty() || file.getName().toLowerCase().endsWith(extension.toLowerCase()))))) {
                        foundPaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return foundPaths;
    }
}
