package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Carta_di_Credito implements CartaI {

	private String accountId, numeroCarta, cvv, conto_corrente;
	private LocalDate dataScadenza;
	
	
	public Carta_di_Credito(String accountId, String numeroCarta, String cvv, String conto_corrente,
			LocalDate dataScadenza) {
		this.accountId = accountId;
		this.numeroCarta = numeroCarta;
		this.cvv = cvv;
		this.conto_corrente = conto_corrente;
		this.dataScadenza = dataScadenza;
	}

	public Carta_di_Credito(String accountId) throws SQLException {
		
		Connection connection=new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_di_credito WHERE account_id='"+accountId+"'");
		rs.next();
		this.accountId=accountId;
		numeroCarta=rs.getString("numero");
		cvv=rs.getString("cvv");
		conto_corrente=rs.getString("conto");
		dataScadenza=rs.getDate("scadenza").toLocalDate();
		stmt.close();
		rs.close();
		connection.close();
		
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

				Carta_Prepagata carta = new Carta_Prepagata(rs.getString("numero"));
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
	public void pagaConCarta(double amount) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM conto_corrente WHERE iban=?");
		pstmt.setString(1, conto_corrente);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		//controllo il codice token
		if(TokenServlet.chiedi_codice(accountId)) System.out.println("Codice ok!");
		
		//calcolo il nuovo saldo
		double nuovo_credito=rs.getDouble("saldo_disponibile")-amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE conto_corrente SET saldo_disponibile=?, saldo_contabile=? WHERE iban=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setDouble(2, nuovo_credito);
		pstmt.setString(3, conto_corrente);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_conto_corrente(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?,?,?,?,?,?)");
		new Transazione(LocalDate.now(), LocalTime.now(), numeroCarta, nuovo_credito, -amount, false).creaTransazione(query);
		
		//chiudo le connessioni al DB
		pstmt.close();
		connection.close();
		
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param nuovoNumero: Il numero da applicare alla carta rinnovata
	 * @param nuovoCvv: Il cvv da applicare alla carta rinnovata
	 * @version 0.0.1
	 * Metodo per rinnovare una carta prepagata, la nuova carta cambiera il numero, la data di scadenza e il cvv
	 * ma manterrà il credito residuo*/
	public Carta_di_Credito rinnovaCarta(String nuovoNumero, String nuovoCvv) {
		
		Carta_di_Credito rinnovata=new Carta_di_Credito(this.accountId, nuovoNumero, nuovoCvv, conto_corrente, dataScadenza.plusYears(4));
		
		return rinnovata;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param nuovaCarta: Una carta appena creata 
	 * @version 0.0.1
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per inserire una carta nel DB*/
	public boolean inserisciCartaToDb(Carta_di_Credito nuovaCarta) throws SQLException {
		
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
	public boolean eliminaCartaFromDb() throws SQLException {

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
	
	

}
