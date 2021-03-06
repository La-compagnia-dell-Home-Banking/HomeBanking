package la_compagnia_dell_homebanking.homebanking.dao;

import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;
import la_compagnia_dell_homebanking.homebanking.exceptions.CreditExceedException;
import la_compagnia_dell_homebanking.homebanking.exceptions.CreditNotAvailableException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**@author Gianmarco Polichetti
 * @version 0.0.1
 * Manages possible operations on a prepaid card: insertion, deletion,
 * reading card information and incoming and outgoing payments */

public class CartaPrepagataDao {
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * @return carta: la carta collegata al conto
	 * Legge la carta di credito collegata al conto dal DB e la restituisce*/
	
	public static Carta_Prepagata readCarta(String numeroCarta) throws SQLException {

			Connection connection=new MySQLConnection().getMyConnection();
			Carta_Prepagata carta = null;
			String query = "SELECT * FROM carta_prepagata WHERE numero=?";
			PreparedStatement prstmt = connection.prepareStatement(query);
			prstmt.setString(1, numeroCarta);
			
			ResultSet rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati


				carta = new Carta_Prepagata(rs.getString("account_id"), rs.getString("numero"), rs.getString("cvv"),
						rs.getDouble("credito_Residuo"), rs.getDate("scadenza").toLocalDate());

			}
			rs.close();
			prstmt.close();
			connection.close();
			return carta;
	}

	/**
	 * @author Gianmarco Polichetti
	 * @param account_id: l'account del quale si vogliono conoscere le carte prepagate collegate
	 * @version 0.0.1
	 * @return lista: la lista delle carte collegate all'account
	 * Legge le carte prepagate collegate all'account dal DB e le salva in un ArrayList*/
	public static ArrayList<Carta_Prepagata> readCarte(String account_id) throws SQLException {

			Connection connection=new MySQLConnection().getMyConnection();
			ArrayList<Carta_Prepagata> lista = new ArrayList<Carta_Prepagata>();
			String query = "SELECT * FROM carta_prepagata WHERE account_id=?";
			PreparedStatement prstmt = connection.prepareStatement(query);
			prstmt.setString(1, account_id);
			
			ResultSet rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati

				Carta_Prepagata carta = new Carta_Prepagata(rs.getString("account_id"), rs.getString("numero"), rs.getString("cvv"),
						rs.getDouble("credito_Residuo"), rs.getDate("scadenza").toLocalDate());

				lista.add(carta);
			}
			connection.close();
			return lista;
	}
	
	
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: La somma da pagare
	 * @version 0.0.1
	 * Metodo per pagare con una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in uscita*/
	public static boolean pagaConCarta(double amount, String numeroCarta) throws SQLException, RuntimeException  {
			if(CartaPrepagataDao.isblocked(numeroCarta)) {
				throw new RuntimeException("La carta è bloccata, non puoi effettuare i pagamenti.");
			}

		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM carta_prepagata WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		double disponibile=rs.getDouble("credito_residuo");
		
		//controllo se il saldo disponibile è sufficiente
		if(disponibile<amount) throw new CreditNotAvailableException();
			
		
		
		//calcolo il nuovo saldo
		double nuovo_credito=rs.getDouble("credito_residuo")-amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE carta_prepagata SET credito_residuo =? WHERE numero=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setString(2, numeroCarta);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?,?,?,?,?,?)");
		Transazione t=new Transazione(LocalDate.now(), LocalTime.now(), numeroCarta, nuovo_credito, -amount, false);
		TransazioneDao.creaTransazione(t,query);
		//chiudo le connessioni al DB
		rs.close();
		pstmt.close();
		connection.close();
		return !status;
		
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: La somma da ricaricare
	 * @version 0.0.1
	 * Metodo per ricaricare una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in entrata*/
	public static boolean ricaricaCarta(double amount, String numeroCarta) throws SQLException {
		if(CartaPrepagataDao.isblocked(numeroCarta)) {
			throw new RuntimeException("La carta è bloccata, non puoi effettuare la ricarica.");
		}
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM carta_prepagata WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		double disponibile=rs.getDouble("credito_residuo");
		
		//controllo se il saldo disponibile è sufficiente
		if(disponibile+amount >= 5000) throw new CreditExceedException();
		
		
		//calcolo il nuovo saldo
		double nuovo_credito=disponibile+amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE carta_prepagata SET credito_residuo =? WHERE numero=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setString(2, numeroCarta);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?, ?, ?, ?, ?, ?)");
		Transazione t=new Transazione(LocalDate.now(), LocalTime.now(), numeroCarta, nuovo_credito, +amount, true);
		TransazioneDao.creaTransazione(t, query);
		
		//chiudo le connessioni al DB
		rs.close();
		pstmt.close();
		connection.close();
		
		return !status;
		
	}


	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo per rinnovare una carta prepagata, la nuova carta cambiera il numero, la data di scadenza e il cvv
	 * ma manterrà il credito residuo*/
	public static boolean rinnovaCarta(String vecchioNumero) {
		Carta_Prepagata vecchia = null;
		Carta_Prepagata rinnovata = null;

		try {
			vecchia=CartaPrepagataDao.readCarta(vecchioNumero);
			rinnovata=new Carta_Prepagata(vecchia.getAccountId(), vecchia.getCreditoResiduo());
			if(CartaPrepagataDao.inserisciCartaToDb(rinnovata)&&CartaPrepagataDao.eliminaCartaFromDb(vecchia.getNumeroCarta())) return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param nuovaCarta: Una carta appena creata 
	 * @version 0.0.1
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per inserire una carta nel DB*/
	public static boolean inserisciCartaToDb(Carta_Prepagata nuovaCarta) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//inserisco i dati della carta
		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO carta_prepagata(account_id, credito_Residuo, numero, scadenza, cvv) VALUES(?,?,?,?,?)");
		pstmt.setString(1, nuovaCarta.getAccountId());
		pstmt.setDouble(2, nuovaCarta.getCreditoResiduo());
		pstmt.setString(3, nuovaCarta.getNumeroCarta());
		pstmt.setDate(4, Date.valueOf(nuovaCarta.getDataScadenza()));
		pstmt.setString(5, nuovaCarta.getCvv());
		
		//eseguo la query e salvo il risultato dell'operazione in una boolean
		boolean flag=pstmt.execute();
		
		//chiudo le connessioni al DB
		connection.close();
		pstmt.close();
		return !flag;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per eliminare una carta dal DB in base al numero della carta*/
	public static boolean eliminaCartaFromDb(String numeroCarta) throws SQLException {

		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta nel DB per eliminarla
		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM carta_prepagata WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		
		//eseguo la query e salvo il risultato dell'operazione in una boolean
		boolean flag=pstmt.execute();
		
		//chiudo le connessioni al DB
		connection.close();
		pstmt.close();
		return flag;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che salva tutti i movimenti di una carta su di un ArrayList "transazioni"*/
	public static ArrayList<Transazione> transazioniCarta(String numeroCarta) throws SQLException {
		
		String q="SELECT * FROM movimenti_carta_prepagata WHERE numero='"+numeroCarta+"'";
		ArrayList<Transazione> transazioni=TransazioneDao.estrattoContoCarta(q);
		return transazioni;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente l'estratto conto di una carta*/
	public static File getEstrattoConto(String numeroCarta) throws SQLException {	
		
		ArrayList<Transazione> transazioni=transazioniCarta(numeroCarta);
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Estratto conto "+numeroCarta+".txt", true));) {
			out.write("Estratto conto carta numero: "+numeroCarta+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");			
			for(Transazione t:transazioni) {
				out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Estratto conto "+numeroCarta+".txt");
		return res;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le entrate di una carta*/
	public static File getEntrate(String numeroCarta) throws SQLException {
		ArrayList<Transazione> transazioni=transazioniCarta(numeroCarta);
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Entrate "+numeroCarta+".txt", true));) {
			out.write("Entrate carta numero: "+numeroCarta+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");			
			for(Transazione t:transazioni) {
				if(t.isAccredito()) out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Entrate "+numeroCarta+".txt");
		return res;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente le uscite di una carta*/
	public static File getUscite(String numeroCarta) throws SQLException {
		ArrayList<Transazione> transazioni=transazioniCarta(numeroCarta);
		
		try(BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Uscite "+numeroCarta+".txt", true));) {
			out.write("Uscite carta numero: "+numeroCarta+"\n"+LocalDate.now()+" "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+"\n\n");
			for(Transazione t:transazioni) {
				if(!t.isAccredito()) out.write(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo()+"\n\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File res=new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\HomeBanking\\Uscite "+numeroCarta+".txt");
		return res;

	}
	
	/**
	 * @author oleskiy.OS
	 * @param numero = conto (in DB)
	 * @return boolean - true if card was blocked properly, false if wasn't.
	 * This method blocks a credit card.
	 */
	public static boolean bloccaCarta(String numero) {
		MySQLConnection connection = new MySQLConnection();
		String query = "UPDATE carta_prepagata SET isBlocked=true WHERE numero =?";
		try {
			PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
			prstmt.setString(1, numero);
			int status = prstmt.executeUpdate();
			if(status == 1) {
				System.out.println(new StringBuilder().append("Prepayed Card Number ").append(numero).append(" was blocked."));
				return true;
			}
		} catch (SQLException e) {
			MySQLConnection.printExceptions(e);
		} finally {
			MySQLConnection.closeAllConnections(connection);
		}
		System.out.println(new StringBuilder().append("Card ").append(numero).append(" doesn't exist."));
		return false;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @return boolean - true if card is blocked, false if wasn't.
	 * This method check if a credit card is blocked.
	 */
	public static boolean isblocked(String numeroCarta) {
		
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM carta_prepagata WHERE numero=?";
		try {
			PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
			prstmt.setString(1, numeroCarta);
			
			ResultSet rs = prstmt.executeQuery();
			
			rs.next();
			
			boolean flag=rs.getBoolean("isBlocked");
			
			if(flag) return true;
			else return false;
		}
	    catch (SQLException e) {
		MySQLConnection.printExceptions(e);
	    }
		return false;
	}
}
