/**
 * @author Dylan Booth
 */

import java.sql.*;

public class Query {

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

        PreparedStatement statement = null;
        try {

            /* Query 1 */
            String sql =    "SELECT c.number, c.name, AVG(register.grade) AS average_grade \r\n" +
                            "  FROM courses c JOIN register ON c.number = register.course_number \r\n" +
                            "  JOIN students s ON register.snum = s.snum \r\n" +
                            "  WHERE register.regtime != 'Fall2023' \r\n" +
                            "  GROUP BY c.number, c.name;";

            statement = con.prepareStatement(sql);                      //prepping the statement
            ResultSet res = statement.executeQuery();                  //storing the results from query

            System.out.println("Query 1: numbers and names of courses and corresponding average grades from students in past:");
            while (res.next()) {
                System.out.println("\tCourse Number: " + res.getInt("number"));
                System.out.println("\tCourse Name: " + res.getString(("name")));
                System.out.println("\tAverage Grade: " + res.getDouble("average_grade"));
            }
            System.out.println();

            /* Query 2 */

            sql =   "SELECT COUNT(DISTINCT students.ssn) AS count FROM students \r\n" +
                    "JOIN major ON students.snum = major.snum \r\n" +
                    "JOIN degrees ON major.name = degrees.name AND major.level = degrees.level \r\n" +
                    "JOIN departments ON departments.code = degrees.department_code \r\n" +
                    "WHERE students.gender = 'F' AND departments.college = 'LAS' \r\n" +
                    "UNION \r\n" +
                    "SELECT COUNT(DISTINCT students.ssn) AS count FROM students \r\n" +
                    "JOIN minor ON students.snum = minor.snum \r\n" +
                    "JOIN degrees ON minor.name = degrees.name AND minor.level = degrees.level \r\n" + // Added a space here
                    "JOIN departments ON departments.code = degrees.department_code \r\n" +
                    "WHERE students.gender = 'F' AND departments.college = 'LAS';";

            statement = con.prepareStatement(sql);
            res = statement.executeQuery();
            res.next();
            System.out.println("Query 2: Count of female students is LAS: " + res.getInt("count"));
            System.out.println();

            /* Query 3 */
            sql =   "SELECT degrees.name FROM degrees WHERE (\n" +
                    "   SELECT COUNT(*) FROM students s\n" +
                    "   JOIN major m ON s.snum = m.snum\n" +
                    "   WHERE m.name = degrees.name AND s.gender = 'M') + (\n" +
                    "   SELECT COUNT(*) FROM students s\n" +
                    "   JOIN minor mi ON s.snum = mi.snum\n" +
                    "   WHERE mi.name = degrees.name AND s.gender = 'M') > (\n" +
                    "   SELECT COUNT(*) FROM students s\n" +
                    "   JOIN major m ON s.snum = m.snum\n" +
                    "   WHERE m.name = degrees.name AND s.gender = 'F') + (\n" +
                    "   SELECT COUNT(*) FROM students s\n" +
                    "   JOIN minor mi ON s.snum = mi.snum\n" +
                    "   WHERE mi.name = degrees.name AND s.gender = 'S'); ";

            statement = con.prepareStatement(sql);
            res = statement.executeQuery();

            System.out.println("Query 3: Names of degrees that have more male than female students:");
            while (res.next()) {
                System.out.println("\t" + res.getString("name"));
            }
            System.out.println();

            /* Query 4 */
            sql = " SELECT d.name FROM students s\n" +
                  "     JOIN major m ON s.snum = m.snum\n" +
                  "     JOIN degrees d ON m.name = d.name AND m.level = d.level\n" +
                  "     WHERE s.dob = (SELECT MAX(dob) FROM students);";

            statement = con.prepareStatement(sql);
            res = statement.executeQuery();

            res.next();
            System.out.println("Query 4: major degree that the youngest student has taken: " + res.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
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
