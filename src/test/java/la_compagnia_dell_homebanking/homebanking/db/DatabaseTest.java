package la_compagnia_dell_homebanking.homebanking.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * DatabaseTest class - tests the MySQLConnection class
 * @author oleskiy OS
 * @version 1.0
 * test_getDatabaseName() - should verify our database name
 * test_getConnection() - should check if database connection is opened
 * test_getSQLException() - should simulate throwing the SQLexception,
 */

public class DatabaseTest {

    /**
     * @throws SQLException
     */

    @Test
    void test_getDatabaseName() throws SQLException {
        MySQLConnection connection = new MySQLConnection();
        ResultSet rs = connection.getSTMT().executeQuery("SHOW DATABASES;");
        //then
        String actual = null;
        while (rs.next()) {
            actual = rs.getString(1);
        }
        //expect
        String expected = "sql7347764";
        assertEquals(expected, actual, "deve essere uguale al nome del nostro db");
        connection.getSTMT().close();
    }

    /**
     * The method checks database connection status by using isClosed() method
     * @throws SQLException
     * @return false - is connection is opened
     */

    @Test
    @DisplayName("Connection status: isClose()")
    void test_getConnection() throws SQLException {
        // given
        MySQLConnection connection = new MySQLConnection();

        //then
        Boolean actual = connection.getSTMT().isClosed();
        //expect
        Boolean expected = false;
        assertEquals(expected, actual, "la connessione deve essere aperta");
        connection.getSTMT().close();
    }

    /**
     * This method tries to establish connection to database providing fake data to MySQLConnection classe's
     * constructor and thus throwing SQLException
     * @throws SQLException
     * @param for MySQLConnection class constructor - username, password, db_port, db_url, db_name, db_type
     */
    @Test
    void test_getSQLException() throws SQLException {
        assertThrows(SQLException.class, () -> {
            MySQLConnection connection = new MySQLConnection("sql7347763", "password", "3306", "//sql7.freemysqlhosting.net",
                    "db_name", "mysql");
        }, "delimiter must be a single character");
    }

}
