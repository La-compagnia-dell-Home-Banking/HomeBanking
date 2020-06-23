package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;

/**
 * MySQLConnection class establish connection to MySQL database
 * @author oleskiy.OS
 *
 */
public class MySQLConnection {

    private Statement stmt = null;
    private Connection conn = null;
    private SQLException exception;

    /**
     * Default constructor connects to local DB.
     * @throws ClassNotFoundException
     */
    public MySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String password = System.getenv("passhomebanking");
        try {
            //"jdbc:mysql://localhost?useTimezone=true&serverTimezone=UTC","USER-NAME","PASSWORD
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/home_banking?useTimezone=true&serverTimezone=UTC","oleskiy", "3487218057.oleskiy");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            this.exception = e;
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    /**
     * Constructor creates remote DB connection. To distinguish constructors simply add true or false. Value is dummy
     * @throws ClassNotFoundException
     */
    public MySQLConnection(boolean isLocal) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String password = System.getenv("passhomebanking");
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?" +
                    "user=sql7347764&password=" + password);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            this.exception = e;
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    /**
     * Constructor creates customized DB connection
     * @param username your DB username
     * @param password your DB password
     * @param db_port by default mariaDB: '3306'
     * @param db_url examples: 'localhost' , 'example.com'
     * @param db_name examples: 'home_banking'
     * @throws ClassNotFoundException
     */
    public MySQLConnection(String username, String password, String db_port, String db_url, String db_name, String db_type) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + db_url + ":" + db_port + "/"
                    + db_name + "?user=" + username + "&password=" + password);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            this.exception = e;
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    /**
     * Getter
     * @return statement
     */
    public Statement getStmt() {
        return this.stmt;
    }

    /**
     * Getter
     * @return connection
     */
    public Connection getMyConnection() {
        return this.conn;
    }

    /**
     * Getter
     * @return cexception
     */
    public SQLException getException() {
        return exception;
    }

    /**
     * This method closes all opened connections
     * @author oleskiy.OS
     * @param connection
     * @param resultSet
     * @throws SQLException if connection was not closed.
     */
    public static void closeAllConnections(MySQLConnection connection, ResultSet resultSet) {
        if (connection.getMyConnection() != null) try { connection.getMyConnection().close(); } catch (SQLException e) { printExceptions(e); }
        if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { printExceptions(e); }
        if (connection.getStmt() != null) try { connection.getStmt().close(); } catch (SQLException e) { printExceptions(e); }
    }

    /**
     * This method closes all opened connections
     * @author oleskiy.OS
     * @param connection
     * @throws SQLException if connection was not closed.
     */
    public static void closeAllConnections(MySQLConnection connection) {
        if (connection.getMyConnection() != null) try { connection.getMyConnection().close(); } catch (SQLException e) { printExceptions(e); }
        if (connection.getStmt() != null) try { connection.getStmt().close(); } catch (SQLException e) { printExceptions(e); }
    }

    /**
     * Simple error printer
     * @author oleskiy.OS
     * @param e
     */
    public static void printExceptions(SQLException e) {
        System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
        System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
        System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
    }
}