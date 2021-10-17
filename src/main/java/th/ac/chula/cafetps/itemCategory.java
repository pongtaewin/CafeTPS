package th.ac.chula.cafetps;

public enum itemCategory {
    COFFEE("Coffee"),
    NONCOFFEE("Non-Coffee"),
    BAKERY("Bakery");

    private  String value;

    itemCategory(final String value){
        this.value = value;
    }

    public String getValue(){ return value;}
}
