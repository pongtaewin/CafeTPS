package th.ac.chula.cafetps.model;

import th.ac.chula.cafetps.helper.DatabaseManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MenuItem {
    private ArrayList<Item> recentOrder = new ArrayList<>();
    private final ArrayList<PickItem> coffeeMenu,nonCoffeeMenu,bakeryMenu;

    public MenuItem(DatabaseManager databaseManager){
        coffeeMenu = new ArrayList<>();
        nonCoffeeMenu = new ArrayList<>();
        bakeryMenu = new ArrayList<>();
        PriceTable priceTable = databaseManager.getPriceTable();
        databaseManager.getRecords().forEach(item -> {
            priceTable.addPrice(item.getName(), item.getProperty(), item.getPricePerUnit());
            switch (item.getCategory()) {
                case BAKERY -> applyMenu(bakeryMenu, new PickItem(item.getName(), false));
                case COFFEE -> applyMenu(coffeeMenu, new PickItem(item.getName(), true, item.getProperty()));
                case NONCOFFEE -> applyMenu(nonCoffeeMenu, new PickItem(item.getName(), item.getId() != 33, item.getProperty()));
            }
        });
    }

    private static void applyMenu(ArrayList<PickItem> menu, PickItem mItem){
        menu.stream().filter(item -> item.getName().equals(mItem.getName())).findFirst()
                .ifPresentOrElse(item -> item.addAllAvailableProperty(mItem.getAvailableProperty()),
                        () -> menu.add(mItem));
    }

    public MenuItem(DatabaseManager databaseManager, Member member) {
        this(databaseManager);
        member.getID()
                .flatMap(databaseManager::getRecentOrder)
                .ifPresent(ro -> recentOrder = (ArrayList<Item>) ro);
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
