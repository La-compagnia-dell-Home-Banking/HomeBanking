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

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.ContoCorrente;
import la_compagnia_dell_homebanking.homebanking.TokenServlet;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class ContoCorrenteDao {

	private static boolean insertCCToDb(ContoCorrente c) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, c.getIBAN());
		prstmt.setString(2, Integer.toString(c.getAccount().getAccountID()));
		prstmt.setDouble(3, c.getSaldo_disponibile());
		prstmt.setDouble(3, c.getSaldo_contabile());

		Boolean status = prstmt.execute();
		prstmt.close();
		connection.getMyConnection().close();
		return status;
	}

	public static boolean insertCCToDb(Account account, ContoCorrente c) throws SQLException {
		if (!account.equals(c.getAccount()))
			return false;
		return insertCCToDb(c);
	}

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
	 * @param amount: La somma da pagare
	 * @version 0.0.1
	 * Metodo per pagare con una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in uscita*/
	public static boolean pagaConContoCorrente(double amount, String iban) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, iban);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		if(TokenServlet.chiedi_codice(rs.getString("account_id"))) System.out.println("Codice ok!");
		
		
		double disponibile=rs.getDouble("saldo_disponibile");
		
		//controllo se il saldo disponibile è sufficiente
		if(disponibile<amount) return false;
		
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
		return true;
		
	}
	

	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che salva tutti i movimenti di un conto corrente su di un ArrayList "transazioni"*/
	public static ArrayList<Transazione> transazioniConto(String iban) throws SQLException {
		String q="SELECT * FROM conto_corrente WHERE iban='"+iban+"'";
		ArrayList<Transazione> transazioni=TransazioneDao.estrattoConto(q);
		return transazioni;
	}
	
	/**
	 * @author Gianmarco Polichetti
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
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le uscite di un conto corrente*/
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
}
