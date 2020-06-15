package la_compagnia_dell_homebanking.homebanking.db;
public interface Cliente extends Comparable<Cliente> {

	public static String generateRandom() {
		return PersFisica.generateRandom();
	}

	public static String generateRandom(long minNumber, long maxNumber) {
		return PersFisica.generateRandom(minNumber, maxNumber);
	}

	public String getEmail();
	public String getTelefono();
	public String getNome();
	public Documents getDocs();
	public String getIndirizzo();
	public String getCap();
	public boolean equals(Object o);
	public int hashCode();
}
