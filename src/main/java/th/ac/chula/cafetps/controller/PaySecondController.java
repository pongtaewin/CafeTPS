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
    private Label changeAmt;

    @FXML
    public Button submit;

    public void init(int gottenMoney,int total){
        this.gottenMoney.setText(gottenMoney+"");
        this.total.setText(total+"");
        changeAmt.setText((gottenMoney-total)+"");
    }

    public int getNetTotal(){
        return Integer.parseInt(total.getText());
    }

    public double getPoint(){
        return getNetTotal()*0.025;
    }



}
