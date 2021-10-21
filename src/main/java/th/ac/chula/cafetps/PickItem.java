package th.ac.chula.cafetps;

import java.util.ArrayList;
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

    public PickItem(String name, ItemProperty availableProperty, boolean pickSweetness) {
        this(name, pickSweetness);
        this.availableProperty.add(availableProperty);
    }

    public void addAvailableProperty(ItemProperty type){
        availableProperty.add(type);
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
