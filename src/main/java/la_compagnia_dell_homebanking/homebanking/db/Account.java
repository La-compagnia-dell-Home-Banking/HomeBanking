package la_compagnia_dell_homebanking.homebanking.db;

import java.util.*;

public class Account {
	private static int accountID=0;
	private final Persona persona;
	private String password="";
	
	private Account(Persona persona) {
		accountID++;
		this.persona=persona;
	}
	
	public static void createAccount(Persona persona) {
		Account account=new Account(persona);
		account.setPassword();
	}
	
	private void setPassword() {
		System.out.println("Imposta la password");
		Scanner in= new Scanner(System.in);
		password=in.next();
		in.close();
	}
	
}
