package la_compagnia_dell_homebanking.homebanking.db;

public class ContoCorrente {
	private Account account;
	private String iban;
	private static final int ABI=45654;
	private static final int CAB=78987;
	private static int iban_final=0;
	private double saldo_disponibile;
	private double saldo_contabile;
	
	public ContoCorrente(Account account) {
	
	this.account=account;
	this.generateIBAN();
	saldo_disponibile=0.00;
	
	
	}
	
	public static String generateRandom(long minNumber, long maxNumber) {
		long random_int = (long)(Math.random() * (maxNumber - minNumber) + minNumber);
		return Long.toString(random_int);
	}

	public static String generateRandom() {
		long min = 1000000000L;
		long max = 9999999999L;
		long random_int = (long)(Math.random() * (max-min) + min);
		return Long.toString(random_int);
	}
	
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
	
	private void generateIBAN() {
		iban_final++;
		this.iban= "IT"+addZeros(generateRandom(1,99),2)+rndChar()+Integer.toString(ABI)+
				Integer.toString(CAB)+addZeros(Integer.toString(iban_final),12);
	}
	
	
	public String getIBAN() {
		return this.iban;
	}
	
}
