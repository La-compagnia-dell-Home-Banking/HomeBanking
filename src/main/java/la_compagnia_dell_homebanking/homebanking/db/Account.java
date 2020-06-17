package la_compagnia_dell_homebanking.homebanking.db;


import java.sql.*;
import java.util.*;


public class Account {
	private ArrayList<ContoCorrente> lista_conti;
	private static int accountID;
	private final Persona persona;

	private String password = "";


	public Account(Persona persona) throws SQLException {
		accountID = controlAccount_id() + 1;
		this.persona = persona;
		this.setPassword();
		this.insertAccountToDb();
		lista_conti = ContoCorrente.readConti();
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
			this.persona = PersonaQueries.getPersonaById(persFis);
		else
			this.persona = PersonaQueries.getPersonaById(persGiur);
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

}
