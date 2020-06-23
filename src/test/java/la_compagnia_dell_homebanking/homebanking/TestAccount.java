
package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class TestAccount {


	/**
	 * @author oleskiy.OS
	 * @param num
	 * @param length
	 * @return
	 */
	@Test
	@DisplayName("Shoould add 0 to complete quantity of digits")


	private static String addZeros(String num,int length) {
		int numlength=num.length();
		String s=num;
		for(int i=0; i<length-numlength; i++)
			s='0'+s;
		return s;

	}

	private static char rndChar () {
		int rnd = (int) (Math.random() * 52); // or use Random or whatever
		return (char) ('A' + rnd % 26);
	}

	public static void main(String[] args) throws SQLException {
		Persona ale=new PersFisica("Alessio","D'Inverno","1234567891","boh@gmail.com","DNVPPPP","1993-12-27","Grosseto","via le mani dal naso","pass","ok","mah", NumberGenerator.generateRandom(),true);
		Account account1= new Account(ale);	

		Persona oleksii=new PersFisica("Oleksii","Suvorov","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass", "ok", "mah", NumberGenerator.generateRandom(),true);
		Account account2= new Account(oleksii);	
		System.out.println(account2.getPassword());
		System.out.println(account2.getAccountID());
		
		ContoCorrente first= new ContoCorrente(account1);
		System.out.println(first.getIBAN());
	}
}
