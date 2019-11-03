/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnUtils {

    static Connection c;
    public static String hostName = "localhost";

    public static String dbName = "datalaptop1";
    public static String connectionURL;
    public static String userName = "root";
    public static String password = "123456";

    static {
        connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
    }

    // Kết nối vào MySQL.
    public static Connection getMySQLConnection() throws SQLException,
            ClassNotFoundException {

        return c = getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
            String userName, String password) throws SQLException,
            ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);
        return conn;
    }
}
