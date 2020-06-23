package la_compagnia_dell_homebanking.homebanking.exceptions;

public class CreditNotAvailableException extends RuntimeException{
	
	public CreditNotAvailableException() {
		super("Credito insufficiente");
	}

}
