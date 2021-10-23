package th.ac.chula.cafetps.model;

import th.ac.chula.cafetps.constants.ItemProperty;

public class Item {
    private ItemProperty property;
    private int quantity;
    private int pricePerUnit;
    private String sweetness;
    private String name;

    public Item(String name, ItemProperty property, int quantity, String sweetness) {
        this.name = name;
        this.property = property;
        this.quantity = quantity;
        this.sweetness = sweetness;
    }

    public Item(String name, ItemProperty property, int quantity, String sweetness, int pricePerUnit) {
        this(name, property, quantity, sweetness);
        this.pricePerUnit = pricePerUnit;

    }

    public ItemProperty getProperty() {
        return property;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDisplayName() {
        if(property.getValue()==null) return name;
        return name + property.getValue();
    }

    public String getName(){
        return name;
    }

    public String getSweetness() {
        return sweetness;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getPrice(){
        return pricePerUnit * quantity;
    }
}
