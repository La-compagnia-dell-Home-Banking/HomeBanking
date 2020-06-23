package la_compagnia_dell_homebanking.homebanking.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class AccountDao {

	public static Account getAccountFromDb(String acc) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM account WHERE account_id=?";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, acc);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		Account acco = new Account(rs.getString("account_id"));
		prstmt.close();
		rs.close();
		connection.getMyConnection().close();
		return acco;

	}
	
	public static boolean insertAccountToDb(String persona_id, boolean kind, String accountID) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, accountID);

		if (kind) {
			prstmt.setString(2, persona_id);
			prstmt.setString(3, null);
		} else {
			prstmt.setString(2, null);
			prstmt.setString(3, persona_id);
		}
		Boolean status = prstmt.execute();
		connection.getMyConnection().close();
		return status;
	}

	public static int controlAccount_id() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * from account";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		ResultSet rs = prstmt.executeQuery();
		int s, id = 0;
		while (rs.next()) { // Leggiamo i risultati
			s = Integer.parseInt(rs.getString("account_id"));
			if (id < s)
				id = s;

		}
		connection.getMyConnection().close();
		return id;

	}
	
	
}
