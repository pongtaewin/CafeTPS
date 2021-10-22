package th.ac.chula.cafetps.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

public class MonthController extends SwitchController{

    @FXML
    private LineChart<Integer,Integer> chart;

    @FXML
    private Label monthNameLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label totalUnitLabel;

    @FXML
    private Label nonUnitLabel;

    @FXML
    private Label coffeeUnitLabel;

    @FXML
    private Label bakeryUnitLabel;

    @FXML
    private Label saleIncomeLabel;

    @FXML
    private Label saleCostLabel;

    @FXML
    private Label rentalCostLabel;

    @FXML
    private Label salaryLabel;

    @FXML
    private Label netTotalLabel;

    public void init(){
        //TODO
    }

}
