package th.ac.chula.cafetps.model;

import th.ac.chula.cafetps.constants.ItemProperty;

import java.util.HashMap;

public class PriceTable {

    private final HashMap<String,HashMap<ItemProperty,Integer>> priceTable = new HashMap<>();

    public PriceTable() {

    }

    public void addPrice(String name, ItemProperty type, int price){
        priceTable.putIfAbsent(name,new HashMap<>());
        priceTable.get(name).put(type,price);
    }

    public int getPrice(String name, ItemProperty type) {
        return priceTable.get(name).get(type);
    }
}
