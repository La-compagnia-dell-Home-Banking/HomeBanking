package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
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


	private boolean insertCCToDb() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, iban);
		prstmt.setString(2, Integer.toString(this.account.getAccountID()));
		prstmt.setDouble(3, this.saldo_disponibile);
		prstmt.setDouble(3, this.saldo_contabile);

		Boolean status = prstmt.execute();
		prstmt.close();
		connection.getMyConnection().close();
		return status;
	}

	public boolean insertCCToDb(Account account) throws SQLException {
		if (!account.equals(this.account))
			return false;
		return insertCCToDb();
	}

	public static ArrayList<ContoCorrente> readConti(String account_id) throws SQLException {

		ArrayList<ContoCorrente> lista = new ArrayList<ContoCorrente>();
		ResultSet rs;
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM conto_corrente WHERE account_id=?";
		
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, account_id);

			 rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati

				ContoCorrente conto = new ContoCorrente(rs.getString("iban"));
				lista.add(conto);
			}
			prstmt.close();
			rs.close();
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
		prstmt.close();
		rs.close();
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
			prstmt.close();
			rs.close();
			connection.getMyConnection().close();
			return true;
		} else {
			prstmt.close();
			rs.close();
			connection.getMyConnection().close();
			return false;}

	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: La somma da pagare
	 * @version 0.0.1
	 * Metodo per pagare con una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in uscita*/
	public void pagaConContoCorrente(double amount) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, iban);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		//controllo il codice token
		if(TokenServlet.chiedi_codice(Integer.toString(account.getAccountID()))) System.out.println("Codice ok!");
		
		//calcolo il nuovo saldo
		double nuovo_credito=rs.getDouble("saldo_disponibile")-amount;
				
		//aggiorno il conto con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE conto_corrente SET saldo_disponibile=?, saldo_contabile=? WHERE iban=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setDouble(2, nuovo_credito);
		pstmt.setString(3, iban);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_conto(data_transazione, orario_transazione, iban, nuovo_saldo, somma, is_accredito) VALUES(?,?,?,?,?,?)");
		new Transazione(LocalDate.now(), LocalTime.now(), iban, nuovo_credito, -amount, false).creaTransazione(query);
		
		//chiudo le connessioni al DB
		rs.close();
		pstmt.close();
		connection.close();
		
	}
	

	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che salva tutti i movimenti di un conto corrente su di un ArrayList "transazioni"*/
	public void transazioniCarta() throws SQLException {
		String q="SELECT * FROM conto_corrente WHERE iban='"+iban+"'";
		transazioni=Transazione.estrattoConto(q);
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente l'estratto conto di un conto corrente*/
	public File getEstrattoConto() throws SQLException {		
		transazioniCarta();
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Estratto conto corrente "+iban+".txt", true));) {
			out.write("Estratto conto corrente: "+iban+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");			
			for(Transazione t:transazioni) {
				out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Estratto conto corrente "+iban+".txt");
		return res;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le entrate di un conto corrente*/
	public File getEntrate() throws SQLException {
		transazioniCarta();
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Entrate conto corrente "+iban+".txt", true));) {
			out.write("Entrate conto corrente: "+iban+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");			
			for(Transazione t:transazioni) {
				if(t.isAccredito()) out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Entrate conto corrente "+iban+".txt");
		return res;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le uscite di un conto corrente*/
	public File getUscite() throws SQLException {
		transazioniCarta();
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Uscite conto corrente "+iban+".txt", true));) {
			out.write("Uscite conto corrente: "+iban+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");			
			for(Transazione t:transazioni) {
				if(!t.isAccredito()) out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Uscite conto corrente "+iban+".txt");
		return res;

	}

}
