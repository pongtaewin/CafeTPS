package th.ac.chula.cafetps;

public enum itemProperty {
    HOT("ร้อน"),
    ICED("เย็น"),
    FRAPPE("ปั่น"),
    NONE(null);

    private String value;

    itemProperty(String value){
        this.value = value;
    }

    public String getValue(){ return value;}
}
