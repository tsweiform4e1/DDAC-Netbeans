package Controller;



import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class DB_Connection {

    public static void main(String[] args) {
        DB_Connection dbcon = new DB_Connection();
        System.out.println(dbcon.getConnection());
    }

    public Connection getConnection() {

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ddac-maerskline?useSSL=false", "root", "12345"); // Local Connection
            con =DriverManager.getConnection("jdbc:mysql://ddac-mysql-server.mysql.database.azure.com:3306/ddac-maerskline?useSSL=true&requireSSL=false","mladmin@ddac-mysql-server", "Tswei12345");  //Azure Connection
            System.out.println("Connection to DB is successful !");
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}
