package la_compagnia_dell_homebanking.homebanking.db;

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
		super(nome, telefono, email, indirizzo, document, cap, NumberGenerator.generateRandom());
		if((this.dataDiNascita = isValidFormat(dataDiNascita)) == null) {
			super.removeValues();
			return;
		}
		this.cognome = cognome;
		this.luogoDiNascita = luogoDiNascita;
		this.getDocs().setCodice_fiscale(codice_fiscale);
		this.residenza = residenza;
		PersonaQueries.insertPersonToDb(this, getPersona_id());
		System.out.println(this.dataDiNascita);
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

	public static void main(String[] args) {
		System.out.println(NumberGenerator.generateRandom());
	}
}