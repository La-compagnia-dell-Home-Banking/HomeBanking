package la_compagnia_dell_homebanking.homebanking.cliente;


/**
 * @author oleskiy.OS
 * @version 1.0
 * interface for all Persons
 */
public interface Cliente {

	String getEmail();
	String getTelefono();
	String getNome();
	Documents getDocs();
	String getIndirizzo();
	String getCap();
	boolean equals(Object o);
	int hashCode();
}
