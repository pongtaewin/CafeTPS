package th.ac.chula.cafetps;

public class itemRecord {
    private int id;
    private String name;
    private itemProperty property;
    private itemCategory category;
    private int pricePerUnit;
    private int costPerUnit;

    public itemRecord(int id, String name, String property, String category, int pricePerUnit,int costPerUnit) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.costPerUnit = costPerUnit;

        if (property.equals("hot")){
            this.property = itemProperty.HOT;
        }else if(property.equals("iced")){
            this.property = itemProperty.ICED;
        }else if(property.equals("frappe")){
            this.property = itemProperty.FRAPPE;
        } else this.property = itemProperty.NONE;

        if(category.equals("coffee")){
           this.category = itemCategory.COFFEE;
        }else if(category.equals("noncoffee")){
            this.category = itemCategory.NONCOFFEE;
        }else this.category = itemCategory.BAKERY;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public itemProperty getProperty() {
        return property;
    }

    public itemCategory getCategory() {
        return category;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public int getCostPerUnit() {
        return costPerUnit;
    }

    @Override
    public String toString() {
        return "itemRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", property=" + property +
                ", category=" + category +
                ", pricePerUnit=" + pricePerUnit +
                ", costPerUnit=" + costPerUnit +
                '}';
    }
}
