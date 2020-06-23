package la_compagnia_dell_homebanking.homebanking.cliente;

import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDaoI;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersGiuridica extends Persona {

	private String ragione_sociale;
	private String nomeRappresentante;
	private String cognomeRappresentante;
	private String sedeLegale;


	public PersGiuridica(String telefono, String email, String ragione_sociale, String p_iva,
						 String indirizzo, String documento, String cap, String nomeRappresentante, String cognomeRappresentante,
						 String sedeLegale, String azienda_id, Boolean isInDb) {
		super(ragione_sociale, telefono, email, indirizzo, documento, cap, azienda_id);
		this.ragione_sociale = ragione_sociale;
		this.nomeRappresentante = nomeRappresentante;
		this.cognomeRappresentante = cognomeRappresentante;
		this.sedeLegale = sedeLegale;
		this.getDocs().setP_iva(p_iva);
		if(!isInDb) {
			insertPersonToDb(this);
		}
	}

	private void insertPersonToDb(PersGiuridica persona) {
		Boolean status = null;
		MySQLConnection connection = new MySQLConnection();
		String query = "INSERT INTO persona_giuridica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
			prstmt.setString(1, persona.getPersona_id());
			prstmt.setString(2, persona.getRagione_sociale());
			prstmt.setString(3, persona.getDocs().getP_iva());
			prstmt.setString(4, persona.getNomeRappresentante());
			prstmt.setString(5, persona.getCognomeRappresentante());
			prstmt.setString(6, persona.getSedeLegale());
			prstmt.setString(7, persona.getIndirizzo());
			prstmt.setString(8, persona.getCap());
			prstmt.setString(9, persona.getEmail());
			prstmt.setString(10, persona.getTelefono());
			prstmt.setString(11, persona.getDocs().getDocument());
			status = prstmt.execute();
		} catch (SQLException e) {
			PersonaDaoI.printexceptions(e);
		} finally {
			try {
				connection.getMyConnection().close();
			} catch (SQLException e) {
				PersonaDaoI.printexceptions(e);
			}
		}
		System.out.println(new StringBuilder().append("Success. ").append(persona.toString()).
					append(" was created.").toString());
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
