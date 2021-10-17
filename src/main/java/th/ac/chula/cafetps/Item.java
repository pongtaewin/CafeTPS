package th.ac.chula.cafetps;

public class Item {
    private itemType type;
    private int quantity,pricePerUnit;
    private String sweetness,name;

    public Item(String name,itemType type,int quantity,String sweetness) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.sweetness = sweetness;
    }

    public itemType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name+sweetness;
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