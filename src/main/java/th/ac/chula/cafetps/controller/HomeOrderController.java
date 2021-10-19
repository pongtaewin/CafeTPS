package th.ac.chula.cafetps.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import th.ac.chula.cafetps.*;
import th.ac.chula.cafetps.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

public class HomeOrderController extends SwitchController{


    @FXML
    private Label memberName;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane coffeeGrid;

    @FXML
    private GridPane noncoffeeGrid;

    @FXML
    private GridPane bakeryGrid;

    @FXML
    private TableView<Item> receiptTable;

    @FXML
    private TableColumn<Item,String> nameItemCol;

    @FXML
    private TableColumn<Item,String> sweetnessCol;

    @FXML
    private TableColumn<Item,Integer> quantityCol;

    @FXML
    private TableColumn<Item,Integer> priceCol;

    @FXML
    private TableColumn<Item,String> editCol;

    @FXML
    private Label totalPrice;


    private Member member;

    private ArrayList<PickItem> data;

    private ObservableList<Item> receiptShow;

    private Receipt receipt;

    private static final String deletePath = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11H7v-2h10v2z";


    @FXML
    public void initialize(){
        receipt = new Receipt();
        receiptShow = FXCollections.observableArrayList(
                new Item("แงว",itemType.HOT,2,"นิดนุง",50)
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meowdsadsadasdasdsa",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50),
//                new Item("meow",itemType.HOT,2,"นิดนุง",50)
        );

        nameItemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        sweetnessCol.setCellValueFactory(new PropertyValueFactory<>("sweetness"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        receiptTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        //add cell of button edit
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory = (TableColumn<Item, String> param) -> {
            // make cell containing buttons
            final TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        SVGPath deleteIcon = new SVGPath();
                        deleteIcon.setContent(deletePath);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:24px;"
                                        + "-fx-fill:#FF0000;"
                                        + "-fx-opacity: 54%;"
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Item selectedItem = receiptTable.getSelectionModel().getSelectedItem();
                            receiptTable.getItems().remove(selectedItem);
                            totalPrice.setText(getTotal()+"");
                        });

                        setGraphic(deleteIcon);
                        setText(null);

                    }
                }

            };

            return cell;
        };

        nameItemCol.setCellFactory(new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {

            @Override
            public TableCell<Item, String> call(
                    TableColumn<Item, String> param) {
                TableCell<Item, String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(cell.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell ;
            }
        });

        sweetnessCol.setResizable(false);
        nameItemCol.setResizable(false);
        quantityCol.setResizable(false);
        priceCol.setResizable(false);
        editCol.setResizable(false);

        sweetnessCol.setStyle("-fx-font-size: 14px;");
        editCol.setCellFactory(cellFactory);
        receiptTable.setItems(receiptShow);
        receiptTable.widthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {
                TableHeaderRow header = (TableHeaderRow) receiptTable.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });



        member = new Member();
        member.setMemberName("อาธร");
        //mock
        totalPrice.setText(getTotal()+"");

        if(!member.getMemberName().equals("guest")){
            memberName.setText("ลูกค้า คุณ"+member.getMemberName());
        }
        helper = new Helper();
        init();
    }

    public void init(){
        MenuItem temp = new MenuItem(helper);
        data = temp.getCoffeeMenu();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < data.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/th/ac/chula/cafetps/card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
//
                CardController cardController = fxmlLoader.getController();
//                must add listener to pop up
                cardController.setData(data.get(i));

                if (column == 6) {
                    column = 0;
                    row++;
                }
                coffeeGrid.add(anchorPane, column++, row); //(child,column,row)
                coffeeGrid.setHgap(8);
                coffeeGrid.setVgap(16);
                coffeeGrid.setPadding(new Insets(0,36,0,40));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotal(){
        int total = 0;
        for(Item item : receiptShow){
            total += item.getPrice();
        }
        return total;
    }

    public void submitOrder() throws InterruptedException{
        if(receiptShow.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/th/ac/chula/cafetps/stylesheet.css").toExternalForm());
            alert.setContentText("ไม่มีสินค้า");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        }else {
            int total = getTotal();
            FXMLLoader firstSceneLoader = new FXMLLoader(CafeTPSApplication.class.getResource("/th/ac/chula/cafetps/pay_first.fxml"));
            FXMLLoader secondSceneLoader = new FXMLLoader(CafeTPSApplication.class.getResource("/th/ac/chula/cafetps/pay_second.fxml"));
            try {
                Scene pay1Scene = new Scene(firstSceneLoader.load(), 326, 448);
                Scene pay2Scene = new Scene(secondSceneLoader.load(), 326, 448);
                PayFirstController payFirstController = firstSceneLoader.getController();
                PaySecondController paySecondController = secondSceneLoader.getController();
                payFirstController.setAmount(total);
                Stage payPopup = new Stage();
                payPopup.initModality(Modality.APPLICATION_MODAL);
                payPopup.initStyle(StageStyle.UNDECORATED);
                payPopup.setScene(pay1Scene);
                payFirstController.next.setOnMouseClicked((MouseEvent event) -> {
                    String temp = payFirstController.getMoneyField.getText();
                    if(temp.equals("")||!helper.isNumeric(temp)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().getStylesheets().add(getClass().getResource("/th/ac/chula/cafetps/stylesheet.css").toExternalForm());
                        alert.setContentText("กรุณากรอกจำนวนเงิน");
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.show();
                    }else{
                        int gottenMoney = Integer.parseInt(temp);
                        if(gottenMoney < total) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.getDialogPane().getStylesheets().add(getClass().getResource("/th/ac/chula/cafetps/stylesheet.css").toExternalForm());
                            alert.setContentText("กรุณารับเงินให้ถูกต้อง");
                            alert.initStyle(StageStyle.UNDECORATED);
                            alert.show();
                        }else{
                            paySecondController.init(gottenMoney, total);
                            payPopup.setScene(pay2Scene);
                        }
                    }
                });
                paySecondController.submit.setOnMouseClicked((MouseEvent event) -> {
                    //insert receipt
                    //get receipt id
                    //insert receipt_detail
                    //close stage
                });
                payPopup.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
