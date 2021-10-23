package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import th.ac.chula.cafetps.model.PickItem;

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
