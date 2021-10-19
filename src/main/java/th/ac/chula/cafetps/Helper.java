package th.ac.chula.cafetps;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

public class Helper {

    // don't forget to change path
    public static final  String DATABASE_NAME = "jdbc:sqlite:/D:\\sqlite3\\Cafe.sqlite";
    private ArrayList<itemRecord> records;
    private PriceTable priceTable;

    public Helper() {
        this.records = getItemRecord();
        this.priceTable = new PriceTable();
        for(int i = 0;i<records.size();i++) {
            itemRecord temp = records.get(i);
            priceTable.addPrice(temp.getName(), temp.getType(), temp.getPricePerUnit());
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
                member = new Member(result.getString("m_id"),result.getString("m_name").split(" ")[0],Integer.parseInt(result.getString("point")));
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

    public ArrayList<Item> getLastOrder(String phoneNumber){
        ArrayList<Item> toReturn = new ArrayList<>();
        String getRID = "SELECT max(r_id) FROM Receipt WHERE m_id = `phoneNumber`";
        String getLastOrder = "SELECT * FROM Receipt_Detail WHERE  r_id =";
        int Receipt_id;
        Connection connection = connect();
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getRID);
            result.next();
            Receipt_id = result.getInt(1);
            getLastOrder += ""+Receipt_id;
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
                return new Item(records.get(i).getName(),records.get(i).getType(),quantity,sweetness);
            }
        }return null;
    }
    public ArrayList<itemRecord> getItemRecord(){
        Connection connection = connect();
        ArrayList<itemRecord> data = new ArrayList<>();
        String sql = "SELECT * FROM Item";
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                data.add(new itemRecord(Integer.parseInt(result.getString("item_id")),result.getString("item_name"),
                        result.getString("type"),result.getString("category"),Integer.parseInt(result.getString("priceperunit"))));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }

    public void updatePoint(Member member){
        Connection connection = connect();
        String sql = String.format("UPDATE Member SET point = %.2f WHERE m_id = '%s'",member.getPoint(),member.getMemberID());
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

    public ArrayList<itemRecord> getRecords() {
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
}

