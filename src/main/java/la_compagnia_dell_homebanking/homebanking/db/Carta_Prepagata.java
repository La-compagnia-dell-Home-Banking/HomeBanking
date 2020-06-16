package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.Connection;
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

	public void pagaConCarta(double amount, String numero_carta) throws SQLException {
		
		Connection connection = new MySQLConnection().getMyConnection();
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_prepagata WHERE numero='"+numeroCarta+"'");
		rs.next();
		
		double nuovo_credito=rs.getDouble("credito_residuo")-amount;
		String DT=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String TM=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		String query = "UPDATE carta_prepagata SET credito_residuo ="+nuovo_credito+" WHERE numero="+numero_carta;
		stmt.execute(query);
		query="INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES"
				+ "('"+DT+"','"+TM+"','"+numero_carta+"','"+nuovo_credito+"','"+-amount+"','"+0+"')";
		Transazione.creaTransazione(query);
		stmt.close();
		connection.close();

	}
	
	public void ricaricaCarta(double amount, String numero_carta) throws SQLException {
		
		Connection connection = new MySQLConnection().getMyConnection();

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM carta_prepagata WHERE numero='"+numeroCarta+"'");
		rs.next();
		
		double nuovo_credito=rs.getDouble("credito_residuo")+amount;
		String DT=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String TM=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		String query = "UPDATE carta_prepagata SET credito_residuo ="+nuovo_credito+" WHERE numero="+numero_carta;
		stmt.execute(query);
		query="INSERT INTO movimenti_carta_prepagata(data_transazione, orario_transazione, numero, nuovo_saldo, somma, is_accredito) VALUES"
				+ "('"+DT+"','"+TM+"','"+numero_carta+"','"+nuovo_credito+"','"+amount+"','"+1+"')";
		Transazione.creaTransazione(query);
		stmt.close();
		connection.close();
		
	}
	
	
	public void rinnovaCarta(String numero_carta) {
		
//		if(ChronoUnit.MONTHS.between(dataScadenza, temporal2Exclusive))
		
	}

	@Override
	public String toString() {
		return "Carta_Prepagata [accountId=" + accountId + ", numeroCarta=" + numeroCarta + ", cvv=" + cvv
				+ ", creditoResiduo=" + creditoResiduo + ", dataScadenza=" + dataScadenza + "]";
	}
	
	public void transazioniCarta() throws SQLException {
		String q="SELECT * FROM movimenti_carta_prepagata WHERE numero='"+numeroCarta+"'";
		transazioni=Transazione.estrattoContoCarta(q);
	}
	public void getEstrattoConto() throws SQLException {		
		transazioniCarta();
		System.out.println("Estratto conto carta numero: "+numeroCarta);
		for(Transazione t:transazioni) {
			System.out.println(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo());
		}
				
	}
	
	public void getEntrate() throws SQLException {
		transazioniCarta();
		System.out.println("Entrate carta numero: "+numeroCarta);
		for(Transazione t:transazioni) {
			if(t.isAccredito())	System.out.println(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo());
		}
	}
	
	public void getUscite() throws SQLException {
		transazioniCarta();
		System.out.println("Uscite carta numero: "+numeroCarta);
		for(Transazione t:transazioni) {
			if(!t.isAccredito())	System.out.println(t.getData()+", "+t.getOrario()+"   "+t.getMovimento()+"   "+t.getSaldo());
		}
	}
	

}
