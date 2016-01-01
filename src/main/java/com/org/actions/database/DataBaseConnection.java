package com.org.actions.database;


import com.org.actions.models.Schedule;
import org.json.JSONException;
import org.json.JSONObject;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by ericbhatti on 12/28/15.
 */
public class DataBaseConnection {

    private final String PROPERTIES_FILE = "properties.txt";
    private final String JSON_KEY_HOST = "host";
    private final String JSON_KEY_USERNAME = "username";
    private final String JSON_KEY_DB_NAME = "database";
    private final String JSON_KEY_PASS = "password";
    private final String DATABASE_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private Connection dbConnection;

    public DataBaseConnection()
    {
        try {
             Class.forName(DATABASE_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public String getTodaysSchedule(String marker)
    {
        String jsonResponse = null;
        Calendar calendar = Calendar.getInstance();
        String today = calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.ENGLISH);
        StringBuilder builder = new StringBuilder("SELECT "+ DataBaseFields.Weekdays.WEEKDAY_NAME + ",");
        builder.append(DataBaseFields.TimeSlots.START_TIME + ",");
        builder.append(DataBaseFields.TimeSlots.END_TIME + ",");
        //builder.append(DataBaseFields.Markers.MARKER_CLASS + ",");
        builder.append(DataBaseFields.Markers.MARKER_DISPLAY_NAME + ",");
        builder.append(DataBaseFields.Schedules.SCHEDULED_CLASS + " FROM ");
        builder.append(DataBaseFields.Markers.TABLE_NAME + ",");
        builder.append(DataBaseFields.Schedules.TABLE_NAME + ",");
        builder.append(DataBaseFields.TimeSlots.TABLE_NAME + ",");
        builder.append(DataBaseFields.Weekdays.TABLE_NAME + " WHERE ");
        builder.append(DataBaseFields.Weekdays.TABLE_NAME + "." + DataBaseFields.Weekdays._ID + " = " + DataBaseFields.Schedules.TABLE_NAME + "." + DataBaseFields.Schedules.WEEKDAY_ID + " AND ");
        builder.append(DataBaseFields.TimeSlots.TABLE_NAME + "." + DataBaseFields.TimeSlots._ID + " = " + DataBaseFields.Schedules.TABLE_NAME + "." + DataBaseFields.Schedules.TIMESLOT_ID + " AND ");
        builder.append(DataBaseFields.Markers.TABLE_NAME + "." + DataBaseFields.Markers._ID + " = " + DataBaseFields.Schedules.TABLE_NAME + "." + DataBaseFields.Schedules.MARKER_ID + " AND ");
        builder.append(DataBaseFields.Markers.MARKER_CLASS + " = '" + marker + "' AND ");
        today = "Monday";
        builder.append(DataBaseFields.Weekdays.WEEKDAY_NAME + " = '" + today + "'");
        String query = builder.toString();
        System.out.println(query);
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Map<String,Object> resultMap = new LinkedHashMap<>();
//            String displayMarkerName = null;
            List<Schedule> scheduleList = new ArrayList<>();
            while (resultSet.next()) {
//                displayMarkerName = resultSet.getString(resultSet.findColumn(DataBaseFields.Markers.MARKER_DISPLAY_NAME));
                Schedule schedule = new Schedule();
                schedule.setClassName(resultSet.getString(resultSet.findColumn(DataBaseFields.Schedules.SCHEDULED_CLASS)));
                String startTime = resultSet.getString(resultSet.findColumn(DataBaseFields.TimeSlots.START_TIME));
                String endTime = resultSet.getString(resultSet.findColumn(DataBaseFields.TimeSlots.END_TIME));
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                try {
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
                    Date startTimeInDate = parser.parse(startTime);
                    Date endTimeInDate = parser.parse(endTime);
                    String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    Date currentTimeInDate = parser.parse(currentTime);
                    if ((currentTimeInDate.after(startTimeInDate) && currentTimeInDate.before(endTimeInDate)) || currentTimeInDate.equals(startTimeInDate) || currentTimeInDate.equals(endTimeInDate)) {
                        schedule.setIsCurrent(true);
                    }
                    else{
                        schedule.setIsCurrent(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    schedule.setIsCurrent(false);
                }
                scheduleList.add(schedule);
            }
            System.out.println(marker);
            ResultSet resultSet2 = statement.executeQuery("SELECT " + DataBaseFields.Markers.MARKER_DISPLAY_NAME + " FROM " + DataBaseFields.Markers.TABLE_NAME + " WHERE " + DataBaseFields.Markers.MARKER_CLASS + " = '" + marker + "'");
            String display = "";
            while (resultSet2.next()) {
               display = resultSet2.getString(1);
            }

            resultMap.put("displayMarkerName",display);
            resultMap.put("day",today);
            resultMap.put("schedules",scheduleList);
            JSONObject object = new JSONObject(resultMap);
            jsonResponse = object.toString();
            dbConnection.close();
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonResponse;
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
