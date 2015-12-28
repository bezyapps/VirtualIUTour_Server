package com.org.actions.database;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Created by ericbhatti on 12/28/15.
 */
public class DataBaseConnection {

    private static final String PROPERTIES_FILE = "properties.txt";
    private static final String JSON_KEY_HOST = "host";
    private static final String JSON_KEY_USERNAME = "username";
    private static final String JSON_KEY_DB_NAME = "database";
    private static final String JSON_KEY_PASS = "password";

    private Connection dbConnection;

    public DataBaseConnection()
    {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE);
        InputStreamReader is = new InputStreamReader(input);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = null;
        try {
            read = br.readLine();
            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = sb.toString();

        String dbUrl = "jdbc:mysql://";
        String username = null;
        String password = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String host = jsonObject.getString(JSON_KEY_HOST);
            String database = jsonObject.getString(JSON_KEY_DB_NAME);
            username = jsonObject.getString(JSON_KEY_USERNAME);
            password = jsonObject.getString(JSON_KEY_PASS);
            dbUrl = dbUrl + host + "/" + database;
            System.out.println(dbUrl);
            System.out.println(username);
            System.out.println(password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection(dbUrl,
                    username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void test()
    {
        String query = "Select * from weekdays";
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String tableName = resultSet.getString(2);
                System.out.println("Day name : " + tableName);
            }
        dbConnection.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
