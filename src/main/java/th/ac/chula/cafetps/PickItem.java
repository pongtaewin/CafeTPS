package th.ac.chula.cafetps;

import java.util.ArrayList;
import java.util.List;

public class PickItem {

    private String name;
    private List<itemType> availableType;
    private boolean pickSweetness;

    public PickItem(String name,boolean pickSweetness) {
        this.name = name;
        this.availableType = new ArrayList<>();
        this.pickSweetness = pickSweetness;
    }

    public PickItem(String name, itemType availableType, boolean pickSweetness) {
        this(name, pickSweetness);
        this.availableType.add(availableType);
    }

    public void addAvailableType(itemType type){
        availableType.add(type);
    }

}
