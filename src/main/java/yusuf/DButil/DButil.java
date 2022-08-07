package yusuf.DButil;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DButil {

    public static Connection getConnection() {
        Properties properties = new Properties();
        Connection connection = null;

        try {
            File file = new File(".");
            FileInputStream fis = new FileInputStream(file + "\\src\\resources\\db.properties");
            properties.load(fis);
            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver is not available: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection could not be established:  " + e.getMessage());
        }
        return connection;
    }
    public static void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("connection could not be closed: "+e.getMessage());
                
            }
        }else{
            System.out.println("Connection is not available");
        }
    }

}
