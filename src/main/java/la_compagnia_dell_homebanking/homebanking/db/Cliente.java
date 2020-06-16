package la_compagnia_dell_homebanking.homebanking.db;
public interface Cliente extends Comparable<Cliente> {



	public String getEmail();
	public String getTelefono();
	public String getNome();
	public Documents getDocs();
	public String getIndirizzo();
	public String getCap();
	public boolean equals(Object o);
	public int hashCode();
}
