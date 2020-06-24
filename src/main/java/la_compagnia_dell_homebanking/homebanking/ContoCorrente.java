package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import la_compagnia_dell_homebanking.homebanking.dao.AccountDao;
import la_compagnia_dell_homebanking.homebanking.dao.CartaDiCreditoDao;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author D'Inverno, Giuseppe Alessio
 * 
 * The class represents a bank account
 *
 */

public class ContoCorrente {
	private Carta_di_Credito carta;
	private Account account;
	private String accountId;
	private String iban;
	private static final int ABI = 45654;
	private static final int CAB = 78987;
	private static int iban_final = 0;
	private double saldo_disponibile;
	private double saldo_contabile;
	private ArrayList<Transazione> transazioni;
	
	

	public ContoCorrente(String accountId, String iban) {
		
		this.accountId=accountId;
		this.iban=iban;
		this.saldo_disponibile=0;
		this.saldo_contabile=0;
		
	}
	
	/**
	 * The constructor takes an Account in input, initializes a new Carta_di_Credito associated to this ContoCorrente
	 * @param account	The Account of the user
	 * @throws SQLException
	 */

	
	public ContoCorrente(Account account) throws SQLException {

		this.account = account;
		this.generateIBAN();
		saldo_disponibile = 0.00;
		saldo_contabile = 0.00;
		carta = new Carta_di_Credito(Integer.toString(account.getAccountID()),iban);
		account.aggiungiConto(this);
	
	}

	
	/**
	 * The constructor takes an iban in input and search the correspondent ContoCorrente in the database and initializes it
	 * @param iban	The Account of the user
	 * @throws SQLException
	 */
	public ContoCorrente(String iban) throws SQLException {
		this.iban = iban;
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * from conto_corrente WHERE iban=?";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, iban);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		account = AccountDao.getAccountFromDb(rs.getString("account_id"));
		if (CartaDiCreditoDao.readCarta(iban)==null)
			carta = new Carta_di_Credito(Integer.toString(account.getAccountID()), iban);

		saldo_disponibile = rs.getDouble("saldo_disponibile");
		saldo_contabile = rs.getDouble("saldo_contabile");
		account.aggiungiConto(this);
		prstmt.close();
		rs.close();
		connection.getMyConnection().close();

	}



	/**
	 * The method generates a random Capital letter.
	 * @return char		The generated capital letter.
	 */
	private static char rndChar() {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		return (char) ('A' + rnd % 26);


	}
	
	/**
	 * The method generates an IBAN.
	 */
	private void generateIBAN() {
		iban_final++;
		this.iban = "IT" + NumberGenerator.generateRandom(1, 99) + rndChar() + Integer.toString(ABI)
				+ Integer.toString(CAB) + NumberGenerator.addZeros(Integer.toString(iban_final), 12);
	}


	/**
	 * The method returns the iban associated to the ContoCorrente
	 * @return iban
	 */
	public String getIBAN() {
		return this.iban;
	}

	
	/**
	 * The method returns the saldo_disponibile of the ContoCorrente
	 * @return saldo_disponibile
	 */
	public double getSaldo_disponibile() {
		return saldo_disponibile;
	}

	
	/**
	 * The method returns the saldo_contabile of the ContoCorrente
	 * @return	saldo_contabile
	 */
	public double getSaldo_contabile() {
		return saldo_contabile;
	}


	
	/**
	 * The method returns the Account owner of this ContoCorrente
	 * @return account
	 */
	public Account getAccount() {
		return this.account;
	}



	
	

}
