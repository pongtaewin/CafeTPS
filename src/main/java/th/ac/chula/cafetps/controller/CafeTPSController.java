package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CafeTPSController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onEnterButtonClick() {
        welcomeText.setText("Welcome to CafeTPS!");
    }
}