package la_compagnia_dell_homebanking.homebanking.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.ContoCorrente;
import la_compagnia_dell_homebanking.homebanking.TokenServlet;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;
import la_compagnia_dell_homebanking.homebanking.exceptions.ContoNotFoundException;
import la_compagnia_dell_homebanking.homebanking.exceptions.CreditExceedException;
import la_compagnia_dell_homebanking.homebanking.exceptions.CreditNotAvailableException;
import la_compagnia_dell_homebanking.homebanking.exceptions.WrongPasswordException;

/**@author Gianmarco Polichetti, Giuseppe Alessio D'Inverno
 * @version 0.0.1
 * Gestisce le operazioni possibili su di un conto corrente: inserimento, eliminazione, 
 * lettura delle informazioni del conto e pagamenti in entrata ed uscita  */

public class ContoCorrenteDao {

	private static boolean insertCCToDb(ContoCorrente c) throws SQLException {
		Connection connection = new MySQLConnection().getMyConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.prepareStatement(query);
		prstmt.setString(1, c.getIBAN());
		prstmt.setString(2, Integer.toString(c.getAccount().getAccountID()));
		prstmt.setDouble(3, c.getSaldo_disponibile());
		prstmt.setDouble(3, c.getSaldo_contabile());

		Boolean status = prstmt.execute();
		prstmt.close();
		connection.close();
		return status;
	}

	public static boolean insertCCToDb(Account account, ContoCorrente c) throws SQLException {
		if (!account.equals(c.getAccount()))
			return false;
		return insertCCToDb(c);
	}

	/**
	 * @author Gianmarco Polichetti, Giuseppe Alessio D'Inverno
	 * @param account_id The id of the owner of the checking account's
	 * @version 0.0.1
	 * Generate a list of checking account's linked to the account id*/	
	
	public static ArrayList<ContoCorrente> readConti(String account_id) throws SQLException {

		ArrayList<ContoCorrente> lista = new ArrayList<ContoCorrente>();
		ResultSet rs;
		Connection connection = new MySQLConnection().getMyConnection();
		String query = "SELECT * FROM conto_corrente WHERE account_id=?";
		
		PreparedStatement prstmt = connection.prepareStatement(query);
		prstmt.setString(1, account_id);

			 rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati

				ContoCorrente conto = new ContoCorrente(rs.getString("iban"));
				lista.add(conto);
			}
			prstmt.close();
			rs.close();
			connection.close();
		return lista;
	}


	/**
	 * @author Gianmarco Polichetti
	 * @param amount: the amount to be paid
	 * @param iban the iban of the checking account from which the transaction starts
	 * @version 0.0.1
	 * Generate an outcoming transaction*/
	public static boolean pagaConBonifico(double amount, String iban) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, iban);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		

		
		
		double disponibile=rs.getDouble("saldo_disponibile");
		
		//controllo se il saldo disponibile è sufficiente
		if(disponibile<amount) throw new CreditNotAvailableException();
		
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
		Transazione t=new Transazione(LocalDate.now(), LocalTime.now(), iban, nuovo_credito, -amount, false);
		TransazioneDao.creaTransazione(t, query);
		
		//chiudo le connessioni al DB
		rs.close();
		pstmt.close();
		connection.close();
		return status;
		
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: the amount of movement
	 * @param iban the iban of the checking account that receives a transaction
	 * @version 0.0.1
	 * Generate an incoming transaction*/
	public static boolean entrataConto(double amount, String iban) throws SQLException {

		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, iban);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		double disponibile=rs.getDouble("saldo_disponibile");
				
		//calcolo il nuovo saldo
		double nuovo_credito=disponibile+amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE conto_corrente SET saldo_disponibile=?, saldo_contabile=? WHERE iban=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setDouble(2, nuovo_credito);
		pstmt.setString(3, iban);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?, ?, ?, ?, ?, ?)");
		Transazione t=new Transazione(LocalDate.now(), LocalTime.now(), iban, nuovo_credito, +amount, true);
		TransazioneDao.creaTransazione(t, query);
		
		//chiudo le connessioni al DB
		rs.close();
		pstmt.close();
		connection.close();
		
		return !status;
		
	}
	

	/**
	 * @author Gianmarco Polichetti
	 * @param iban the iban of the checking account to be analyzed
	 * @version 0.0.1
	 * Metodo che salva tutti i movimenti di un conto corrente su di un ArrayList "transazioni"*/
	public static ArrayList<Transazione> transazioniConto(String iban) throws SQLException {
		String q="SELECT * FROM conto_corrente WHERE iban='"+iban+"'";
		ArrayList<Transazione> transazioni=TransazioneDao.estrattoConto(q);
		return transazioni;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param iban the iban of the checking account to be analyzed
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente l'estratto conto di un conto corrente*/
	public static File getEstrattoConto(String iban) throws SQLException {		
		
		ArrayList<Transazione> transazioni=transazioniConto(iban);
		
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
	 * @param iban the iban of the checking account to be analyzed
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le entrate di un conto corrente*/
	public static File getEntrate(String iban) throws SQLException {
		ArrayList<Transazione> transazioni=transazioniConto(iban);
		
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
	 * @param iban the iban of the checking account to be analyzed
	 * @version 0.0.1
	 * Writes and returns a file containing the issues of a checking account*/
	public static File getUscite(String iban) throws SQLException {
		ArrayList<Transazione> transazioni=transazioniConto(iban);
		
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
	
	
	/**
	 * @author Giuseppe Alessio D'inverno
	 * @param account
	 * @param conto
	 * @version 0.0.1
	 * Richiede la chiusura del conto da parte di un amministratore di sistema*/
	public static boolean richiestaChiusuraConto(Account account, ContoCorrente conto) throws IOException, SQLException {
		if(!account.getLista_carte().contains(conto)) throw new ContoNotFoundException();
		
		String password;
		System.out.println("Inserisci password");
		Scanner in= new Scanner(System.in);
		password=in.next();
		in.close();
		if (!password.equals(account.getPassword())) throw new WrongPasswordException();
		
		FileWriter file=new FileWriter("richiesta_chiusura_"+account.getAccountID());
		BufferedWriter bf=new BufferedWriter(file);
		if(account.getPersona() instanceof PersFisica)
		{bf.write("Il cliente "+account.getPersona().getNome()+" "+((PersFisica)account.getPersona()).getCognome()+
				" richiede la chiusura del conto corrente "+conto.getIBAN()+".");
		bf.flush();}
		else	
		{bf.write("Il cliente "+account.getPersona().getNome()+
				" richiede la chiusura del conto corrente "+conto.getIBAN()+".");
		bf.flush();}
		bf.close();
		account.setRichiestaChiusura(true);
		return true;
		

	}
	
	/**
	 * @author Giuseppe Alessio D'inverno
	 * @param account
	 * @param conto
	 * @version 0.0.1
	 * Questo metodo può essere richiamato da un amministratore di sistema per chiudere un conto se ne è stata fatta richiesta*/
	public boolean chiusuraConto(Account account, ContoCorrente conto) throws SQLException {
		if(!account.isRichiestaChiusura())
			throw new RuntimeException("Non è stata fatta richiesta di chiusura del conto");

			//connetto al DB
			Connection connection = new MySQLConnection().getMyConnection();
			
			//cerco il conto nel DB per eliminarlo
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM account WHERE account_id=?");
			pstmt.setString(1, Integer.toString(account.getAccountID()));
			
			//eseguo la query e salvo il risultato dell'operazione in una boolean
			boolean flag=pstmt.execute();
			
			//chiudo le connessioni al DB e restituisco il risultato
			connection.close();
			pstmt.close();
			account.setChiuso(flag);
			return flag;
		}
}
