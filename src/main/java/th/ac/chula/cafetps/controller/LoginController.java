package th.ac.chula.cafetps.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.ac.chula.cafetps.Helper;
import th.ac.chula.cafetps.User;

import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label alertmsg;

    private Helper helper = new Helper();
    private User employee;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void login(ActionEvent event )throws NoSuchAlgorithmException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        employee = new User(username,password);
        if(helper.login(employee)){
            ;
        }
        else alertmsg.setText("Username or Password is incorrect.");
    }


}
