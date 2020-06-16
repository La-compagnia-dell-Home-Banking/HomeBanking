//package la_compagnia_dell_homebanking.homebanking.db;
//
//import java.sql.SQLException;
//
//public class TestAccount {
//
//	private static String addZeros(String num,int length) {
//		int numlength=num.length();
//		String s=num;
//		for(int i=0; i<length-numlength; i++)
//			s='0'+s;
//		return s;
//
//	}
//
//	private static char rndChar () {
//	    int rnd = (int) (Math.random() * 52); // or use Random or whatever
//	    return (char) ('A' + rnd % 26);
//	}
//	public static void main(String[] args) throws SQLException {
//
//		String s="2";
//		System.out.println(addZeros(s,6));
//		System.out.println(rndChar());
//
//		Persona ale=new PersFisica("Alessio","D'Inverno","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass","ok","mah");
//		Account account1= new Account(ale);
//		System.out.println(account1.getPassword());
//		System.out.println(account1.getAccountID());
////		Persona oleksii=new PersFisica("Oleksii","Suvorov","1234567891","boh@gmail.com","ok","27/12/1238","Grosseto","via le mani dal naso","pass", s, s);
////		Account account2= new Account(oleksii);
////		System.out.println(account2.getPassword());
////		System.out.println(account2.getAccountID());
//
//		ContoCorrente first= new ContoCorrente(account1);
//		System.out.println(first.getIBAN());
//
//
//	}
//}
