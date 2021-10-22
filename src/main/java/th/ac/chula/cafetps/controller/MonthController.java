package th.ac.chula.cafetps.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.time.YearMonth;
import java.util.*;
import com.sun.javafx.charts.Legend;


public class MonthController extends SwitchController {

    @FXML
    private LineChart<String, Integer> chart;

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
    private Label netTotalamount;

    @FXML
    private Label netTotalLabel;

    @FXML
    private ComboBox<String> selectorBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private NumberAxis yaxis;

    public static final String[] monthConvertor = new String[]{"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

    public void init() {
        boxInit();
        setMonthAndYear();
        setSellUnit();
        setIncomeCostProfit();
        setChart();
    }

    private void boxInit(){
        ArrayList<String> values = helper.getDistinctYearMonth();
        selectorBox.setItems(FXCollections.observableArrayList(values));
        selectorBox.setValue(values.get(values.size()-1));
    }

    public void setMonthAndYear(){
        String[] temp = selectorBox.getValue().split("-");
        String year = temp[0];
        int month = Integer.parseInt(temp[1]);
        monthNameLabel.setText(monthConvertor[month-1]);
        yearLabel.setText(year);
    }

    public void setSellUnit(){
        HashMap<String,Integer> values = helper.getSellUnit(selectorBox.getValue());
        bakeryUnitLabel.setText(values.get("bakery")+"");
        coffeeUnitLabel.setText(values.get("coffee")+"");
        nonUnitLabel.setText(values.get("noncoffee")+"");
        totalUnitLabel.setText(values.get("bakery")+values.get("coffee")+values.get("noncoffee")+"");
    }

    public void setIncomeCostProfit(){
        ArrayList<Integer> values = helper.getIncomeAndCost(selectorBox.getValue());
        int income = values.get(0);
        int sale_cost = values.get(1);
        int salary = helper.getEmployeeTotalSalary();
        int rental = helper.getRentalCost();
        int netTotal = income-(sale_cost+salary+rental);
        saleIncomeLabel.setText(income+"");
        saleCostLabel.setText(sale_cost+"");
        rentalCostLabel.setText(rental+"");
        salaryLabel.setText(salary+"");
        if(netTotal<0){
            netTotalLabel.setText("ขาดทุนสุทธิ");
            //TODO ใส่สี css แทนนะ
            netTotalamount.setTextFill(Color.RED);
        }else netTotalamount.setTextFill(Color.GREEN);
        netTotalamount.setText(netTotal+"");
    }

    private int[] getYearMonth(){
        return Arrays.stream(selectorBox.getValue().split("-")).mapToInt((x) -> Integer.parseInt(x)).toArray();
    }

    public void setChart(){
        HashMap<String,ArrayList<XYChart.Data<String,Integer>>> values = helper.getChartData(selectorBox.getValue());
        int[] touse = getYearMonth();
        int[] maxY = {Integer.MIN_VALUE};
        YearMonth yearMonth = YearMonth.of(touse[0],touse[1]);

        XYChart.Series<String,Integer> mockLine = new XYChart.Series<>();
        for(int i = 1 ;i<yearMonth.lengthOfMonth()+1;i++){
            mockLine.getData().add(new XYChart.Data<String,Integer>(i+"",0));
        }
        XYChart.Series<String,Integer> bakeryline = new XYChart.Series<>();
        XYChart.Series<String,Integer> coffeeline = new XYChart.Series<>();
        XYChart.Series<String,Integer> noncoffeeline = new XYChart.Series<>();
        bakeryline.getData().addAll(values.get("bakery"));
        coffeeline.getData().addAll(values.get("coffee"));
        noncoffeeline.getData().addAll(values.get("noncoffee"));
        bakeryline.setName("Bakery");
        coffeeline.setName("Coffee");
        noncoffeeline.setName("Non-Coffee");
        chart.getData().add(mockLine);
        chart.getData().addAll(bakeryline,coffeeline,noncoffeeline);
        chart.getData().forEach((series) -> {
           int temp = series.getData().stream().max(Comparator.comparing(XYChart.Data::getYValue)).get().getYValue();
           if(temp > maxY[0]) maxY[0] = temp;
        });
        yaxis.setAutoRanging(false);
        yaxis.setUpperBound((maxY[0]/5+1)*5);
        chart.getData().remove(0);
        chart.setAnimated(true);
        for (Node n : chart.getChildrenUnmodifiable()) {
            if (n instanceof Legend) {
                Legend l = (Legend) n;
                for (Legend.LegendItem li : l.getItems()) {
                    for (XYChart.Series<String, Integer> s : chart.getData()) {
                        if (s.getName().equals(li.getText())) {
                            li.getSymbol().setCursor(Cursor.HAND); // Hint user that legend symbol is clickable
                            li.getSymbol().setOnMouseClicked(me -> {
                                if (me.getButton() == MouseButton.PRIMARY) {
                                    s.getNode().setVisible(!s.getNode().isVisible()); // Toggle visibility of line
                                    for (XYChart.Data<String, Integer> d : s.getData()) {
                                        if (d.getNode() != null) {
                                            d.getNode().setVisible(s.getNode().isVisible()); // Toggle visibility of every node in the series
                                        }
                                    }
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }
    }

    public void changeData(){
        setMonthAndYear();
        setSellUnit();
        setIncomeCostProfit();
        chart.getData().clear();
        setChart();
    }



}
