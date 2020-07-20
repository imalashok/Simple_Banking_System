//remove connection string duplication

package banking;

import java.sql.*;

public class SQLiteDatabase {
    private static String fileName;


    public static void setDatabaseName(String databaseName) {
        fileName = databaseName;
    }

    public static void createNewDatabase() {

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                //System.out.println("The driver name is " + meta.getDriverName());
                //System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:test.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createCardTable() {

        String createTableSQL = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertCard(String cardNumber, String cardPin) {

        String url = "jdbc:sqlite:" + fileName;
        String sql = "INSERT INTO card (number, pin) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean checkCardCredentials(String cardNumber, String cardPin) {

        String url = "jdbc:sqlite:" + fileName;
        String sql = "SELECT id "
                + "FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                if (rs.getInt("id") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static int checkBalance(String cardNumber, String cardPin) {
        int balance = 0;
        String url = "jdbc:sqlite:" + fileName;
        String sql = "SELECT balance "
                + "FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            //
            ResultSet rs = pstmt.executeQuery();

            balance = rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }


    public static boolean closeAccount(String cardNumber, String cardPin) {
        String url = "jdbc:sqlite:" + fileName;

        String sql = "DELETE FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            int deleted = pstmt.executeUpdate();
            if (deleted == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean updateBalance(String cardNumber, int income) {
        String url = "jdbc:sqlite:" + fileName;

        String sql = "UPDATE card SET balance = balance + ? "
                + "WHERE number = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, income);
            pstmt.setString(2, cardNumber);
            int updated = pstmt.executeUpdate();
            if (updated == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean isCardExists(String cardNumber) {
        String url = "jdbc:sqlite:" + fileName;

        String sql = "SELECT id "
                + "FROM card WHERE number = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, cardNumber);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                if (rs.getInt("id") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}