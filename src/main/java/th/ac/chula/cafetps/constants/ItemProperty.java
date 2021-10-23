package th.ac.chula.cafetps.constants;

public enum ItemProperty {
    HOT("ร้อน"),
    ICED("เย็น"),
    FRAPPE("ปั่น"),
    NONE(null);

    private String value;

    ItemProperty(String value){
        this.value = value;
    }

    public static ItemProperty fromString(String string){
        return switch (string) {
            case "hot" -> ItemProperty.HOT;
            case "iced" -> ItemProperty.ICED;
            case "frappe" -> ItemProperty.FRAPPE;
            default -> ItemProperty.NONE;
        };
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
