package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import th.ac.chula.cafetps.Helper;
import th.ac.chula.cafetps.MenuItem;
import th.ac.chula.cafetps.PickItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;

public class CoffeeController{

    @FXML
    private Label name;

    @FXML
    private GridPane grid;

    private ArrayList<PickItem> data;

    private EventListener listener;




}
