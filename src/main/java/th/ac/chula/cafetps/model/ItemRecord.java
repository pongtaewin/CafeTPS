package th.ac.chula.cafetps.model;

import th.ac.chula.cafetps.ItemCategory;
import th.ac.chula.cafetps.ItemProperty;

public class ItemRecord {
    private int id;
    private String name;
    private ItemProperty property;
    private ItemCategory category;
    private int pricePerUnit;
    private int costPerUnit;

    public ItemRecord(int id, String name, String property, String category, int pricePerUnit, int costPerUnit) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.costPerUnit = costPerUnit;
        this.property = ItemProperty.fromString(property);
        this.category = ItemCategory.fromString(category);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemProperty getProperty() {
        return property;
    }

    public ItemCategory getCategory() {
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
