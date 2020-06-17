package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;

public class MySQLConnection {

    private Statement stmt = null;
    private Connection conn = null;

    public MySQLConnection() {
        String password = System.getenv("passhomebanking");
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?" +
                    "user=sql7347764&password=" + password);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    public MySQLConnection(String username, String password, String db_port, String db_url, String db_name, String db_type) {
        try {
            this.conn = DriverManager.getConnection("jdbc:" + db_type +":" + db_url + ":" + db_port + "/"
                    + db_name + "?user=" + username + "&password=" + password);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    public Statement getStmt() {
        return this.stmt;
    }

    public Connection getMyConnection() {
        return this.conn;
    }

}
