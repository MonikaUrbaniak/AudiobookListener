package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadPath {
    public static String load() {
        try (BufferedReader reader = new BufferedReader(new FileReader("selected_path.txt"))) {
            return reader.readLine(); // Wczytujemy pierwszą linię pliku
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Jeśli plik nie istnieje lub błąd odczytu
        }
    }
}
