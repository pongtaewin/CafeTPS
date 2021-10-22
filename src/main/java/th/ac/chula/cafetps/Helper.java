package th.ac.chula.cafetps;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import th.ac.chula.cafetps.model.Item;
import th.ac.chula.cafetps.model.ItemRecord;
import th.ac.chula.cafetps.model.Member;
import th.ac.chula.cafetps.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

public class Helper {

    // don't forget to change path
    public static final  String DATABASE_NAME = "jdbc:sqlite:/D:\\sqlite3\\Cafe.sqlite";
    private ArrayList<ItemRecord> records;
    private PriceTable priceTable;

    public Helper() {
        this.records = getItemRecord();
        this.priceTable = new PriceTable();
        for(int i = 0;i<records.size();i++) {
            ItemRecord temp = records.get(i);
            priceTable.addPrice(temp.getName(), temp.getProperty(), temp.getPricePerUnit());
        }
    }

    private Connection connect(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(DATABASE_NAME);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public boolean login(User user) throws NoSuchAlgorithmException{
        boolean flag = false;
        String password = sha256Encode(user.getPassword());
        String username = "'"+user.getUsername()+"'";
        Connection connection = connect();
        String sql = "SELECT * FROM Employee WHERE e_uname = "+username;
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                if (result.getString("e_pass").equals(password)) flag = true;
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flag;
    }

    public Member memberCheck(String phoneNumber){
        Connection connection = connect();
        String sql = "SELECT * FROM Member WHERE m_id = '"+phoneNumber+"'";
        Member member = null;
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(!result.isClosed()) {
                member = new Member(result.getString("m_id"),result.getString("m_name").split(" ")[0],result.getInt("point"));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return member;
    }

    public static String getNow(){
        java.util.Date time = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String now = formatter.format(time);
        return now;
    }

    public static String getToday(){
        java.util.Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateToday = formatter.format(today);
        return dateToday;
    }

    public static String getDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateTemp = formatter.parse(date);
        String toReturn = formatter.format(dateTemp);
        return toReturn;
    }

    private static String sha256Encode(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);
        return encoded;
    }

    public void addMember(String m_id,String m_name,String gender,String dob){
        String join_date = getToday();
        Connection connection = connect();
        String sql = "INSERT INTO Member VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,m_id);
            statement.setString(2,m_name);
            statement.setString(3,join_date);
            statement.setString(4,gender);
            statement.setInt(5,0);
            statement.setString(6,getDate(dob));
            statement.executeUpdate();
            connection.close();
        }catch (SQLException | ParseException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Item> getRecentOrder(String phoneNumber){
        ArrayList<Item> toReturn = new ArrayList<>();
        String getRecent = "SELECT max(r_id) FROM Receipt WHERE m_id = '%s'".formatted(phoneNumber);
        String getLastOrder = "SELECT * FROM Receipt_Detail WHERE  r_id = '%s'";
        int Receipt_id;
        Connection connection = connect();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getRecent);
            result.next();
            Receipt_id = result.getInt(1);
            getLastOrder = getLastOrder.formatted(Receipt_id);
            result = statement.executeQuery(getLastOrder);
            while (result.next()){
                toReturn.add(getItemFromID(result.getInt("item_id"),result.getInt("amount"),result.getString("sweetness")));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }
    private Item getItemFromID(int id,int quantity,String sweetness){
        for (int i = 0;i<records.size();i++){
            if(records.get(i).getId()==id){
                return new Item(records.get(i).getName(),records.get(i).getProperty(),quantity,sweetness);
            }
        }return null;
    }
    public ArrayList<ItemRecord> getItemRecord(){
        Connection connection = connect();
        ArrayList<ItemRecord> data = new ArrayList<>();
        String sql = "SELECT * FROM Item";
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                data.add(new ItemRecord(Integer.parseInt(result.getString("item_id")),result.getString("item_name"),
                        result.getString("property")==null ? "":result.getString("property"),result.getString("category"),result.getInt("priceperunit"),result.getInt("costperunit")));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }

    public void updatePoint(Member member){
        if(member.getID().equals("0")) return;
        Connection connection = connect();
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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void insertReceipt(User employee,Member member,int total){
        Connection connection = connect();
        String commandReceipt = "INSERT INTO Receipt VALUES(null,?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(commandReceipt);
            statement.setString(1,member.getID());
            statement.setString(2,employee.getUsername());
            statement.setInt(3,total);
            statement.setString(4,getNow());
            statement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertReceiptDetail(ObservableList<Item> receipt){
            Connection connection = connect();
            String command = "INSERT INTO Receipt_Detail VALUES(?,?,?,?)";
            String getRID = "SELECT MAX(r_id) FROM Receipt";
            int r_id;
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getRID);
                resultSet.next();
                r_id = resultSet.getInt(1);
                System.out.println(r_id);
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                for (Item item: receipt) {
                    int itemID = getItemID(item);
                    if(itemID==0) throw new IllegalArgumentException();
                    preparedStatement.setInt(1, r_id);
                    preparedStatement.setInt(2, itemID);
                    preparedStatement.setInt(3, item.getQuantity());
                    preparedStatement.setString(4, item.getSweetness());
                    preparedStatement.executeUpdate();
                }
                connection.close();

        }catch (SQLException | IllegalArgumentException e){
                e.printStackTrace();
            }
    }

    public int getItemID(Item item) {
        for(ItemRecord record: records){
            if(record.getProperty()==item.getProperty() && record.getName().equals(item.getName())){
                return record.getId();
            }
        }return 0;
    }

    //Summary Helper
    public ArrayList<String> getDistinctYearMonth(){
        Connection connection = connect();
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
        Connection connection = connect();
        HashMap<String,Integer> item = new HashMap<>();
        String sql = "select temp.category,sum(temp.amount) as sale_unit from (select r_id,Item.category,amount from Receipt_Detail \n" +
                "inner join  Item\n" +
                "on Receipt_Detail.item_id = Item.item_id\n" +
                "order by r_id) as temp\n" +
                "where temp.r_id in (select r_id from Receipt where strftime('%Y-%m',create_date)  = ?)\n" +
                "group by category";
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
        Connection connection = connect();
        ArrayList<Integer> item = new ArrayList<>();
        String sql = "select sum(income) as total_income,sum(cost) as total_cost,sum(income)-sum(cost) as sell_profit from (select r_id,Item.item_id,priceperunit*amount as income,costperunit*amount as cost from Item\n" +
                "inner join (select r_id,item_id as id,amount from Receipt_Detail\n" +
                "where r_id in (select r_id from Receipt where strftime('%Y-%m',create_date)  = ?))\n" +
                "on Item.item_id = id)";
        try {
            PreparedStatement preparedStatement = connect().prepareStatement(sql);
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
        Connection connection = connect();
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
        Connection connection = connect();
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

        Connection connection = connect();
        String sql = "select strftime('%d',create_date) as day,sum(amount) as total,category from Receipt_Detail\n" +
                "inner join Item\n" +
                "on Receipt_Detail.item_id = Item.item_id\n" +
                "inner join (select * from Receipt where strftime('%Y-%m',create_date)  = ?) as temp\n" +
                "on Receipt_Detail.r_id = temp.r_id\n" +
                "group by category,day;";
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

