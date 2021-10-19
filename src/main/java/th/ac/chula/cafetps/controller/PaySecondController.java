package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PaySecondController {

    @FXML
    private Label gottenMoney;

    @FXML
    private Label total;

    @FXML
    private Label discount;

    @FXML
    private Label changeAmt;

    @FXML
    public Button submit;

    public void init(int gottenMoney,int total){
        int discountAmt = (int)((double)total*0.10);
        this.gottenMoney.setText(gottenMoney+"");
        this.total.setText(total+"");
        discount.setText(discountAmt+"");
        changeAmt.setText((gottenMoney-total+discountAmt)+"");
    }



}
