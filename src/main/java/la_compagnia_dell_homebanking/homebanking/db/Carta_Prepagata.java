package la_compagnia_dell_homebanking.homebanking.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Carta_Prepagata implements CartaI {
	
	private String accountId, numeroCarta, cvv;
	private double creditoResiduo;
	private LocalDate dataScadenza;
	ArrayList<Transazione> transazioni;
	
	public Carta_Prepagata(String accountId, String numeroCarta, String cvv, LocalDate dataScadenza, double creditoResiduo) {
		this.accountId = accountId;
		this.numeroCarta = numeroCarta;
		this.cvv = cvv;
		this.creditoResiduo=creditoResiduo;
		this.dataScadenza = dataScadenza;
	}

	public Carta_Prepagata(String numeroCarta) throws SQLException {
		
		Connection conn=new MySQLConnection().getMyConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_prepagata WHERE numero='"+numeroCarta+"'");
		rs.next();
		accountId=rs.getString("account_id");
		this.numeroCarta=numeroCarta;
		cvv=rs.getString("cvv");
		creditoResiduo=rs.getDouble("credito_residuo");
		dataScadenza=rs.getDate("scadenza").toLocalDate();
		stmt.close();
		rs.close();
		conn.close();
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
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM carta_prepagata WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		//controllo il codice token
		if(TokenServlet.chiedi_codice(accountId)) System.out.println("Codice ok!");
		
		//calcolo il nuovo saldo
		double nuovo_credito=rs.getDouble("credito_residuo")-amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE carta_prepagata SET credito_residuo =? WHERE numero=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setString(2, numeroCarta);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?,?,?,?,?,?)");
		new Transazione(LocalDate.now(), LocalTime.now(), numeroCarta, nuovo_credito, -amount, false).creaTransazione(query);
		
		//chiudo le connessioni al DB
		pstmt.close();
		connection.close();
		
	}
	/**
	 * @author Gianmarco Polichetti
	 * @param amount: La somma da ricaricare
	 * @version 0.0.1
	 * Metodo per ricaricare una carta prepagata, dopo aver richiesto il codice token generato, se il codice è corretto,
	 *  il saldo sulla carta viene aggiornato e viene creata una nuova transazione in entrata*/
	public void ricaricaCarta(double amount) throws SQLException {

		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//cerco la carta sul DB
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM carta_prepagata WHERE numero=?");
		pstmt.setString(1, numeroCarta);
		ResultSet rs = pstmt.executeQuery();
		rs.next();		
		
		//controllo il codice token
		if(TokenServlet.chiedi_codice(accountId)) System.out.println("Codice ok!");
		
		//calcolo il nuovo saldo
		double nuovo_credito=rs.getDouble("credito_residuo")+amount;
		
		//aggiorno la carta con il nuovo saldo
		pstmt = connection.prepareStatement("UPDATE carta_prepagata SET credito_residuo =? WHERE numero=?");
		pstmt.setDouble(1, nuovo_credito);
		pstmt.setString(2, numeroCarta);
		Boolean status = pstmt.execute();
		
		//creo la nuova transazione in uscita sul DB
		String query=("INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES(?, ?, ?, ?, ?, ?)");
		new Transazione(LocalDate.now(), LocalTime.now(), numeroCarta, nuovo_credito, +amount, true).creaTransazione(query);
		
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
	public Carta_Prepagata rinnovaCarta(String nuovoNumero, String nuovoCvv) {
		
		Carta_Prepagata rinnovata=new Carta_Prepagata(this.accountId, nuovoNumero, nuovoCvv, dataScadenza.plusYears(4), this.creditoResiduo);
		
		return rinnovata;
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @param nuovaCarta: Una carta appena creata 
	 * @version 0.0.1
	 * @return flag: Indica se l'operazione è riuscita o fallita
	 * Metodo per inserire una carta nel DB*/
	public boolean inserisciCartaToDb(Carta_Prepagata nuovaCarta) throws SQLException {
		
		//connetto al DB
		Connection connection = new MySQLConnection().getMyConnection();
		
		//inserisco i dati della carta
		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO carta_prepagata(account_id, credito_residuo, numero, scadenza, cvv) VALUES(?,?,?,?,?)");
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
	 * @return True se la data di scadenza è superata rispetto alla data attuale, false altrimenti
	 * Metodo per controllare se una carta è scaduta*/
	public boolean isScaduta() {
		
		if(dataScadenza.isBefore(LocalDate.now())) return true;
		else return false;
		
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che salva tutti i movimenti di una carta su di un ArrayList "transazioni"*/
	public void transazioniCarta() throws SQLException {
		String q="SELECT * FROM movimenti_carta_prepagata WHERE numero='"+numeroCarta+"'";
		transazioni=Transazione.estrattoContoCarta(q);
	}
	
	/**
	 * @author Gianmarco Polichetti
	 * @version 0.0.1
	 * Metodo che scrive e restituisce un file contenente l'estratto conto di una carta*/
	public File getEstrattoConto() throws SQLException {		
		transazioniCarta();
		
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
	public File getEntrate() throws SQLException {
		transazioniCarta();
		
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
	public File getUscite() throws SQLException {
		transazioniCarta();
		
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