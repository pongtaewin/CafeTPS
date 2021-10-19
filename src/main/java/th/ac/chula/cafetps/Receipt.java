package th.ac.chula.cafetps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Receipt {

    public ArrayList<Item> list;

    //maybe change
    public Receipt() {
        list = new ArrayList<>();
    }

    public void addItem(Item item,int pricePerUnit){
        item.setPricePerUnit(pricePerUnit);
        list.add(item);
    }

    public int totalPrice(Helper helper){
        int total = 0;
        for(int i = 0 ;i < list.size();i++){
            total += list.get(i).getQuantity()*list.get(i).getPricePerUnit();
        }
        return total;
    }




}

