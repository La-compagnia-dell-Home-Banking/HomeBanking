package la_compagnia_dell_homebanking.homebanking.db;

import java.time.LocalDate;

public interface CartaI {
	
	public String getAccountId();
	public String getNumeroCarta();
	public LocalDate getDataScadenza();
	public String getCvv();

}
