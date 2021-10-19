package th.ac.chula.cafetps.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import th.ac.chula.cafetps.Helper;
import th.ac.chula.cafetps.Member;
import th.ac.chula.cafetps.MenuItem;
import th.ac.chula.cafetps.PickItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeController extends SwitchController {

    @FXML
    private TextField memberField;

    @FXML
    private Label alertmsg;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane coffeeGrid;

    @FXML
    private GridPane noncoffeeGrid;

    @FXML
    private GridPane bakeryGrid;




    private Member member;
    private ArrayList<PickItem> data;
    private final String phoneCheck = "^0\\d{9}$";
    private final Pattern phonePattern = Pattern.compile(phoneCheck,Pattern.MULTILINE);


    public void init(){
        MenuItem temp = new MenuItem(helper);
        data = temp.getCoffeeMenu();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < data.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/th/ac/chula/cafetps/card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
//
                CardController cardController = fxmlLoader.getController();
//                must add listener to pop up
                cardController.setData(data.get(i));

                if (column == 6) {
                    column = 0;
                    row++;
                }
                coffeeGrid.add(anchorPane, column++, row); //(child,column,row)
                coffeeGrid.setHgap(8);
                coffeeGrid.setVgap(16);
                coffeeGrid.setPadding(new Insets(0,36,0,40));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginMember(){
        String phoneNum = memberField.getText();
        Matcher matcher = phonePattern.matcher(phoneNum);
        if(phoneNum.equals("")){
            member = new Member("0","guest",0);
        }else if(matcher.matches()){
            member = helper.memberCheck(phoneNum);
            if(member == null) alertmsg.setText("ไม่พบหมายเลขสมาชิก");
            else {
                alertmsg.setTextFill(Color.GREEN);
                alertmsg.setText("OK");
            }
        }else alertmsg.setText("กรุุณากรอกหมายเลขให้ถูกต้อง");
    }

}
