package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transazione {

	private LocalDate data;
	private LocalTime orario;
	private String numero;
	private double saldo, movimento;
	private boolean accredito;
	
	
	public Transazione(LocalDate data, LocalTime orario, String numero, double saldo, double movimento,
			boolean accredito) {

		this.data = data;
		this.orario = orario;
		this.numero = numero;
		this.saldo = saldo;
		this.movimento = movimento;
		this.accredito = accredito;
	}

	public void creaTransazione(String query) throws SQLException {
		
		Connection connection = new MySQLConnection().getMyConnection();
		PreparedStatement pstmt = connection.prepareStatement(query);
		//(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito)
		pstmt.setDate(1, Date.valueOf(data));
		pstmt.setTime(2, Time.valueOf(orario));
		pstmt.setString(3, numero);
		pstmt.setDouble(4, saldo);
		pstmt.setDouble(5, movimento);
		pstmt.setBoolean(6, accredito);
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
		stmt.close();
		connection.close();
		
		return transazioni;
	}

	public LocalDate getData() {
		return data;
	}

	public LocalTime getOrario() {
		return orario;
	}

	public String getNumero() {
		return numero;
	}

	public double getSaldo() {
		return saldo;
	}

	public double getMovimento() {
		return movimento;
	}

	public boolean isAccredito() {
		return accredito;
	}
	
	
	
	
}
