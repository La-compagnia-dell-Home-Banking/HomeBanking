package la_compagnia_dell_homebanking.homebanking.exceptions;

public class WrongPasswordException extends RuntimeException{
	
	public WrongPasswordException() {
		super("Password errata");
	}

}
