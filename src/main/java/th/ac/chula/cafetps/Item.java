package th.ac.chula.cafetps;

public class Item {
    private itemProperty property;
    private int quantity,pricePerUnit;
    private String sweetness,name;

    public Item(String name, itemProperty property, int quantity, String sweetness) {
        this.name = name;
        this.property = property;
        this.quantity = quantity;
        this.sweetness = sweetness;
    }

    public Item(String name, itemProperty property, int quantity, String sweetness, int pricePerUnit) {
        this.property = property;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.sweetness = sweetness;
        this.name = name;
    }

    public itemProperty getProperty() {
        return property;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        if(property.getValue()==null) return name;
        return name+property.getValue();
    }

    public String getOnlyName(){
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
