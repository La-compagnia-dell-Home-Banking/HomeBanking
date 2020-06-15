package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PersFisica extends Persona {
	private String cognome;
	private LocalDate dataDiNascita;
	private String luogoDiNascita;
	private String residenza;
	
	public PersFisica(String nome, String cognome, String telefono, String email,
					  String codice_fiscale, String dataDiNascita, String luogoDiNascita, String indirizzo,
					  String document, String residenza, String cap) throws DateTimeParseException {
		super(nome, telefono, email, indirizzo, document, cap);
		if((this.dataDiNascita = isValidFormat(dataDiNascita)) == null) {
			super.removeValues();
			return;
		}
		this.cognome = cognome;
		this.luogoDiNascita = luogoDiNascita;
		this.getDocs().setCodice_fiscale(codice_fiscale);
		this.residenza = residenza;
		insertPersonToDb(this);
		System.out.println(this.dataDiNascita);
	}

	private static boolean insertPersonToDb(PersFisica personaFisica) {
		Boolean status = null;
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO persona_fisica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
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
			status = prstmt.execute();
		} catch (SQLException e) {
			System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
			System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
			System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
		} finally {
			try {
				connection.getMyConnection().close();
			} catch (SQLException e) {
				System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
				System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
				System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
			}

		}
		if(status != null && status) {
			StringBuilder stringBuilder = new StringBuilder().append("Success. ").append(personaFisica.toString()).
					append(" was created.");
			System.out.println(stringBuilder);
		}
		return status;
	}

	private static LocalDate isValidFormat(String date) {
		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(date);
		} catch (DateTimeParseException e) {
			System.out.println("Failed creating persona. Date should be provided in format: yyyy-mm-dd.");
			System.out.println(new StringBuilder().append("Error message:").append(e.getMessage()));
		}
		return localDate;
	}

	public static String generateRandom(long minNumber, long maxNumber) {
		long random_int = (long)(Math.random() * (maxNumber - minNumber) + minNumber);
		return Long.toString(random_int);
	}

	public static String generateRandom() {
		long min = 1000000000L;
		long max = 9999999999L;
		long random_int = (long)(Math.random() * (max-min) + min);
		return Long.toString(random_int);
	}

	public String getCognome() {
		return this.cognome;
	}

	public String getdataDiNascita() {
		return this.dataDiNascita.toString();
	}

	public String getResidenza() {
		return this.residenza;
	}

	public String getLuogoDiNascita() {
		return this.luogoDiNascita;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getNome()).append(" ").append(this.getCognome());
		return stringBuilder.toString();
	}

}
