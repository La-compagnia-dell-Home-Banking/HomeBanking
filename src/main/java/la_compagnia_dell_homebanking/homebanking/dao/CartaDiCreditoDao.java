package la_compagnia_dell_homebanking.homebanking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import la_compagnia_dell_homebanking.homebanking.TokenServlet;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class CartaDiCreditoDao {


	/**
	 * @author Gianmarco Polichetti
	 * @param account_id: l'iban del conto del quale si vuole conoscere la carta di credito collegata
	 * @version 0.0.1
	 * @return carta: la carta collegata al conto
	 * Legge la carta di credito collegata al conto dal DB e la restituisce*/
	
	public static Carta_di_Credito readCarta(String iban) throws SQLException {

			Connection connection=new MySQLConnection().getMyConnection();
			Carta_di_Credito carta = null;
			String query = "SELECT * FROM carta_di_credito WHERE conto=?";
			PreparedStatement prstmt = connection.prepareStatement(query);
			prstmt.setString(1, iban);
			
			ResultSet rs = prstmt.executeQuery();

			while (rs.next()) { // Leggiamo i risultati

				carta = new Carta_di_Credito(rs.getString("conto"));

			}
			rs.close();
			prstmt.close();
			connection.close();
			return carta;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: La somma da pagare
	 * @version 0.0.1
	 * Metodo per pagare con una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in uscita*/
	public static boolean pagaConCarta(double amount, String iban) throws SQLException {
		
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, iban);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		//controllo il codice token
		if(TokenServlet.chiedi_codice(rs.getString("account_id"))) System.out.println("Codice ok!");
		
		
		double disponibile=rs.getDouble("saldo_disponibile");
		
		//controllo se il saldo disponibile è sufficiente
		if(disponibile<amount) return false;
		
		//calcolo il nuovo saldo
		double nuovo_credito=disponibile-amount;
		
		//aggiorno la carta con il nuovo saldo
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
	 * @param iban: l'iban del conto collegato alla carta
	 * @param nuovoNumero: Il numero da applicare alla carta rinnovata
	 * @param nuovoCvv: Il cvv da applicare alla carta rinnovata
	 * @version 0.0.1
	 * Metodo per rinnovare una carta di credito, la nuova carta cambiera il numero, la data di scadenza e il cvv*/
	public static Carta_di_Credito rinnovaCarta(String iban, String nuovoNumero, String nuovoCvv) {
		
		Carta_di_Credito rinnovata = null;
		Carta_di_Credito vecchia = null;

		try {
			vecchia = new Carta_di_Credito(iban);
			rinnovata=new Carta_di_Credito(vecchia.getAccountId(), nuovoNumero, nuovoCvv, vecchia.getConto_corrente(), LocalDate.now().plusYears(4));

			CartaDiCreditoDao.inserisciCartaToDb(rinnovata);
			CartaDiCreditoDao.eliminaCartaFromDb(vecchia.getNumeroCarta());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rinnovata;
	}
	
	
	/**
	 * @author Gianmarco Polichetti
	 * @param nuovaCarta: Una carta appena creata 
	 * @version 0.0.1
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per inserire una carta nel DB*/
	public static boolean inserisciCartaToDb(Carta_di_Credito nuovaCarta) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//inserisco i dati della carta
		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO carta_di_credito(conto, account_id, numero, scadenza, cvv) VALUES(?,?,?,?,?)");
		pstmt.setString(1, nuovaCarta.getConto_corrente());
		pstmt.setString(2, nuovaCarta.getAccountId());
		pstmt.setString(3, nuovaCarta.getNumeroCarta());
		pstmt.setDate(4, Date.valueOf(nuovaCarta.getDataScadenza()));
		pstmt.setString(5, nuovaCarta.getCvv());
		
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
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per eliminare una carta dal DB in base al numero della carta*/
	public static boolean eliminaCartaFromDb(String numeroCarta) throws SQLException {

		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta nel DB per eliminarla
		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM carta_di_credito WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		
		//eseguo la query e salvo il risultato dell'operazione in una boolean
		boolean flag=pstmt.execute();
		
		//chiudo le connessioni al DB
		connection.close();
		pstmt.close();
		return flag;
	}
}
