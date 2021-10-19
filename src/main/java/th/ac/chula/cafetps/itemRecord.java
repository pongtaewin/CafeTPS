package th.ac.chula.cafetps;

public class itemRecord {
    private int id;
    private String name;
    private itemType type;
    private itemCategory category;
    private int pricePerUnit;

    public itemRecord(int id, String name, String type, String category, int pricePerUnit) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;

        if (type.equals("hot")){
            this.type = itemType.HOT;
        }else if(type.equals("iced")){
            this.type = itemType.ICED;
        }else if(type.equals("frappe")){
            this.type = itemType.FRAPPE;
        } else this.type = itemType.NONE;

        if(category.equals("coffee")){
           this.category = itemCategory.COFFEE;
        }else if(category.equals("non-coffee")){
            this.category = itemCategory.NONCOFFEE;
        }else this.category = itemCategory.BAKERY;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public itemType getType() {
        return type;
    }

    public itemCategory getCategory() {
        return category;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }
}
