package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PayFirstController {
    @FXML
    private Label amount;

    @FXML
    public TextField getMoneyField;

    @FXML
    public Button next;


    public void setAmount(int amt){
        amount.setText(amt+"");
    }
}
