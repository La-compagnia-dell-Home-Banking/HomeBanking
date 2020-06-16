package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;

public class MySQLConnection {

    private Statement STMT = null;
    private Connection CONN = null;

    public MySQLConnection() {
        String password = "SLMMhsNp3G";//System.getenv("passhomebanking");
        try {
            this.CONN = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?" +
                    "user=sql7347764&password=" + password);
            STMT = CONN.createStatement();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    public MySQLConnection(String username, String password, String db_port, String db_url, String db_name, String db_type) {
        try {
            this.CONN = DriverManager.getConnection("jdbc:" + db_type +":" + db_url + ":" + db_port + "/"
                    + db_name + "?user=" + username + "&password=" + password);
            STMT = CONN.createStatement();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        }
    }

    public Statement getSTMT() {
        return this.STMT;
    }

    public Connection getMyConnection() {
        return this.CONN;
    }

}
