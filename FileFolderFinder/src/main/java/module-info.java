module com.example.filefolderfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media; // Jeśli używasz dźwięku

    opens com.example.filefolderfinder to javafx.fxml;
    exports com.example.filefolderfinder;
}
