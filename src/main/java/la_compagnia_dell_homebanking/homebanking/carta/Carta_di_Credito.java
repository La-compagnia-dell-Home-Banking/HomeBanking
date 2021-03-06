package la_compagnia_dell_homebanking.homebanking.carta;

import la_compagnia_dell_homebanking.homebanking.NumberGenerator;
import la_compagnia_dell_homebanking.homebanking.TokenServlet;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
/**@author Gianmarco Polichetti
 * Classe che definisce l'oggetto carta di credito*/

public class Carta_di_Credito implements CartaI {

	private String accountId, numeroCarta, cvv, conto_corrente;
	private LocalDate dataScadenza;
	
	
	public Carta_di_Credito(String accountId, String conto_corrente) {
		this.accountId = accountId;
		this.numeroCarta = NumberGenerator.generateCardNumber();
		this.cvv = NumberGenerator.generateCvvNumber();
		this.conto_corrente = conto_corrente;
		this.dataScadenza = LocalDate.now().plusYears(4);
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
