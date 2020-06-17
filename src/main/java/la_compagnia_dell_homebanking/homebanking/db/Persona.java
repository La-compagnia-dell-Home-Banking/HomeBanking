package la_compagnia_dell_homebanking.homebanking.db;

public class Persona implements Cliente {

	private String nome;
	private String telefono;
	private String email;
	private String indirizzo;
	private Documents docs;
	private String cap;
	private String persona_id;


	public Persona(String nome, String telefono, String email,
				   String indirizzo, String document, String cap, String persona_id) {
		this.nome = nome;
		this.telefono = telefono;
		this.email = email;
		this.indirizzo = indirizzo;
		this.docs = new Documents(document);
		this.cap = cap;
		this.persona_id = persona_id;
	}

	public String getNome() {
		return nome;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getPersona_id() {
		return persona_id;
	}

	public String getEmail() {
		return email;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public Documents getDocs() {
		return docs;
	}

	protected void removeValues() {
		this.nome = null;
		this.telefono = null;
		this.email = null;
		this.indirizzo = null;
		this.docs.setDocument(null);
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
