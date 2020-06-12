package project.homeBanking;

import java.sql.*;

public class MySQLConnection {

    private Statement stmt = null;
    private Connection conn = null;

    public MySQLConnection() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7347764?user=sql7347764&password=" +
                "SLMMhsNp3G");
        stmt = conn.createStatement();
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection connect() {
        return this.conn;
    }

}
