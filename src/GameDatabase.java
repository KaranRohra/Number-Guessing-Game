import javax.swing.*;
import java.sql.*;

public class GameDatabase {
    private static Statement statement;
    private static Connection connection;
    private static ResultSet resultSet;

    public static void initializeDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/number_guessing_game", "root", "MySQL2001*");
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertIntoDatabase(int result, int moves) {
        String query = "INSERT INTO recent_games VALUES(" + result + ", " + moves + ");";

        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteFromDatabase() {
        String query = "DELETE FROM recent_games LIMIT 1";

        try {
            resultSet = statement.executeQuery("SELECT COUNT(*) AS NUMBER_OF_ROWS FROM recent_games;");
            resultSet.next();
            if (resultSet.getInt("NUMBER_OF_ROWS") > 5) {
                statement.executeUpdate(query);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static int fetchData(JLabel[] recentData) {
        int i = 4;
        try {
            resultSet = statement.executeQuery("SELECT * FROM recent_games;");
            while (i >= 0 && resultSet.next()) {
                String result = resultSet.getInt("RESULT") == 0 ? "Lose" : "Won";
                recentData[i--].setText("<html>&emsp;&emsp; " + result +
                        "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;" + resultSet.getInt("MOVES") + "</html>");
            }
            return i;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return i;
    }

    public static void closeDatabase() {
        try {
            statement.close();
            connection.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
