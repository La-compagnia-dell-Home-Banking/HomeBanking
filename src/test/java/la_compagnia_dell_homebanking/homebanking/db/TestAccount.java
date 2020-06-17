package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.SQLException;

public class TestAccount {


	public static void main(String[] args) throws SQLException {

		
		Persona ale=new PersFisica("Alessio","D'Inverno","1234567891","boh@gmail.com","DNVPPPP","1993-12-27","Grosseto","via le mani dal naso","pass","ok","mah");
		Account account1= new Account(ale);	

		Persona oleksii=new PersFisica("Oleksii","Suvorov","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass", s, s);
		Account account2= new Account(oleksii);	
		System.out.println(account2.getPassword());
		System.out.println(account2.getAccountID());
		
		ContoCorrente first= new ContoCorrente(account1);
		System.out.println(first.getIBAN());
	

	}
}
