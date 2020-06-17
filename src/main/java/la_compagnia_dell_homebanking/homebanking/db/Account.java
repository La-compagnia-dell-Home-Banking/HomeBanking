package la_compagnia_dell_homebanking.homebanking.db;


import java.sql.*;
import java.util.*;


public class Account {
	private ArrayList<ContoCorrente> lista_conti;
	private ArrayList<Carta_Prepagata> lista_carte;
	private static int accountID;
	private final Persona persona;

	private String password = "";


	public Account(Persona persona) throws SQLException {
		accountID = controlAccount_id() + 1;
		this.persona = persona;
		this.setPassword();
		this.insertAccountToDb();
		lista_conti = ContoCorrente.readConti(Integer.toString(accountID));
		lista_carte = Carta_Prepagata.readCarte(Integer.toString(accountID));
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
			this.persona = getPersonaFisicaFromDb(persFis);
		else
			this.persona = getPersonaGiuridicaFromDb(persGiur);
		this.setPassword();
		connection.getMyConnection().close();

	}

	private boolean insertAccountToDb() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO account VALUES (?,?,?)";

		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, Integer.toString(accountID));

		if (this.persona instanceof PersFisica) {
			prstmt.setString(2, this.persona.getPersona_id());
			prstmt.setString(3, null);
		} else {
			prstmt.setString(2, null);
			prstmt.setString(3, this.persona.getPersona_id());
		}
		Boolean status = prstmt.execute();
		connection.getMyConnection().close();
		return status;
	}

	private static int controlAccount_id() throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * from account";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		ResultSet rs = prstmt.executeQuery();
		int s, id = 0;
		while (rs.next()) { // Leggiamo i risultati
			s = Integer.parseInt(rs.getString("account_id"));
			if (id < s)
				id = s;

		}
		connection.getMyConnection().close();
		return id;

	}


//	private boolean insertAccountToDb() throws SQLException {
//		MySQLConnection connection = new MySQLConnection();
//		String query = "INSERT INTO account VALUES (?,?,?)";
//
//		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
//		prstmt.setString(1, Integer.toString(accountID));
//		prstmt.setString(2, this.persona.getPersona_id());
//		prstmt.setString(3, personaFisica.getCognome());
//		prstmt.setString(4, personaFisica.getDocs().getCodice_fiscale());
//		prstmt.setString(5, personaFisica.getdataDiNascita());
//		prstmt.setString(6, personaFisica.getLuogoDiNascita());
//		prstmt.setString(7, personaFisica.getResidenza());
//		prstmt.setString(8, personaFisica.getIndirizzo());
//		prstmt.setString(9, personaFisica.getCap());
//		prstmt.setString(10, personaFisica.getEmail());
//		prstmt.setString(11, personaFisica.getTelefono());
//		prstmt.setString(12, personaFisica.getDocs().getDocument());
//		Boolean status = prstmt.execute();
//		connection.getMyConnection().close();
//		return status;
//	}
	
	private void setPassword() {
		System.out.println("Imposta la password");
		Scanner in= new Scanner(System.in);
		password=in.next();


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
		boolean s = conto.insertCCToDb(this);
		return lista_conti.add(conto);

	}

	private Persona getPersonaFisicaFromDb(String pers) throws SQLException {

		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM persona_fisica WHERE persona_id=" + pers;
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		PersFisica persona = new PersFisica(rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"),
				rs.getString("email"), rs.getString("codice_fiscale"), rs.getString("data_nascita"),
				rs.getString("luogo_nascita"), rs.getString("indirizzo"), rs.getString("documento"),
				rs.getString("residenza"), rs.getString("cap"));
		connection.getMyConnection().close();
		return persona;
	}

	private Persona getPersonaGiuridicaFromDb(String pers) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "SELECT * FROM persona_giuridica WHERE azienda_id=" + pers;
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		PersGiuridica persona = new PersGiuridica(rs.getString("nome"), rs.getString("telefono"), rs.getString("email"),
				rs.getString("codice_fiscale"), rs.getString("ragione_sociale"),
				Long.parseLong(rs.getString("partita_iva")), rs.getString("sede_legale"), rs.getString("documento"),
				rs.getString("cap"), rs.getString("nome_rappresentante"));
		connection.getMyConnection().close();
		return persona;
	}

}
