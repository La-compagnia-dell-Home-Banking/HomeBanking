package la_compagnia_dell_homebanking.homebanking.exceptions;

public class ContoNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ContoNotFoundException() {
		super("Conto non trovato nel presente account");
	}
	

}
