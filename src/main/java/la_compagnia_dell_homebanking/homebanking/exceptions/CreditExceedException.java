package la_compagnia_dell_homebanking.homebanking.exceptions;

public class CreditExceedException extends RuntimeException{
	
	public CreditExceedException() {
		super("Non puoi caricare una carta ricaricabile con più di 5000€");
	}

}
