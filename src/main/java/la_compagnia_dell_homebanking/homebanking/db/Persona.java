package project.homeBanking;

public class Persona implements Cliente {

	private String nome;
	private String telefono;
	private String email;
	private String indirizzo;
	private String account_number = null;
	private Documents docs = null;

	public Persona(String nome, String telefono, String email, String indirizzo, String passaporto, String codice_fiscale) {
		this.nome = nome;
		this.telefono = telefono;
		this.email = email;
		this.indirizzo = indirizzo;
		this.docs = docs;
		this.docs = new Documents(passaporto);
	}

	public String getNome() {
		return nome;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getEmail() {
		return email;
	}

	public String getACCOUNT_NUMBER() {
		return account_number;
	}

	public Documents getDocs() {
		return docs;
	}

	@Override
	public int compareTo(Cliente o) {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(this == o) return true;
		if(!(o instanceof Persona)) return false;
		Persona p = (Persona) o;
		if(p.getDocs() == null) {
			if(this.getDocs() != null)
				return false;
		}
		return this.getDocs().equals(p.getDocs());
	}

	@Override
	public int hashCode() {
		int res = 0;
		int pr = 31;
		res += this.getDocs().hashCode() * pr;
		return Math.abs(res);
	}
}
