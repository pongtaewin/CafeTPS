package th.ac.chula.cafetps;

import th.ac.chula.cafetps.model.ItemRecord;
import th.ac.chula.cafetps.model.Member;
import th.ac.chula.cafetps.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Objects;

public class DatabaseHelper {
    static Connection connect(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(Constants.DATABASE_NAME);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean login(User user) throws NoSuchAlgorithmException {
        boolean flag = false;
        String password = sha256Encode(user.getPassword());
        String username = MessageFormat.format("''{0}''", user.getUsername());
        Connection connection = DatabaseHelper.connect();
        String sql = MessageFormat.format("SELECT * FROM Employee WHERE e_uname = {0}", username);
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

    public static Member memberCheck(String phoneNumber){
        Connection connection = DatabaseHelper.connect();
        String sql = MessageFormat.format("SELECT * FROM Member WHERE m_id = ''{0}''", phoneNumber);
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
        return formatter.format(time);
    }

    public static String getToday(){
        java.util.Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(today);
    }

    public static String getDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateTemp = formatter.parse(date);
        return formatter.format(dateTemp);
    }

    private static String sha256Encode(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static void addMember(String m_id, String m_name, String gender, String dob){
        String join_date = getToday();
        Connection connection = DatabaseHelper.connect();
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

    static ResultSet recentOrderQuery(String phoneNumber){
        ResultSet result = null;
        String getRecent = "SELECT max(r_id) FROM Receipt WHERE m_id = '%s'".formatted(phoneNumber);
        String getLastOrder = "SELECT * FROM Receipt_Detail WHERE  r_id = '%s'";
        Connection connection = DatabaseHelper.connect();
        try{
            Statement statement = connection.createStatement();
            result = statement.executeQuery(getRecent);
            result.next();
            getLastOrder = getLastOrder.formatted(result.getInt(1)); // receipt_id
            result = statement.executeQuery(getLastOrder);
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ItemRecord> getItemRecord(){
        Connection connection = DatabaseHelper.connect();
        ArrayList<ItemRecord> data = new ArrayList<>();
        String sql = "SELECT * FROM Item";
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                data.add(new ItemRecord(Integer.parseInt(
                        result.getString("item_id")),
                        result.getString("item_name"),
                        Objects.requireNonNullElse(result.getString("property"),""),
                        result.getString("category"),
                        result.getInt("priceperunit"),
                        result.getInt("costperunit")));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
