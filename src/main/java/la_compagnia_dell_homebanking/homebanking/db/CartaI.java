package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.SQLException;
import java.time.LocalDate;

public interface CartaI {
	
	public String getAccountId() throws SQLException;
	public String getNumeroCarta();
	public LocalDate getDataScadenza();
	public String getCvv();

}
