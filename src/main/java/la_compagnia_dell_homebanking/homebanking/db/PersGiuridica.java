package la_compagnia_dell_homebanking.homebanking.db;

public class PersGiuridica extends Persona {

	private String ragione_sociale;
	private long p_iva;
	private String nomeRappresentante;


	public PersGiuridica(String nome, String telefono, String email, String codice_fiscale, String ragione_sociale, long p_iva,
						 String sede, String document, String cap, String nomeRappresentante) {
		super(nome, telefono, email, sede, document, cap, NumberGenerator.generateRandom());
		this.p_iva = p_iva;
		this.ragione_sociale = ragione_sociale;
		this.nomeRappresentante = nomeRappresentante;
		this.getDocs().setP_iva(p_iva);
	}

	public String getNomeRappresentante() {
		return nomeRappresentante;
	}

	public String getRagione_sociale() {
		return ragione_sociale;
	}

	public long getP_iva() {
		return p_iva;
	}
}
