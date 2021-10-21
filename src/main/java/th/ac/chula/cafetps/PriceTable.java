package th.ac.chula.cafetps;

import java.util.HashMap;

public class PriceTable {

    private HashMap<String,HashMap<ItemProperty,Integer>> priceTable;

    public PriceTable() {
        this.priceTable = new HashMap<>();
    }

    public void addPrice(String name, ItemProperty type, int price){
        if(priceTable.containsKey(name)){
            priceTable.get(name).put(type,price);
        }else{
            priceTable.put(name,new HashMap<>());
            priceTable.get(name).put(type,price);
        }
    }

    public int getPrice(String name, ItemProperty type) { return priceTable.get(name).get(type);}
}
