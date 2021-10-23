package th.ac.chula.cafetps.helper;

import javafx.collections.ObservableList;
import th.ac.chula.cafetps.model.PriceTable;
import th.ac.chula.cafetps.helper.DatabaseHelper;
import th.ac.chula.cafetps.model.Item;
import th.ac.chula.cafetps.model.ItemRecord;
import th.ac.chula.cafetps.model.Member;
import th.ac.chula.cafetps.model.User;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseManager {

    // don't forget to change path
    private final ArrayList<ItemRecord> records;
    private final PriceTable priceTable;

    public DatabaseManager() {
        this.records = DatabaseHelper.getItemRecord();
        this.priceTable = new PriceTable();
        for (ItemRecord record : records) {
            priceTable.addPrice(record.getName(), record.getProperty(), record.getPricePerUnit());
        }
    }

    public Optional<List<Item>> getRecentOrder(String phoneNumber){
        ResultSet result = DatabaseHelper.recentOrderQuery(phoneNumber);
        try{
            if(Objects.isNull(result)) return Optional.empty();
            Stream.Builder<Item> bd = Stream.builder();
            while (result.next())
                bd.add(getItemFromID(
                        result.getInt("item_id"),
                        result.getInt("amount"),
                        result.getString("sweetness")));
            return Optional.of(bd.build().collect(Collectors.toList()));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
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
            statement.setString(1,member.getID().orElse("0"));
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

}

