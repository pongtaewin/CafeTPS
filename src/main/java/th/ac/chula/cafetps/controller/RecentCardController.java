package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import th.ac.chula.cafetps.model.Item;

public class RecentCardController {

    @FXML
    private Label name;

    @FXML
    private Label quantity;

    @FXML
    private Label sweetness;

    public Item item;

    public void setData(Item item){
        sweetness.setText(item.getSweetness());
        name.setText(item.getDisplayName());
        quantity.setText(item.getQuantity()+"");
        this.item = item;
    }



}
