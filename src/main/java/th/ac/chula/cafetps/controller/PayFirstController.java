package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import th.ac.chula.cafetps.Utility;

public class PayFirstController {
    @FXML
    private Label amountLabel;

    @FXML
    public TextField getMoneyField;

    @FXML
    public Button next;


    public void setAmount(int amt){
        Utility.formatLabelText(amountLabel,amt);
    }
}
