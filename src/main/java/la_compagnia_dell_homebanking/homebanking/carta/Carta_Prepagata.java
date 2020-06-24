package la_compagnia_dell_homebanking.homebanking.carta;

import la_compagnia_dell_homebanking.homebanking.NumberGenerator;
import la_compagnia_dell_homebanking.homebanking.Transazione;

import javax.json.bind.annotation.JsonbCreator;
import java.time.LocalDate;
import java.util.ArrayList;

/**@author Gianmarco Polichetti
 * Classe che definisce l'oggetto carta prepagata*/
public class Carta_Prepagata implements CartaI {
	
	private String accountId, numeroCarta, cvv;
	private double creditoResiduo;
	private LocalDate dataScadenza;
	private ArrayList<Transazione> transazioni;

	@JsonbCreator
	public Carta_Prepagata(String accountId) {
		this.accountId = accountId;
		this.numeroCarta = NumberGenerator.generateCardNumber();
		this.cvv = NumberGenerator.generateCvvNumber();
		this.creditoResiduo=0.0;
		this.dataScadenza = LocalDate.now().plusYears(4);
	}

	public Carta_Prepagata(String accountId, String numeroCarta, String cvv, double creditoResiduo,
						   LocalDate dataScadenza) {
		this.accountId = accountId;
		this.numeroCarta = numeroCarta;
		this.cvv = cvv;
		this.creditoResiduo = creditoResiduo;
		this.dataScadenza = dataScadenza;
	}

	public Carta_Prepagata(String accountId, double creditoResiduo) {
		this.accountId = accountId;
		this.numeroCarta = NumberGenerator.generateCardNumber();
		this.cvv = NumberGenerator.generateCvvNumber();
		this.creditoResiduo=creditoResiduo;
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
		return "Carta_Prepagata [accountId=" + accountId + ", numeroCarta=" + numeroCarta + ", cvv=" + cvv
				+ ", creditoResiduo=" + creditoResiduo + ", dataScadenza=" + dataScadenza + "]";
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
	public String getCvv() {
		return cvv;
	}


	public double getCreditoResiduo() {
		return creditoResiduo;
	}

	@Override
	public LocalDate getDataScadenza() {
		return dataScadenza;
	}

}