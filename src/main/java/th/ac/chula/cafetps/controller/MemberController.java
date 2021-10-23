package th.ac.chula.cafetps.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import th.ac.chula.cafetps.DatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberController extends SwitchController {

    @FXML
    private TextField phoneNumField;

    @FXML
    private TextField fullnameField;

    @FXML
    private ChoiceBox<String> genderBox;

    @FXML
    private TextField DOBField;

    @FXML
    private Label alertmsg;

    ObservableList<String> genderSelector = FXCollections.observableArrayList("ชาย","หญิง","ไม่ระบุ");

    @FXML
    private void initialize(){
        genderBox.setValue("ไม่ระบุ");
        genderBox.setItems(genderSelector);
    }

    private final String dobCheck = "^\\d{4}-\\d{2}-\\d{2}$";
    private final String phoneCheck = "^0\\d{9}$";
    private final Pattern DOBPattern = Pattern.compile(dobCheck,Pattern.MULTILINE);
    private final Pattern phonePattern = Pattern.compile(phoneCheck,Pattern.MULTILINE);



    public void addMember(){
        String m_id = phoneNumField.getText();
        String name = fullnameField.getText();
        String gender = genderBox.getValue();
        String dob = DOBField.getText();
        Matcher DOBmatcher = DOBPattern.matcher(dob);
        Matcher phoneMatcher = phonePattern.matcher(m_id);
        if(phoneMatcher.matches()){
            if(!name.equals("")){
                if(DOBmatcher.matches()){
                    DatabaseHelper.addMember(m_id,name,gender,dob);
                    alertmsg.setTextFill(Color.GREEN);
                    alertmsg.setText("สมัครสมาชิกเสร็จสิ้น");
                }else alertmsg.setText("กรุณากรอกวันเกิดให้ถูกต้อง");
            }else alertmsg.setText("กรุณากรอกชื่อ-นามสกุล");
        }else alertmsg.setText("กรุณากรอกเบอร์โทรศัพท์ให้ถูกต้อง");
    }

}
