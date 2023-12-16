/**
 * @author Dylan Booth
 */

import java.sql.*;

public class ModifyRecords {

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

            // The first update of changing students name based on ssn
            stmt.addBatch("UPDATE students set name = 'Scott' WHERE ssn = 746897816;");

            // Changing major to CS, level Masters based by ssn
            stmt.addBatch(" UPDATE major set name = 'Computer Science', level = 'MS' WHERE snum = (\n" +
                        "       SELECT snum FROM students WHERE ssn = 746897816);\n");

            // Deleting all registration records that were in Spring of 2021
            stmt.addBatch(" DELETE FROM register WHERE regtime = 'Spring2021';");

            // Removing all duplicate records
            stmt.addBatch(" CREATE TEMPORARY TABLE temp AS" +
                              "     SELECT level, department_code, MIN(number) as number" +
                              "     FROM courses GROUP BY level, department_code;");
            stmt.addBatch(" DELETE register FROM register" +
                              "     LEFT JOIN courses ON register.course_number = courses.number" +
                              "     WHERE (courses.level, courses.department_code, courses.number) NOT IN (" +
                              "         SELECT level, department_code, number FROM temp);");
            stmt.addBatch(" DELETE FROM courses WHERE (level, department_code, number) NOT IN (" +
                              "     SELECT level, department_code, number FROM temp);");
            stmt.addBatch(" drop table temp;");

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
