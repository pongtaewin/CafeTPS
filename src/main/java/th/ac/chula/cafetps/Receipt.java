package th.ac.chula.cafetps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Receipt {

    private Helper helper;
    private ObservableList<Item> records;
    private ArrayList<Item> list;


    //maybe change
    public Receipt(Helper helper) {
        this.helper = helper;
        list = new ArrayList<>();
        this.records = FXCollections.observableList(list);
    }

    public void remove(int index){
        records.remove(index);
    }

    private void clear() {
        records.clear();
    }

    public int totalPrice(Helper helper){
        int total = 0;
        for(int i = 0 ;i < records.size();i++){
            total += records.get(i).getQuantity()*records.get(i).getPricePerUnit();
        }
        return total;
    }

    public void addItem(Item item){
        item.setPricePerUnit(helper.getPriceTable().getPrice(item.getName(),item.getType()));
        records.add(item);
    }



}

