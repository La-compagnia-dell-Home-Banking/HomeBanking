package la_compagnia_dell_homebanking.homebanking.exceptions;

/**
 * 
 * @author D'Inverno, Giuseppe Alessio
 * Eccezione da lanciare quando non viene trovato 
 * un conto corrente nell'account di riferimento
 * @version 1.0
 *
 */

public class ContoNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ContoNotFoundException() {
		super("Conto non trovato nel presente account");
	}
	

}
