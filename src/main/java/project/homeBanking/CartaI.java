package project.homeBanking;

import java.time.LocalDate;

public interface CartaI {
	
	public String getAccountId();
	public String getNumeroCarta();
	public LocalDate getDataScadenza();
	public String getCvv();

}
