package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;

public class MySQLConnection {

    private Statement stmt = null;
    private Connection conn = null;

    public MySQLConnection(String variabile_password) throws SQLException {
        String password = System.getenv(variabile_password);
        this.conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?user=sql7347764&password=" +
                password);
        stmt = conn.createStatement();
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection connect() {
        return this.conn;
    }

}
