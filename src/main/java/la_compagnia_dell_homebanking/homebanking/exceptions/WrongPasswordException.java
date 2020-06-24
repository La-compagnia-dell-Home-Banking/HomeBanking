package la_compagnia_dell_homebanking.homebanking.exceptions;

/**
 * This exception is thrown when somewhere is inserted a wrong password.
 * 
 * @author D'Inverno, Giuseppe Alessio
 *
 */
public class WrongPasswordException extends RuntimeException{
	
	public WrongPasswordException() {
		super("Password errata");
	}

}
