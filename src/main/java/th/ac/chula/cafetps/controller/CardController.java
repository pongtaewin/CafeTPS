package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import th.ac.chula.cafetps.PickItem;

public class CardController {

    @FXML
    private Label name;

    public PickItem pickItem;

    public void setData(PickItem pickItem){
        this.pickItem = pickItem;
        name.setText(this.pickItem.getName());

    }
}
