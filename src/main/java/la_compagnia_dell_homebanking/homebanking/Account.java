package la_compagnia_dell_homebanking.homebanking;


import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.dao.AccountDao;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;
import la_compagnia_dell_homebanking.homebanking.dao.ContoCorrenteDao;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;
import la_compagnia_dell_homebanking.homebanking.exceptions.*;
import java.io.*;

import java.sql.*;
import java.util.*;


public class Account {
	private ArrayList<ContoCorrente> lista_conti;
	private ArrayList<Carta_Prepagata> lista_carte;
	private static int accountID;
	private final Persona persona;
	private boolean richiestaChiusura=false;
	private boolean chiuso=false;
	

	private String password = "";


	public Account(Persona persona) throws SQLException {
		accountID = AccountDao.controlAccount_id() + 1;
		this.persona = persona;
		this.setPassword();
		AccountDao.insertAccountToDb(persona, accountID);
		lista_conti = ContoCorrenteDao.readConti(Integer.toString(accountID));
		lista_carte = CartaPrepagataDao.readCarte(Integer.toString(accountID));
	}

	public Account(String account_id) throws SQLException {
		accountID = Integer.parseInt(account_id);
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * from account WHERE account_id=" + account_id;
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		String persFis = rs.getString("persona_id");
		String persGiur = rs.getString("azienda_id");
		if (persFis != null)
			this.persona = PersonaDao.getPersonaById(persFis);
		else
			this.persona = PersonaDao.getPersonaById(persGiur);
		this.setPassword();
		connection.getMyConnection().close();

	}


	private void setPassword() {
		System.out.println("Imposta la password");
		Scanner in= new Scanner(System.in);
		password=in.next();
		this.password=in.next();

	}

	public String getPassword() {
		return this.password;
	}


	public int getAccountID() {
		return this.accountID;
	}

	public boolean aggiungiConto(ContoCorrente conto) throws SQLException {
		if (lista_conti.contains(conto))
			return false;
		if (!this.equals(conto.getAccount()))
			return false;
		boolean s = ContoCorrenteDao.insertCCToDb(this, conto);
		return lista_conti.add(conto);

	}


	public boolean richiestaChiusuraConto(ContoCorrente conto) throws IOException {
		if(!lista_conti.contains(conto)) throw new ContoNotFoundException();
		
		String password;
		System.out.println("Inserisci password");
		Scanner in= new Scanner(System.in);
		password=in.next();
		in.close();
		if (!password.equals(this.password)) throw new WrongPasswordException();
		
		FileWriter file=new FileWriter("richiesta_chiusura_"+accountID);
		BufferedWriter bf=new BufferedWriter(file);
		if(persona instanceof PersFisica)
		{bf.write("Il cliente "+this.persona.getNome()+" "+((PersFisica)this.persona).getCognome()+
				" richiede la chiusura del conto corrente "+conto.getIBAN()+".");
		bf.flush();}
		else	
		{bf.write("Il cliente "+this.persona.getNome()+
				" richiede la chiusura del conto corrente "+conto.getIBAN()+".");
		bf.flush();}
		bf.close();
		return richiestaChiusura=true;
		

	}
	
	
	public boolean chiusuraConto(ContoCorrente conto) throws SQLException {
		if(!richiestaChiusura)
			throw new RuntimeException("Non è stata fatta richiesta di chiusura del conto");

			//connetto al DB
			Connection connection = new MySQLConnection().getMyConnection();
			
			//cerco la carta nel DB per eliminarla
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM account WHERE account_id=?");
			pstmt.setString(1, Integer.toString(this.accountID));
			
			//eseguo la query e salvo il risultato dell'operazione in una boolean
			boolean flag=pstmt.execute();
			
			//chiudo le connessioni al DB
			connection.close();
			pstmt.close();
			return chiuso=flag;
		}
	
	
	public boolean isChiuso() {
		return this.chiuso;
	}
	
	
	}
