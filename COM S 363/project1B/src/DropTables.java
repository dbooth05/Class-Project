/**
 * @author Dylan Booth
 */

import java.sql.*;


public class DropTables {
    private static Connection con = null;

    public static void main(String[] args) {

        try {
            //setup connection params
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1";
            con = DriverManager.getConnection(dbServer, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.addBatch("drop table if exists register;");
            stmt.addBatch("drop table if exists major;");
            stmt.addBatch("drop table if exists minor;");
            stmt.addBatch("drop table if exists degrees;");
            stmt.addBatch("drop table if exists courses;");
            stmt.addBatch("drop table if exists students;");
            stmt.addBatch("drop table if exists departments");

            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
