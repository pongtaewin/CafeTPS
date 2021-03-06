package th.ac.chula.cafetps.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.ac.chula.cafetps.Utility;
import th.ac.chula.cafetps.controller.page.HomeController;
import th.ac.chula.cafetps.helper.DatabaseHelper;
import th.ac.chula.cafetps.helper.DatabaseManager;
import th.ac.chula.cafetps.model.User;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label alertmsg;

    private DatabaseManager databaseManager = new DatabaseManager();
    private User employee;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void login(ActionEvent event) throws NoSuchAlgorithmException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.equals("")||password.equals("")){
            alertmsg.setText("กรุณากรอกชื่อผู้ใช้งานและรหัสผ่าน");
        }else{
            employee = new User(username,password);
            if(DatabaseHelper.login(employee)){
                FXMLLoader loader = Utility.loadResource(getClass(),"home");
                root = loader.load();
                HomeController homeController = loader.getController();
                homeController.setHelper(databaseManager);
                homeController.setEmployee(employee);
                homeController.setStage(getStage());
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else alertmsg.setText("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
        }

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
