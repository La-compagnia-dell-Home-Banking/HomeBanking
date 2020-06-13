package la_compagnia_dell_homebanking.homebanking.db;
public class PersFisica extends Persona{

	private String cognome;
	private String dataDiNascita;
	private String luogoDiNascita;
	
	public PersFisica(String nome, String cognome, String telefono, String email,
					  String codice_fiscale, String dataDiNascita, String luogoDiNascita, String indirizzo,
					  String passaporto) {
		super(nome, telefono, email, codice_fiscale, indirizzo, passaporto);
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.luogoDiNascita = luogoDiNascita;
	}

	public String getCognome() {
		return cognome;
	}

	public String dataDiNascita() {
		return dataDiNascita;
	}

	public String getLuogoDiNascita() {
		return luogoDiNascita;
	}
	
	
}
