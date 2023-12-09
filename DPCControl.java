package com.example.dataperformancecomparison;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DPCControl {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
