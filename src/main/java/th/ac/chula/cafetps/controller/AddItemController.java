package th.ac.chula.cafetps.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import th.ac.chula.cafetps.model.PickItem;
import th.ac.chula.cafetps.constants.ItemProperty;

public class AddItemController {

    // TODO: Convert AddItemController into subclasses.

    @FXML
    public Label alertMsg;

    @FXML
    private Label nameLabel;

    @FXML
    public ChoiceBox<ItemProperty> propertyBox;

    @FXML
    public ChoiceBox<String> sweetnessBox;

    @FXML
    public TextField amountField;

    @FXML
    public Button addButton;

    @FXML
    private Button minusButton;

    @FXML
    private Button plusButton;

    @FXML
    private Label amount;

    public void setData(PickItem pickItem){
        nameLabel.setText(pickItem.getName());
        alertMsg.setTextFill(Color.RED);
        propertyBox.setItems(FXCollections.observableArrayList(pickItem.getAvailableProperty()));
        if (!pickItem.isPickSweetness()) sweetnessBox.setDisable(true);
        if(pickItem.getAvailableProperty().size()==0) propertyBox.setDisable(true);
        else sweetnessBox.setItems(FXCollections.observableArrayList(
                "ไม่หวาน",
                "หวานน้อย",
                "หวานกลาง",
                "หวานปกติ"
        ));
    }

    public ItemProperty getPropertyFromBox(){
        return propertyBox.getValue();
    }

    public int getQuantity() {
        return Integer.parseInt(amount.getText());
    }

    public String getSweetness(){
        return sweetnessBox.getValue();
    }

    public void plus(){
        amount.setText((Integer.parseInt(amount.getText())+1)+"");
        if(amount.getText().equals("1")){
            minusButton.setDisable(false);
            addButton.setDisable(false);
        }
    }

    public void minus(){
        amount.setText((Integer.parseInt(amount.getText())-1)+"");
        if(amount.getText().equals("0")){
            minusButton.setDisable(true);
            addButton.setDisable(true);
        }

    }
}
