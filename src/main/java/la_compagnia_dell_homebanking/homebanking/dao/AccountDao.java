package la_compagnia_dell_homebanking.homebanking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class AccountDao {

	public static Account getAccountFromDb(String acc) throws SQLException {
		MySQLConnection connection = new MySQLConnection(true);
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
		MySQLConnection connection = new MySQLConnection(true);
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
		MySQLConnection connection = new MySQLConnection(true);
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
	
	
	/**
	 * @author Gianmarco Polichetti
	 * @param account_id: The account that requires a token generator
	 * @version 0.0.1
	 * That create a unique token generator for an account. This can be used for make all the operation.*/
	public static boolean addToken(String account_id) {
		

		try {
			Connection connection = new MySQLConnection(true).getMyConnection();
			
			String query = "INSERT INTO token VALUES(?, ?, ?, ?)";
			PreparedStatement prstmt = connection.prepareStatement(query);
			prstmt.setString(1, account_id);
			prstmt.setDate(2, Date.valueOf(LocalDate.now()));
			prstmt.setTime(3, Time.valueOf(LocalTime.now()));
			prstmt.setInt(4, (int)(((Math.random())*999999)+1));
			Boolean status = prstmt.execute();
			connection.close();
			return status;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
}
