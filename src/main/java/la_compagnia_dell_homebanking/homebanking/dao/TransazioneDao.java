package la_compagnia_dell_homebanking.homebanking.dao;

import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;

/** @author Gianmarco Polichetti
 * @version 0.0.1
 * Manages possible operations on a transaction: insertion,
 * return of transaction lists */
public class TransazioneDao {

	/**
	* @author Gianmarco Polichetti
	* @param query: the query launched by the methods to create an incoming or outgoing transaction
	* @version 0.0.1
	* Takes as input a query and generates a transaction which is saved in the DB */
	public static void creaTransazione(Transazione t, String query) throws SQLException {
		
		Connection connection = new MySQLConnection().getMyConnection();
		PreparedStatement pstmt = connection.prepareStatement(query);
		//(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito)
		pstmt.setDate(1, Date.valueOf(t.getData()));
		pstmt.setTime(2, Time.valueOf(t.getOrario()));
		pstmt.setString(3, t.getNumero());
		pstmt.setDouble(4, t.getSaldo());
		pstmt.setDouble(5, t.getMovimento());
		pstmt.setBoolean(6, t.isAccredito());
		pstmt.execute();
		pstmt.close();
		connection.close();
		
	}
	
	
	/**
	* @author Gianmarco Polichetti
	* @Param query: query to create a bank statement of a prepaid card	
	* @version 0.0.1
	* Takes a query as input and generates a list of transactions */
	public static ArrayList<Transazione> estrattoContoCarta(String query) throws SQLException{
		
		ArrayList<Transazione> transazioni=new ArrayList<Transazione>();
		Connection connection = new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			transazioni.add(new Transazione(rs.getDate("data_transazione").toLocalDate(), rs.getTime("orario_transazione").toLocalTime(),
					rs.getString("numero"), rs.getDouble("nuovo_saldo"), rs.getDouble("somma"), rs.getBoolean("is_accredito")));
		}
		rs.close();
		stmt.close();
		connection.close();
		
		return transazioni;
	}
	
	/**
	* @author Gianmarco Polichetti
	* @Param query: query to create a bank statement of a checking account	
	* @version 0.0.1
	* Takes a query as input and generates a list of transactions */
	public static ArrayList<Transazione> estrattoConto(String query) throws SQLException{
		
		ArrayList<Transazione> transazioni=new ArrayList<Transazione>();
		Connection connection = new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			transazioni.add(new Transazione(rs.getDate("data_transazione").toLocalDate(), rs.getTime("orario_transazione").toLocalTime(),
					rs.getString("iban"), rs.getDouble("nuovo_saldo"), rs.getDouble("somma"), rs.getBoolean("is_accredito")));
		}
		rs.close();
		stmt.close();
		connection.close();
		
		return transazioni;
	}
}
