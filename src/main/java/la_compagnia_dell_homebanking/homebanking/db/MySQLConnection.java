package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;

public class MySQLConnection {

    private final Statement STMT;
    private final Connection CONN;

    public MySQLConnection() throws SQLException {
        String password = System.getenv("passhomebanking");
        this.CONN = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?" +
                "user=sql7347764&password=" + password);
        STMT = CONN.createStatement();
    }

    public MySQLConnection(String username, String password, String db_port, String db_url, String db_name, String db_type) throws SQLException {
        this.CONN = DriverManager.getConnection("jdbc:" + db_type +":" + db_url + ":" + db_port + "/"
                + db_name + "?user=" + username + "&password=" + password);
        STMT = CONN.createStatement();
    }

    public Statement getSTMT() {
        return this.STMT;
    }

    public Connection connect() {
        return this.CONN;
    }

}
