package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Account {
	private static int accountID=0;
	private final Persona persona;
	private String password="";
	private ArrayList<Carta_Prepagata> carte_disponibili = new ArrayList<Carta_Prepagata>();
	
	public Account(Persona persona) throws SQLException {
		accountID++;
		this.persona=persona;
		this.setPassword();
//		this.insertAccountToDb();
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
	

	
}
