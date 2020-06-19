package la_compagnia_dell_homebanking.homebanking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class TransazioneDao {

	/**
	 * @author Gianmarco Polichetti
	 * @param query: la query lanciata dai metodi per creare una transazione in entrata o in uscita
	 * @version 0.0.1
	 * Prende in input una query e genera una transazione che viene salvata nel DB*/
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
