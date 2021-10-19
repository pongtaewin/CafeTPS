package th.ac.chula.cafetps.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import th.ac.chula.cafetps.Helper;
import th.ac.chula.cafetps.User;

import java.io.IOException;

public abstract class SwitchController {

    protected User employee;
    protected Helper helper;
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    public void switchToHome(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/chula/cafetps/home.fxml"));
        root = loader.load();
        HomeController homeController = loader.getController();
        homeController.setHelper(helper);
        homeController.setEmployee(employee);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToMonthlySum(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/chula/cafetps/monthsum.fxml"));
        root = loader.load();
//        MonthSumController monthController = loader.getController();
//        homeController.setHelper(helper);
//        homeController.setEmployee(employee);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToMembership(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/chula/cafetps/membership.fxml"));
        root = loader.load();
        MemberController memberController = loader.getController();
        memberController.setHelper(helper);
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

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }

}
