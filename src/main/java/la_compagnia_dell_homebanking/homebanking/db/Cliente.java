package la_compagnia_dell_homebanking.homebanking.db;
public interface Cliente extends Comparable<Cliente> {

	public static String generateRandom() {
		return PersFisica.generateRandom();
	}

	public static String generateRandom(int lunghezza) {
		return PersFisica.generateRandom(lunghezza);
	}

	public String getEmail();
	public String getTelefono();
	public String getNome();
	public Documents getDocs();
}
