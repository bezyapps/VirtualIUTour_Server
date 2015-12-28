package com.org.actions.database;

import java.sql.*;

/**
 * Created by ericbhatti on 12/28/15.
 */
public class DataBaseConnection {


    public void test()
    {
        String dbUrl = "jdbc:mysql://localhost/virtual_tour";
        String dbClass = "com.mysql.jdbc.Driver";
        String query = "Select * from weekdays";
        String username = "root";
        String password = "root";
        try {

            Class.forName(dbClass);
            Connection connection = DriverManager.getConnection(dbUrl,
                    username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String tableName = resultSet.getString(2);
                System.out.println("Day name : " + tableName);
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
