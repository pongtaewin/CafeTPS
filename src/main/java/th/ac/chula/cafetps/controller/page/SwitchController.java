package th.ac.chula.cafetps.controller.page;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import th.ac.chula.cafetps.helper.DatabaseManager;
import th.ac.chula.cafetps.Utility;
import th.ac.chula.cafetps.model.User;

import java.io.IOException;

public abstract class SwitchController {

    protected User employee;
    protected DatabaseManager databaseManager;
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    public void switchToHome(ActionEvent event)throws IOException{
        FXMLLoader loader = Utility.loadResource(getClass(),"home");
        root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setHelper(databaseManager);
        homeController.setEmployee(employee);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToMonthlySum(ActionEvent event)throws IOException {
        FXMLLoader loader = Utility.loadResource(getClass(),"summary");
        root = loader.load();
        MonthController monthController = loader.getController();
        monthController.setHelper(databaseManager);
        monthController.setEmployee(employee);
        monthController.init();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToMembership(ActionEvent event)throws IOException {
        FXMLLoader loader = Utility.loadResource(getClass(),"membership");
        root = loader.load();
        MemberController memberController = loader.getController();
        memberController.setHelper(databaseManager);
        memberController.setEmployee(employee);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public DatabaseManager getHelper() {
        return databaseManager;
    }

    public void setHelper(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
