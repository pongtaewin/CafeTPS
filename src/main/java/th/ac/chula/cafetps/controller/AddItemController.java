package th.ac.chula.cafetps.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import th.ac.chula.cafetps.PickItem;
import th.ac.chula.cafetps.itemProperty;

public class AddItemController {

    @FXML
    public Label alertmsg;

    @FXML
    private Label nameLabel;

    @FXML
    public ChoiceBox propertyBox;

    @FXML
    public ChoiceBox sweetnessBox;

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
        alertmsg.setTextFill(Color.RED);
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

    public itemProperty getPropertyFromBox(){
        return (itemProperty) propertyBox.getValue();
    }

    public int getQuantity() {
        return Integer.parseInt(amount.getText());
    }

    public String getSweetness(){
        return sweetnessBox.getValue().toString();
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
