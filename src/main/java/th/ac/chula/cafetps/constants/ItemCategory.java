package th.ac.chula.cafetps.constants;

public enum ItemCategory {
    COFFEE("Coffee"),
    NONCOFFEE("Non-Coffee"),
    BAKERY("Bakery");

    private  String value;

    ItemCategory(final String value){
        this.value = value;
    }

    public static ItemCategory fromString(String string){
        return switch (string) {
            case "coffee" -> ItemCategory.COFFEE;
            case "noncoffee" -> ItemCategory.NONCOFFEE;
            default -> ItemCategory.BAKERY;
        };
    }

    public String getValue(){ return value;}
}
