package th.ac.chula.cafetps.controller.page;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import th.ac.chula.cafetps.helper.DatabaseHelper;
import th.ac.chula.cafetps.Utility;
import th.ac.chula.cafetps.model.Member;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeController extends SwitchController {

    @FXML
    private TextField memberField;

    @FXML
    private Label alertmsg;

    @FXML
    private ScrollPane scrollPane;

    private Member member;
    private final String phoneCheck = "^0\\d{9}$";
    private final Pattern phonePattern = Pattern.compile(phoneCheck,Pattern.MULTILINE);

    @FXML
    public void initialize(){
        scrollPane.setDisable(true);
    }

    public void loginMember(ActionEvent event) throws IOException {
        String phoneNum = memberField.getText();
        Matcher matcher = phonePattern.matcher(phoneNum);
        alertmsg.setTextFill(Color.RED);
        if (phoneNum.equals("")) {
            member = new Member("0", "guest", 0);
        } else if (matcher.matches()) {
            member = DatabaseHelper.memberCheck(phoneNum);
            if (member == null) alertmsg.setText("ไม่พบหมายเลขสมาชิก");
        } else {
            alertmsg.setText("กรุุณากรอกหมายเลขให้ถูกต้อง");
        }
        if (member != null) {
                FXMLLoader loader = Utility.loadResource(getClass(),"home_order");
                root = loader.load();
                HomeOrderController homeOrderController = loader.getController();
                homeOrderController.setHelper(databaseManager);
                homeOrderController.setMember(member);
                homeOrderController.setEmployee(employee);
                homeOrderController.setStage(stage);
                homeOrderController.init();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
}
