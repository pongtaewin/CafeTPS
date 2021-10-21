package th.ac.chula.cafetps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import th.ac.chula.cafetps.controller.LoginController;

import java.io.IOException;

public class CafeTPSApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CafeTPSApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 1024);
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(primaryStage);
        primaryStage.setTitle("CafeTPS");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}