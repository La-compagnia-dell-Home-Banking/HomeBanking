package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


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

	public Carta_di_Credito(String accountId) throws SQLException {
		
		Connection connection=new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_di_credito WHERE account_id='"+accountId+"'");
		rs.next();
		this.accountId=accountId;
		numeroCarta=rs.getString("numero");
		cvv=rs.getString("cvv");
		conto_corrente=rs.getString("conto");
		dataScadenza=rs.getDate("scadenza").toLocalDate();
		stmt.close();
		rs.close();
		connection.close();
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
	
	public void pagaConCarta(double amount) {
		String query;

		query="INSERT INTO movimenti_conto(iban, nuovo_saldo, somma, is_accredito) VALUES"
				+ "('"+numeroCarta+"','"+nuovo_credito+"','"+amount+"','"+0+"')";
		Transazione.creaTransazione(query);
		
	}
	
	public void rinnovaCarta() {
		
	}

	@Override
	public String toString() {
		return "Carta_di_Credito [accountId=" + accountId + ", numeroCarta=" + numeroCarta + ", cvv=" + cvv
				+ ", conto_corrente=" + conto_corrente + ", dataScadenza=" + dataScadenza + "]";
	}
	
	

}
