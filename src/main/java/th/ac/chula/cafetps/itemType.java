package th.ac.chula.cafetps;

public enum itemType {
    HOT("ร้อน"),
    ICED("เย็น"),
    FRAPPE("ปั่น"),
    NONE(null);

    private String value;

    itemType(String value){
        this.value = value;
    }

    public String getValue(){ return value;}
}
