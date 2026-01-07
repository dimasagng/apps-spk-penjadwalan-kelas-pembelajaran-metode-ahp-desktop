package com.spk.config;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection conn;
    
        public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/db_spk_ahp_penjadwalan_kelas";
                String user = "root";
                String pass = "MilanoIsRed_1899";
                
               DriverManager.registerDriver(new Driver());
                conn = (Connection) DriverManager.getConnection(url, user, pass);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return conn;
    }
}

