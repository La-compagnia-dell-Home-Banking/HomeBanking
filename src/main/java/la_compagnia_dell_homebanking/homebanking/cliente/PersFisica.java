package la_compagnia_dell_homebanking.homebanking.cliente;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.config.PropertyVisibilityStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * @author oleskiy.OS
 * @version 1.0
 */

public class PersFisica extends Persona {
	private String cognome;
	private LocalDate dataDiNascita;
	private String luogoDiNascita;
	private String residenza;

	@JsonbCreator
	public PersFisica(@JsonbProperty("nome") String nome,
					  @JsonbProperty("cognome") String cognome,
					  @JsonbProperty("telefono") String telefono,
					  @JsonbProperty("email") String email,
					  @JsonbProperty("codice_fiscale") String codice_fiscale,
					  @JsonbProperty("dataDiNascita") String dataDiNascita,
					  @JsonbProperty("luogoDiNascita") String luogoDiNascita,
					  @JsonbProperty("indirizzo") String indirizzo,
					  @JsonbProperty("document") String document,
					  @JsonbProperty("residenza") String residenza,
					  @JsonbProperty("cap") String cap,
					  @JsonbProperty("persona_id") String persona_id
					  ) throws DateTimeParseException {
		super(nome, telefono, email, indirizzo, document, cap, persona_id);
		if((this.dataDiNascita = isValidFormat(dataDiNascita)) == null) {
			super.removeValues();
			return;
		}
		this.cognome = cognome;
		this.luogoDiNascita = luogoDiNascita;
		this.getDocs().setCodice_fiscale(codice_fiscale);
		this.residenza = residenza;
	}

	/**
	 * This method controls date format for DB inserting
	 * @param date
	 * @return LocalDate
	 */
	public static LocalDate isValidFormat(String date) {
		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(date);
		} catch (DateTimeParseException e) {
			System.out.println("Error. Correct data format: yyyy-mm-dd.");
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
		stringBuilder.append("ID: ").append(this.getPersona_id()).append(". Nome: ").append(this.getNome()).append(" ").
				append(this.getCognome());
		return stringBuilder.toString();
	}

	public String toJson() {
		JsonbConfig config = new JsonbConfig().withPropertyVisibilityStrategy(new PropertyVisibilityStrategy() {
			@Override
			public boolean isVisible(Field field) {
				return false;
			}
			@Override
			public boolean isVisible(Method method) {
				return true;
			}
		});
		return JsonbBuilder.newBuilder().withConfig(config).build().toJson(this);
	}
}
