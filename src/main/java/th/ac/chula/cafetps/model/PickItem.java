package th.ac.chula.cafetps.model;

import th.ac.chula.cafetps.constants.ItemProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PickItem {

    private String name;
    private List<ItemProperty> availableProperty;
    private boolean pickSweetness;

    public PickItem(String name,boolean pickSweetness) {
        this.name = name;
        this.availableProperty = new ArrayList<>();
        this.pickSweetness = pickSweetness;
    }

    public PickItem(String name, boolean pickSweetness, ItemProperty availableProperty) {
        this(name, pickSweetness);
        this.availableProperty.add(availableProperty);
    }

    public PickItem(String name, boolean pickSweetness, Collection<ItemProperty> availableProperties) {
        this(name, pickSweetness);
        this.availableProperty.addAll(availableProperties);
    }

    public void addAvailableProperty(ItemProperty type){
        availableProperty.add(type);
    }

    public void addAllAvailableProperty(Collection<ItemProperty> types){
        availableProperty.addAll(types);
    }

    public List<ItemProperty> getAvailableProperty() {
        return availableProperty;
    }

    public boolean isPickSweetness() {
        return pickSweetness;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PickItem{" +
                "name='" + name + '\'' +
                ", availableProperty=" + availableProperty +
                ", pickSweetness=" + pickSweetness +
                '}';
    }
}
