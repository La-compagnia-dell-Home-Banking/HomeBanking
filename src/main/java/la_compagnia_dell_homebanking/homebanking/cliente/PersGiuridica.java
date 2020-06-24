package la_compagnia_dell_homebanking.homebanking.cliente;



/**
 * @author oleskiy.OS
 * @version 1.0
 */
public class PersGiuridica extends Persona {

	private String ragione_sociale;
	private String nomeRappresentante;
	private String cognomeRappresentante;
	private String sedeLegale;


	public PersGiuridica(String telefono, String email, String ragione_sociale, String p_iva,
						 String indirizzo, String documento, String cap, String nomeRappresentante, String cognomeRappresentante,
						 String sedeLegale, String azienda_id) {
		super(ragione_sociale, telefono, email, indirizzo, documento, cap, azienda_id);
		this.ragione_sociale = ragione_sociale;
		this.nomeRappresentante = nomeRappresentante;
		this.cognomeRappresentante = cognomeRappresentante;
		this.sedeLegale = sedeLegale;
		this.getDocs().setP_iva(p_iva);
	}



	public String getNomeRappresentante() {
		return nomeRappresentante;
	}

	public String getSedeLegale() {
		return sedeLegale;
	}

	public String getCognomeRappresentante() {
		return cognomeRappresentante;
	}

	public String getRagione_sociale() {
		return ragione_sociale;
	}

	public String getP_iva() {
		return this.getDocs().getP_iva();
	}

	@Override
	public String toString() {
		return "ID: " + this.getPersona_id() + " Ragione Sociale: " + this.getRagione_sociale();
	}
}
