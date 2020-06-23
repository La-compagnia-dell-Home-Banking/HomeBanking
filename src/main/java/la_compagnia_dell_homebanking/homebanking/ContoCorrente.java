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

public class ContoCorrente {
	private Carta_di_Credito carta;
	private Account account;
	private String iban;
	private static final int ABI = 45654;
	private static final int CAB = 78987;
	private static int iban_final = 0;
	private double saldo_disponibile;
	private double saldo_contabile;
	private ArrayList<Transazione> transazioni;

	public ContoCorrente(Account account) throws SQLException {

		this.account = account;
		this.generateIBAN();
		saldo_disponibile = 0.00;
		saldo_contabile = 0.00;
		carta = new Carta_di_Credito(Integer.toString(account.getAccountID()), iban);
		account.aggiungiConto(this);
	
	}

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

	private static String addZeros(String num, int length) {
		int numlength = num.length();
		String s = num;
		for (int i = 0; i < length - numlength; i++)
			s = '0' + s;

		return s;

	}


	private static char rndChar() {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		return (char) ('A' + rnd % 26);


	}

	private void generateIBAN() {
		iban_final++;
		this.iban = "IT" + NumberGenerator.generateRandom(1, 99) + rndChar() + Integer.toString(ABI)
				+ Integer.toString(CAB) + addZeros(Integer.toString(iban_final), 12);
	}


	public String getIBAN() {
		return this.iban;
	}

	

	public double getSaldo_disponibile() {
		return saldo_disponibile;
	}

	public double getSaldo_contabile() {
		return saldo_contabile;
	}



	public Account getAccount() {
		return this.account;
	}


//
//	private boolean findCartaAssociata() throws SQLException {
//		MySQLConnection connection = new MySQLConnection();
//		String query = "SELECT * FROM carta_di_credito WHERE conto=?";
//		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
//		prstmt.setString(1, this.iban);
//		ResultSet rs = prstmt.executeQuery();
//		rs.next();
//		if (rs.getString("numero") != null) {
//			carta = new Carta_di_Credito(rs.getString("account_id"));
//			prstmt.close();
//			rs.close();
//			connection.getMyConnection().close();
//			return true;
//		} else {
//			prstmt.close();
//			rs.close();
//			connection.getMyConnection().close();
//			return false;}
//
//	}
	
	

}
