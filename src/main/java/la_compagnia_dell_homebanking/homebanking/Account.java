package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.dao.AccountDao;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;
import la_compagnia_dell_homebanking.homebanking.dao.ContoCorrenteDao;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * 
 * @author D'Inverno, Giuseppe Alessio
 * Classe account
 * 
 */


public class Account {
	private ArrayList<ContoCorrente> lista_conti;
	private ArrayList<Carta_Prepagata> lista_carte;
	private String accountID;
	private final Persona persona;
	private boolean richiestaChiusura=false;
	private boolean chiuso=false;
//	private String password = "";
	private boolean isAdmin=false;

	
	public Account(String adminId, boolean isAdmin) {
		this.accountID=adminId;
		this.persona = PersonaDao.getPersonaById(adminId);
	}
	/**
	 * Class constructor which takes in input a Persona and initializes all the attributes
	 * @param persona	The person which owns the account
	 * @throws SQLException	The exception SQLException is launched when there are problems with the database connection
	 */

	public Account(Persona persona) throws SQLException {
		accountID = Integer.toString(AccountDao.controlAccount_id() + 1);
		this.persona = persona;
//		this.setPassword();
		if(persona instanceof PersFisica) AccountDao.insertAccountToDb(persona.getPersona_id(), true, accountID);
		else AccountDao.insertAccountToDb(persona.getPersona_id(), false, accountID);
		lista_conti = ContoCorrenteDao.readConti(accountID);
		lista_carte = CartaPrepagataDao.readCarte(accountID);
	}

	/**
	 * Class constructor which takes in input an account ID  and initializes all the attributes from the database
	 * @param account_id	The ID number of the account
	 * @throws SQLException	The exception SQLException is launched when there are problems with the database connection
	 */
	public Account(String account_id) throws SQLException {
		this.accountID = account_id;
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

//		this.setPassword();
		connection.getMyConnection().close();
	}

	/**
	 * The method set a password for the account; It is called in the main constructor
	 */
//	private void setPassword() {
//		System.out.println("Password impostata:");
//		String pwd = NumberGenerator.generateRandom(1, 999999);
//		System.out.println(pwd);
//		this.password = pwd;
//		Scanner in= new Scanner(System.in);
//		password=in.next();
//		this.password=in.next();

//	}

	/**
	 * The method returns the password of the account.
	 * @return
	 */
//	public String getPassword() {
//		return this.password;
//	}


	/**
	 * The method returns the ID of the account.
	 * @return
	 */

	/**
	 * The method returns the object Persona which owns the account.
	 * @return
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * The method adds a bank account ContoCorrent in the Account
	 * @param conto 	The bank account which we want to add in the Account
	 * @return true if the bank account is added, false otherwise;
	 * @throws SQLException
	 */
	public boolean aggiungiConto(ContoCorrente conto) throws SQLException {
		if(isAdmin) return false;
		if (lista_conti.contains(conto))
			return false;
		if (!this.equals(conto.getAccount()))
			return false;
		boolean s = ContoCorrenteDao.insertCCToDb(this, conto);
		return lista_conti.add(conto);

	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isChiuso() {
		return this.chiuso;
	}

	public boolean isRichiestaChiusura() {
		return richiestaChiusura;
	}

	public void setRichiestaChiusura(boolean richiestaChiusura) {
		this.richiestaChiusura = richiestaChiusura;
	}

	public ArrayList<Carta_Prepagata> getLista_carte() {
		return lista_carte;
	}

	public void setChiuso(boolean chiuso) {
		this.chiuso = chiuso;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public String getAccountID() {
		return this.accountID;
	}
	
	}
