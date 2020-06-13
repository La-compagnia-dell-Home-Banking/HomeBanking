package la_compagnia_dell_homebanking.homebanking.db;

public class TestAccount {

	public static void main(String[] args) {
		
		Persona ale=new PersFisica("Alessio","D'Inverno","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass");
		Account account1= new Account(ale);	
		System.out.println(account1.getPassword());
		System.out.println(account1.getAccountID());
		Persona oleksii=new PersFisica("Oleksii","Suvorov","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass");
		Account account2= new Account(oleksii);	
		System.out.println(account2.getPassword());
		System.out.println(account2.getAccountID());

	}
}
