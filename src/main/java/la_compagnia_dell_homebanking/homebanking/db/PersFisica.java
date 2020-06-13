package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class PersFisica extends Persona {
	private String cognome;
	private String dataDiNascita;
	private String luogoDiNascita;
	private String residenza;
	
	public PersFisica(String nome, String cognome, String telefono, String email,
					  String codice_fiscale, String dataDiNascita, String luogoDiNascita, String indirizzo,
					  String document, String residenza, String cap) throws SQLException {
		super(nome, telefono, email, indirizzo, document, cap);
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.luogoDiNascita = luogoDiNascita;
		this.getDocs().setCodice_fiscale(codice_fiscale);
		this.residenza = residenza;
		insertPersonToDb(this);
	}

	private static boolean insertPersonToDb(PersFisica personaFisica) throws SQLException {
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO persona_fisica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
		prstmt.setString(1, generateRandom());
		prstmt.setString(2, personaFisica.getNome());
		prstmt.setString(3, personaFisica.getCognome());
		prstmt.setString(4, personaFisica.getDocs().getCodice_fiscale());
		prstmt.setString(5, personaFisica.getdataDiNascita());
		prstmt.setString(6, personaFisica.getLuogoDiNascita());
		prstmt.setString(7, personaFisica.getResidenza());
		prstmt.setString(8, personaFisica.getIndirizzo());
		prstmt.setString(9, personaFisica.getCap());
		prstmt.setString(10, personaFisica.getEmail());
		prstmt.setString(11, personaFisica.getTelefono());
		prstmt.setString(12, personaFisica.getDocs().getDocument());
		Boolean status = prstmt.execute();
		connection.getMyConnection().close();
		return status;
	}

	public static String generateRandom(int lunghezza) {
		Random random = new Random(lunghezza);
		return Integer.toString(Math.abs(random.nextInt()));
	}

	public static String generateRandom() {

		Random random = new Random(10);
		return Integer.toString(Math.abs(random.nextInt()));
	}

	public String getCognome() {
		return this.cognome;
	}

	public String getdataDiNascita() {
		return this.dataDiNascita;
	}

	public String getResidenza() {
		return this.residenza;
	}

	public String getLuogoDiNascita() {
		return this.luogoDiNascita;
	}
}
