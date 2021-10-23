package th.ac.chula.cafetps;

import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SummaryHelper {
    //Summary Helper
    public static ArrayList<String> getDistinctYearMonth(){
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

    public static HashMap<String,Integer> getSellUnit(String yearAndMonth){
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

    public static ArrayList<Integer> getIncomeAndCost(String yearAndMonth){
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

    public static int getEmployeeTotalSalary(){
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

    public static int getRentalCost(){
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

    public static HashMap<String,ArrayList<XYChart.Data<String,Integer>>> getChartData(String yearAndMonth){
        HashMap<String,ArrayList<XYChart.Data<String,Integer>>> data = new HashMap<>();
        data.put("bakery",new ArrayList<>());
        data.put("coffee",new ArrayList<>());
        data.put("noncoffee",new ArrayList<>());

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
                if(data.containsKey(resultSet.getString("category"))){
                    data.get(resultSet.getString("category")).add(new XYChart.Data<>(
                            resultSet.getInt("day") + "", resultSet.getInt("total")));
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return data;
    }
}
