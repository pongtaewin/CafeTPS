package th.ac.chula.cafetps;

import java.util.ArrayList;

public class MenuItem {
    private ArrayList<Item> recentOrder = new ArrayList<>();
    private ArrayList<PickItem> coffeeMenu,nonCoffeeMenu,bakeryMenu;

    public MenuItem(Helper helper){
        coffeeMenu = new ArrayList<>();
        nonCoffeeMenu = new ArrayList<>();
        bakeryMenu = new ArrayList<>();
        ArrayList<itemRecord> records = helper.getRecords();
        PriceTable priceTable = helper.getPriceTable();
        ArrayList<String> tempArrayCoffee = new ArrayList<>();
        ArrayList<String> tempArrayNon = new ArrayList<>();
        for(int i = 0;i<records.size();i++) {
            itemRecord temp = records.get(i);
            priceTable.addPrice(temp.getName(), temp.getProperty(), temp.getPricePerUnit());
            if(temp.getCategory()==itemCategory.COFFEE){
                if(!tempArrayCoffee.contains(temp.getName())){
                    coffeeMenu.add(new PickItem(temp.getName(),true));
                    tempArrayCoffee.add(temp.getName());

                }
                coffeeMenu.get(tempArrayCoffee.indexOf(temp.getName())).addAvailableProperty(temp.getProperty());
            }else if(temp.getCategory()==itemCategory.NONCOFFEE){
                if(!tempArrayNon.contains(temp.getName())) {
                    nonCoffeeMenu.add(new PickItem(temp.getName(), true));
                    tempArrayNon.add(temp.getName());
                }
                nonCoffeeMenu.get(tempArrayNon.indexOf(temp.getName())).addAvailableProperty(temp.getProperty());
            }else if(temp.getCategory()==itemCategory.BAKERY){
                bakeryMenu.add(new PickItem(temp.getName(), false));
            }
        }
    }

    public MenuItem(Helper helper,Member member) {
        this(helper);
        if(!member.getMemberID().equals("0"))
        recentOrder = helper.getRecentOrder(member.getMemberID());
    }

    public ArrayList<Item> getRecentOrder() {
        return recentOrder;
    }

    public ArrayList<PickItem> getCoffeeMenu() {
        return coffeeMenu;
    }

    public ArrayList<PickItem> getNonCoffeeMenu() {
        return nonCoffeeMenu;
    }

    public ArrayList<PickItem> getBakeryMenu() {
        return bakeryMenu;
    }
}
