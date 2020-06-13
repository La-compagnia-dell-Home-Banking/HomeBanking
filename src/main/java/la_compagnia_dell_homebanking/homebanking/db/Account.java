package la_compagnia_dell_homebanking.homebanking.db;

import java.util.*;

public class Account {
	private static int accountID=0;
	private final Persona persona;
	private String password="";
	
	public Account(Persona persona) {
		accountID++;
		this.persona=persona;
		this.setPassword();
	}
	

	
	private void setPassword() {
		System.out.println("Imposta la password");
		Scanner in= new Scanner(System.in);
		password=in.next();
	
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public int getAccountID() {
		return accountID;
	}
	
}
