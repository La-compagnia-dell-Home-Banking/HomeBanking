package la_compagnia_dell_homebanking.homebanking.carta;

import la_compagnia_dell_homebanking.homebanking.TokenServlet;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class Carta_di_Credito implements CartaI {

	private String accountId, numeroCarta, cvv, conto_corrente;
	private LocalDate dataScadenza;
	
	
	public Carta_di_Credito(String accountId, String numeroCarta, String cvv, String conto_corrente,
			LocalDate dataScadenza) {
		this.accountId = accountId;
		this.numeroCarta = numeroCarta;
		this.cvv = cvv;
		this.conto_corrente = conto_corrente;
		this.dataScadenza = dataScadenza;
	}

	public Carta_di_Credito(String iban) throws SQLException {
		
		Connection connection=new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_di_credito WHERE conto='"+iban+"'");
		rs.next();
		accountId=rs.getString("account_id");
		numeroCarta=rs.getString("numero");
		cvv=rs.getString("cvv");
		this.conto_corrente=iban;
		dataScadenza=rs.getDate("scadenza").toLocalDate();
		stmt.close();
		rs.close();
		connection.close();
		
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * @return True se la data di scadenza è superata rispetto alla data attuale, false altrimenti
	 * Metodo per controllare se una carta è scaduta*/
	public boolean isScaduta() {
		
		if(dataScadenza.isBefore(LocalDate.now())) return true;
		else return false;
		
	}

	/**
	 * @author oleskiy.OS
	 * @param iban = conto (in DB)
	 * @return boolean - true if card was blocked properly, false if wasn't.
	 * This method blocks a credit card.
	 */
	public static boolean bloccareCarta(String iban) {
		MySQLConnection connection = new MySQLConnection();
		String query = "UPDATE carta_di_credito SET isBlocked=true WHERE conto =?";
		try {
			PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
			prstmt.setString(1, iban);
			int status = prstmt.executeUpdate();
			if(status == 1) {
				System.out.println(new StringBuilder().append("Credit card IBAN ").append(iban).append(" was blocked."));
				return true;
			}
		} catch (SQLException e) {
			MySQLConnection.printExceptions(e);
		} finally {
			MySQLConnection.closeAllConnections(connection);
		}
		System.out.println(new StringBuilder().append("Card ").append(iban).append(" doesn't exist."));
		return false;
	}

	@Override
	public String toString() {
		return "Carta_di_Credito [accountId=" + accountId + ", numeroCarta=" + numeroCarta + ", cvv=" + cvv
				+ ", conto_corrente=" + conto_corrente + ", dataScadenza=" + dataScadenza + "]";
	}
	
	@Override
	public String getAccountId() {
		return accountId;
	}


	@Override
	public String getNumeroCarta() {
		return numeroCarta;
	}


	@Override
	public LocalDate getDataScadenza() {
		return dataScadenza;
	}


	@Override
	public String getCvv() {
		return cvv;
	}


	public String getConto_corrente() {
		return conto_corrente;
	}

}
