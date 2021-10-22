package th.ac.chula.cafetps;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import th.ac.chula.cafetps.model.Item;
import th.ac.chula.cafetps.model.ItemRecord;
import th.ac.chula.cafetps.model.Member;
import th.ac.chula.cafetps.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.HashMap;

public class Helper {

    // don't forget to change path
    private final ArrayList<ItemRecord> records;
    private final PriceTable priceTable;

    public Helper() {
        this.records = DatabaseHelper.getItemRecord();
        this.priceTable = new PriceTable();
        for (ItemRecord record : records) {
            priceTable.addPrice(record.getName(), record.getProperty(), record.getPricePerUnit());
        }
    }

    public ArrayList<Item> getRecentOrder(String phoneNumber){
        ArrayList<Item> toReturn = new ArrayList<>();
        ResultSet result = DatabaseHelper.recentOrderQuery(phoneNumber);
        try{
            Objects.requireNonNull(result);
            while (result.next()){
                toReturn.add(getItemFromID(result.getInt("item_id"),result.getInt("amount"),result.getString("sweetness")));
            }
        }catch (NullPointerException | SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    private Item getItemFromID(int id,int quantity,String sweetness){
        return records.stream()
                .filter(r -> r.getId() == id).findFirst()
                .map(r-> new Item(r.getName(),r.getProperty(),quantity,sweetness))
                .orElse(null);
    }

    public static void updatePoint(Member member){
        if(member.getID().equals("0")) return;
        Connection connection = DatabaseHelper.connect();
        String sql = String.format("UPDATE Member SET point = %.2f WHERE m_id = '%s'",member.getPoints(),member.getID());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public PriceTable getPriceTable() {
        return priceTable;
    }

    public ArrayList<ItemRecord> getRecords() {
        return records;
    }

    public static void insertReceipt(User employee,Member member,int total){
        Connection connection = DatabaseHelper.connect();
        String commandReceipt = "INSERT INTO Receipt VALUES(null,?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(commandReceipt);
            statement.setString(1,member.getID());
            statement.setString(2,employee.getUsername());
            statement.setInt(3,total);
            statement.setString(4,DatabaseHelper.getNow());
            statement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertReceiptDetail(ObservableList<Item> receipt){
            Connection connection = DatabaseHelper.connect();
            String command = "INSERT INTO Receipt_Detail VALUES(?,?,?,?)";
            String getRID = "SELECT MAX(r_id) FROM Receipt";
            int r_id;
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getRID);
                resultSet.next();
                r_id = resultSet.getInt(1);
                // TODO: remove debug println
                // System.out.println(r_id);
                PreparedStatement preparedStatement = connection.prepareStatement(command);

                for (Item item : receipt) {
                    ItemRecord record = getRecord(item).orElseThrow(IllegalArgumentException::new);
                    preparedStatement.setInt(1, r_id);
                    preparedStatement.setInt(2, record.getId());
                    preparedStatement.setInt(3, item.getQuantity());
                    preparedStatement.setString(4, item.getSweetness());
                    preparedStatement.executeUpdate();
                }
                connection.close();

        }catch (SQLException | IllegalArgumentException e){
                e.printStackTrace();
            }
    }

    public Optional<ItemRecord> getRecord(Item item) {
        return records.stream()
                .filter(r -> r.getProperty().equals(item.getProperty()))
                .filter(r -> r.getName().equals(item.getName()))
                .findFirst();
    }

    //Summary Helper
    public ArrayList<String> getDistinctYearMonth(){
        Connection connection = DatabaseHelper.connect();
        ArrayList<String> item = new ArrayList<>();
        String sql = "select distinct(strftime('%Y-%m',create_date)) from Receipt";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                item.add(resultSet.getString(1));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }return item;
    }

    public HashMap<String,Integer> getSellUnit(String yearAndMonth){
        Connection connection = DatabaseHelper.connect();
        HashMap<String,Integer> item = new HashMap<>();
        String sql = """
                select temp.category,sum(temp.amount) as sale_unit from (select r_id,Item.category,amount from Receipt_Detail\s
                inner join  Item
                on Receipt_Detail.item_id = Item.item_id
                order by r_id) as temp
                where temp.r_id in (select r_id from Receipt where strftime('%Y-%m',create_date)  = ?)
                group by category""";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,yearAndMonth);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                item.put(resultSet.getString("category"),resultSet.getInt("sale_unit"));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    public ArrayList<Integer> getIncomeAndCost(String yearAndMonth){
        Connection connection = DatabaseHelper.connect();
        ArrayList<Integer> item = new ArrayList<>();
        String sql = """
                select sum(income) as total_income,sum(cost) as total_cost,sum(income)-sum(cost) as sell_profit from (select r_id,Item.item_id,priceperunit*amount as income,costperunit*amount as cost from Item
                inner join (select r_id,item_id as id,amount from Receipt_Detail
                where r_id in (select r_id from Receipt where strftime('%Y-%m',create_date)  = ?))
                on Item.item_id = id)""";
        try {
            PreparedStatement preparedStatement = DatabaseHelper.connect().prepareStatement(sql);
            preparedStatement.setString(1,yearAndMonth);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            item.add(resultSet.getInt("total_income"));
            item.add(resultSet.getInt("total_cost"));
            item.add(resultSet.getInt("sell_profit"));
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    public int getEmployeeTotalSalary(){
        Connection connection = DatabaseHelper.connect();
        int total = 0;
        String sql = "select sum(salary) from Employee";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            total = resultSet.getInt(1);
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }return total;
    }

    public int getRentalCost(){
        Connection connection = DatabaseHelper.connect();
        int rental = 0;
        String sql = "select * from Cost where subject = 'rental'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            rental = resultSet.getInt("price");
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }return rental;
    }

    public HashMap<String,ArrayList<XYChart.Data<String,Integer>>> getChartData(String yearAndMonth){
        HashMap<String,ArrayList<XYChart.Data<String,Integer>>> data = new HashMap<>();
        ArrayList<XYChart.Data<String,Integer>> bakeryline = new ArrayList<>();
        ArrayList<XYChart.Data<String,Integer>> coffeeline = new ArrayList<>();
        ArrayList<XYChart.Data<String,Integer>> noncoffeeline = new ArrayList<>();
        data.put("bakery",bakeryline);
        data.put("coffee",coffeeline);
        data.put("noncoffee",noncoffeeline);

        Connection connection = DatabaseHelper.connect();
        String sql = """
                select strftime('%d',create_date) as day,sum(amount) as total,category from Receipt_Detail
                inner join Item
                on Receipt_Detail.item_id = Item.item_id
                inner join (select * from Receipt where strftime('%Y-%m',create_date)  = ?) as temp
                on Receipt_Detail.r_id = temp.r_id
                group by category,day;""";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,yearAndMonth);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getString("category").equals("bakery")){
                    bakeryline.add(new XYChart.Data<>(resultSet.getInt("day")+"",resultSet.getInt("total")));
                }else if (resultSet.getString("category").equals("coffee")){
                    coffeeline.add(new XYChart.Data<>(resultSet.getInt("day")+"",resultSet.getInt("total")));
                }else if (resultSet.getString("category").equals("noncoffee")){
                    noncoffeeline.add(new XYChart.Data<>(resultSet.getInt("day")+"",resultSet.getInt("total")));
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }
}

