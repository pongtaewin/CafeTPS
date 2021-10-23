package th.ac.chula.cafetps.controller.page;

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
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import th.ac.chula.cafetps.CafeTPSApplication;
import th.ac.chula.cafetps.Utility;
import th.ac.chula.cafetps.constants.ItemProperty;
import th.ac.chula.cafetps.controller.*;
import th.ac.chula.cafetps.helper.DatabaseHelper;
import th.ac.chula.cafetps.model.Item;
import th.ac.chula.cafetps.model.Member;
import th.ac.chula.cafetps.model.MenuItem;
import th.ac.chula.cafetps.model.PickItem;

import java.io.IOException;
import java.util.ArrayList;

public class HomeOrderController extends SwitchController{

    /***
     * TODO: Refactor this part of code.
     */


    @FXML
    private Label memberName;

    @FXML
    private GridPane recentGrid;

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

    @FXML
    private VBox recentBox;


    private Member member;


    private ArrayList<PickItem> data;

    private ObservableList<Item> receiptShow;

    private static final String deletePath = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11H7v-2h10v2z";


    @FXML
    public void initialize(){
        receiptShow = FXCollections.observableArrayList();

        nameItemCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        sweetnessCol.setCellValueFactory(new PropertyValueFactory<>("sweetness"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        receiptTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //add cell of button edit
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory = (TableColumn<Item, String> param) -> {
            // make cell containing buttons
            final TableCell<Item, String> cell = new TableCell<>() {
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
                        deleteIcon.setStyle(" -fx-cursor: hand ;"
                                + "-glyph-size:24px;"
                                + "-fx-fill:#FF0000;"
                                + "-fx-opacity: 54%;");

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Item selectedItem = receiptTable.getSelectionModel().getSelectedItem();
                            receiptTable.getItems().remove(selectedItem);
                            totalPrice.setText(getTotal() + "");
                        });

                        setGraphic(deleteIcon);
                        setText(null);

                    }
                }

            };

            return cell;
        };

        nameItemCol.setCellFactory(param -> {
            TableCell<Item, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

        sweetnessCol.setResizable(false);
        nameItemCol.setResizable(false);
        quantityCol.setResizable(false);
        priceCol.setResizable(false);
        editCol.setResizable(false);

        sweetnessCol.setStyle("-fx-font-size: 14px;");
        editCol.setCellFactory(cellFactory);
        receiptTable.setItems(receiptShow);
        receiptTable.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) receiptTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener(
                    (observable, oldValue, newValue) -> header.setReordering(false));
        });

        totalPrice.setText(getTotal()+"");
    }

    public void init(){
//        member = helper.memberCheck("0842053668");
        MenuItem temp = new MenuItem(databaseManager,member);
        ArrayList<Item> recentOrder = temp.getRecentOrder();
        int column = 0;
        int row = 1;
        if(member.getID().equals("0")){
            memberName.setText("ไม่ได้เป็นสมาชิก");
            recentBox.setVisible(false);
        }else{
            memberName.setText("ลูกค้า คุณ"+member.getName());
            try {
                for(int i = 0;i< 4;i++){
                    FXMLLoader loader = Utility.loadResource(getClass(),"recent_card");
                    AnchorPane pane = loader.load();
                    RecentCardController recentCardController = loader.getController();
                    if(i<recentOrder.size()){
                        recentCardController.setData(recentOrder.get(i));
                        recentGrid.add(pane, i, 1); //(child,column,row)
                        pane.setOnMouseClicked((MouseEvent event) -> {
                            Item item = recentCardController.item;
                            item.setPricePerUnit(databaseManager.getPriceTable().getPrice(item.getName(),item.getProperty()));
                            addItem(recentCardController.item);
                        });
                    }
                }
                recentGrid.setHgap(16);
                recentGrid.setPadding(new Insets(0,36,0,40));
                recentGrid.setMaxWidth(160*recentGrid.getColumnCount());

                column = 0;
                row = 1;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        data = temp.getCoffeeMenu();
        try {
            for (int i = 0; i < data.size(); i++) {
                FXMLLoader fxmlLoader = Utility.loadResource(getClass(),"card");
                AnchorPane anchorPane = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(data.get(i));
                anchorPane.setOnMouseClicked((MouseEvent event) -> {
                    FXMLLoader loader = Utility.loadResource(getClass(),"add_item");
                    Stage addPopupStage = new Stage();
                    try {
                        // TODO : Refactor Code
                        AddItemController controller = getPopupStage(loader,addPopupStage,cardController);
                        controller.addButton.setOnMouseClicked((MouseEvent e) ->{
                            if( controller.propertyBox.getValue()==null || controller.sweetnessBox.getValue()==null ){
                                controller.alertmsg.setText("กรุณาเลือกข้อมูลให้ครบถ้วน");
                            }else{
                                Item item = new Item(cardController.pickItem.getName(),controller.getPropertyFromBox(),controller.getQuantity(),controller.getSweetness());
                                item.setPricePerUnit(databaseManager.getPriceTable().getPrice(item.getName(),item.getProperty()));
                                addItem(item);
                                addPopupStage.close();
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                });
                if (column == 6) {
                    column = 0;
                    row++;
                }
                coffeeGrid.add(anchorPane, column++, row); //(child,column,row)
                coffeeGrid.setHgap(8);
                coffeeGrid.setVgap(16);
                coffeeGrid.setPadding(new Insets(0,36,0,40));
            }
            data = temp.getNonCoffeeMenu();
            column = 0;
            row = 1;
            for (int i = 0; i < data.size(); i++) {
                FXMLLoader fxmlLoader = Utility.loadResource(getClass(),"card");
                AnchorPane anchorPane = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(data.get(i));
                anchorPane.setOnMouseClicked((MouseEvent event) -> {
                    FXMLLoader loader = Utility.loadResource(getClass(),"add_item");
                    Stage addPopupStage = new Stage();
                    try {
                        AddItemController controller = getPopupStage(loader,addPopupStage,cardController);
                        controller.addButton.setOnMouseClicked((MouseEvent e) ->{
                            if(cardController.pickItem.getName().equals("อิตาเลี่ยนโซดา")){
                                if(controller.propertyBox.getValue()==null) controller.alertmsg.setText("กรุณาเลือกข้อมูลให้ครบถ้วน");
                                else{
                                Item item = new Item(cardController.pickItem.getName(), controller.getPropertyFromBox(), controller.getQuantity(),"");
                                item.setPricePerUnit(databaseManager.getPriceTable().getPrice(item.getName(), item.getProperty()));
                                addItem(item);
                                addPopupStage.close();
                                }
                            }
                            else if((controller.propertyBox.getValue()==null || controller.sweetnessBox.getValue()==null)){
                                controller.alertmsg.setText("กรุณาเลือกข้อมูลให้ครบถ้วน");
                            }else {
                                Item item = new Item(cardController.pickItem.getName(), controller.getPropertyFromBox(), controller.getQuantity(), controller.getSweetness());
                                item.setPricePerUnit(databaseManager.getPriceTable().getPrice(item.getName(), item.getProperty()));
                                addItem(item);
                                addPopupStage.close();
                            }
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
                if (column == 6) {
                    column = 0;
                    row++;
                }
                noncoffeeGrid.add(anchorPane, column++, row); //(child,column,row)
            }
            noncoffeeGrid.setHgap(8);
            noncoffeeGrid.setVgap(16);
            noncoffeeGrid.setPadding(new Insets(0,36,0,40));
            data = temp.getBakeryMenu();
            column = 0;
            row = 1;
            for (int i = 0; i < data.size(); i++) {
                FXMLLoader fxmlLoader = Utility.loadResource(getClass(),"card");
                AnchorPane anchorPane = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(data.get(i));
                anchorPane.setOnMouseClicked((MouseEvent event) -> {
                    FXMLLoader loader = Utility.loadResource(getClass(),"add_item");
                    Stage addPopupStage = new Stage();
                    try {
                        AddItemController controller = getPopupStage(loader,addPopupStage,cardController);
                        controller.addButton.setOnMouseClicked((MouseEvent e) ->{
                            Item item = new Item(cardController.pickItem.getName(), ItemProperty.NONE,controller.getQuantity(),"");
                            item.setPricePerUnit(databaseManager.getPriceTable().getPrice(item.getName(),item.getProperty()));
                            addItem(item);
                            addPopupStage.close();
                        });
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                });
                if (column == 6) {
                    column = 0;
                    row++;
                }
                bakeryGrid.add(anchorPane, column++, row); //(child,column,row)
                bakeryGrid.setHgap(8);
                bakeryGrid.setVgap(16);
                bakeryGrid.setPadding(new Insets(0,36,0,40));
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
            alert.getDialogPane().getStylesheets().add(Utility.loadStyleSheet(getClass()));
            alert.setContentText("ไม่มีสินค้า");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        }else {
            int total = getTotal();
            FXMLLoader firstSceneLoader = Utility.loadResource(CafeTPSApplication.class,"pay_first");
            FXMLLoader secondSceneLoader = Utility.loadResource(CafeTPSApplication.class,"pay_second");
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
                    if(temp.equals("")||!DatabaseHelper.isNumeric(temp)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().getStylesheets().add(Utility.loadStyleSheet(getClass()));
                        alert.setContentText("กรุณากรอกจำนวนเงิน");
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.show();
                    }else{
                        int gottenMoney = Integer.parseInt(temp);
                        if(gottenMoney < total) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.getDialogPane().getStylesheets().add(Utility.loadStyleSheet(getClass()));
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
                    databaseManager.insertReceipt(employee,member,getTotal());
                    databaseManager.insertReceiptDetail(receiptShow);
                    member.setPoints(member.getPoints()+paySecondController.getPoint());
                    databaseManager.updatePoint(member);
                    payPopup.close();
                    FXMLLoader loader = Utility.loadResource(getClass(),"home");;
                    try{
                        root = loader.load();
                        HomeController homeController = loader.getController();
                        homeController.setHelper(databaseManager);
                        homeController.setEmployee(employee);
                        scene = new Scene(root);
                        stage.setScene(scene);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
                payPopup.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addItem(Item item){
        receiptShow.add(item);
        totalPrice.setText(getTotal()+"");
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }



    private AddItemController getPopupStage(FXMLLoader loader, Stage addPopupStage, CardController cardController) throws IOException{
        Scene addScene = new Scene(loader.load(), 326, 499);
        AddItemController controller = loader.getController();
        addPopupStage.initModality(Modality.APPLICATION_MODAL);
        addPopupStage.initStyle(StageStyle.UNDECORATED);
        addPopupStage.setScene(addScene);
        controller.setData(cardController.pickItem);
        addPopupStage.show();
        return controller;
    }


}
