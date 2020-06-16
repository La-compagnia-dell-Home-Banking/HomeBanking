package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.*;
import java.util.*;



public class Account {
	private static int accountID; 
	private final Persona persona;
	private String password = "";

	public Account(Persona persona) throws SQLException {
		accountID=controlAccount_id()+1;
		this.persona = persona;
		this.setPassword();
		this.insertAccountToDb();
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
		int s,id=0;
		while (rs.next()) { // Leggiamo i risultati
			s=Integer.parseInt(rs.getString("account_id"));
			if (id<s)
				id=s;
			System.out.println(id);

		}
		return id;
		
	
	
	}

	private void setPassword() {
		System.out.println("Imposta la password");
		Scanner in = new Scanner(System.in);
		password = in.next();

	}

	public String getPassword() {
		return this.password;
	}

}
