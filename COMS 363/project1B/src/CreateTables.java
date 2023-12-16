/**
 * @author Dylan Booth
 */

import java.sql.*;

public class CreateTables {

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
            stmt.addBatch(" CREATE TABLE students (\n\r" +
                            "   snum INTEGER, \r\n" +
                            "   ssn INTEGER, \r\n" +
                            "   name VARCHAR(10), \r\n" +
                            "   gender VARCHAR(1), \r\n" +
                            "   dob DATE, \r\n" +
                            "   c_addr VARCHAR(20), \r\n" +
                            "   c_phone VARCHAR(10), \r\n" +
                            "   p_addr VARCHAR(20), \r\n" +
                            "   p_phone VARCHAR(10), \r\n" +
                            "   PRIMARY KEY(ssn), \r\n" +
                            "   UNIQUE(snum)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE departments (\n\r" +
                            "   code INTEGER, \r\n" +
                            "   name VARCHAR(50), \r\n" +
                            "   phone VARCHAR(10), \r\n" +
                            "   college VARCHAR(20), \r\n" +
                            "   PRIMARY KEY(code)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE degrees (\n\r" +
                            "   name VARCHAR(50), \r\n" +
                            "   level VARCHAR(5), \r\n" +
                            "   department_code INTEGER, \r\n" +
                            "   PRIMARY KEY(name, level), \r\n" +
                            "   FOREIGN KEY(department_code) REFERENCES departments(code)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE courses (\n\r" +
                            "   number INTEGER, \r\n" +
                            "   name varchar(50), \r\n" +
                            "   description varchar(50), \r\n" +
                            "   credithours integer, \r\n" +
                            "   level varchar(20), \r\n" +
                            "   department_code integer, \r\n" +
                            "   primary key (number), \r\n" +
                            "   foreign key (department_code) REFERENCES departments(code)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE register(\n\r" +
                            "   snum integer, \r\n" +
                            "   course_number integer, \r\n" +
                            "   regtime varchar(20), \r\n" +
                            "   grade integer, \r\n" +
                            "   primary key (snum, course_number), \r\n" +
                            "   foreign key (snum) REFERENCES students(snum), \r\n" +
                            "   foreign key (course_number) REFERENCES courses(number)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE major(\n\r" +
                            "   snum integer, \r\n" +
                            "   name varchar(50), \r\n" +
                            "   level varchar(5), \r\n" +
                            "   primary key (snum, name, level), \r\n" +
                            "   foreign key (snum) REFERENCES students(snum), \r\n" +
                            "   foreign key (name, level) REFERENCES degrees(name, level)\r\n" + ");"
            );

            stmt.addBatch(" CREATE TABLE minor(\n\r" +
                            "   snum integer, \r\n" +
                            "   name varchar(50), \r\n" +
                            "   level varchar(5), \r\n" +
                            "   primary key (snum, name, level), \r\n" +
                            "   foreign key (snum) REFERENCES students(snum), \r\n" +
                            "   foreign key (name, level) REFERENCES degrees(name, level)\r\n" + ");"
            );

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
