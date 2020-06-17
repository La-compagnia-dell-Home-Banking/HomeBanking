package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class ContoCorrente {
	private Carta_di_Credito carta;
	private Account account;
	private String iban;
	private static final int ABI = 45654;
	private static final int CAB = 78987;
	private static int iban_final = 0;
	private double saldo_disponibile;
	private double saldo_contabile;

	public ContoCorrente(Account account) throws SQLException {

		this.account = account;
		this.generateIBAN();
		saldo_disponibile = 0.00;
		saldo_contabile = 0.00;
		carta = new Carta_di_Credito(Integer.toString(account.getAccountID()),
				NumberGenerator.generateRandom(10000000, 99999999) + NumberGenerator.generateRandom(10000000, 99999999),
				(NumberGenerator.generateRandom(100, 999)), iban, LocalDate.now().plusYears(4));
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
		account = getAccountFromDb(rs.getString("account_id"));
		if (!findCartaAssociata())
			carta = new Carta_di_Credito(Integer.toString(account.getAccountID()),
					NumberGenerator.generateRandom(10000000, 99999999)
							+ NumberGenerator.generateRandom(10000000, 99999999),
					(NumberGenerator.generateRandom(100, 999)), iban, LocalDate.now().plusYears(4));

		saldo_disponibile = rs.getDouble("saldo_disponibile");
		saldo_contabile = rs.getDouble("saldo_contabile");
		account.aggiungiConto(this);
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

	private boolean insertCCToDb() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, iban);
		prstmt.setString(2, Integer.toString(this.account.getAccountID()));
		prstmt.setDouble(3, this.saldo_disponibile);
		prstmt.setDouble(3, this.saldo_contabile);

		Boolean status = prstmt.execute();
		connection.getMyConnection().close();
		return status;
	}

	public boolean insertCCToDb(Account account) throws SQLException {
		if (!account.equals(this.account))
			return false;
		return insertCCToDb();
	}

	public static ArrayList<ContoCorrente> readConti() throws SQLException {

		ArrayList<ContoCorrente> lista = new ArrayList<ContoCorrente>();
		ResultSet rs;
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM conto_corrente";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);

			 rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati

				ContoCorrente conto = new ContoCorrente(rs.getString("iban"));
				lista.add(conto);
			}
			connection.getMyConnection().close();
		return lista;
	}

	public Account getAccount() {
		return this.account;
	}

	private static Account getAccountFromDb(String acc) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM account WHERE account_id=?";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, acc);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		Account acco = new Account(rs.getString("account_id"));
		connection.getMyConnection().close();
		return acco;

	}

	private boolean findCartaAssociata() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM carta_di_credito WHERE conto=?";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, this.iban);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		if (rs.getString("numero") != null) {
			carta = new Carta_di_Credito(rs.getString("account_id"));
			connection.getMyConnection().close();
			return true;
		} else {
			connection.getMyConnection().close();
			return false;}

	}

}
