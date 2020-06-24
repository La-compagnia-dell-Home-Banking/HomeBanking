package la_compagnia_dell_homebanking.homebanking.cliente;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

/**
 * This class serves for updating company and serializing and deserializing it within JSON-B
 * @author oleskiy.OS
 */

public class UpdatedPersonaGiuridica {

    private String personId;
    private String nome_rappresentante;
    private String cognome_rappresentante;
    private String indirizzo;
    private String sede_legale;
    private String cap;
    private String email;
    private String telefono;

    @JsonbCreator
    public UpdatedPersonaGiuridica(@JsonbProperty("persona_id") String personId,
                                @JsonbProperty("indirizzo") String indirizzo,
                                @JsonbProperty("cap") String cap,
                                @JsonbProperty("email") String email,
                                @JsonbProperty("telefono") String telefono,
                                   @JsonbProperty("nome_rappresentante") String nome_rappresentante,
                                   @JsonbProperty("cognome_rappresentante") String cognome_rappresentante,
                                   @JsonbProperty("sede_legale") String sede_legale) {
        this.personId = personId;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.email = email;
        this.telefono = telefono;
        this.nome_rappresentante = nome_rappresentante;
        this.cognome_rappresentante = cognome_rappresentante;
        this.sede_legale = sede_legale;
    }

    public String getPersonId() {
        return personId;
    }

    public String getNome_rappresentante() {
        return nome_rappresentante;
    }

    public String getCognome_rappresentante() {
        return cognome_rappresentante;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getSede_legale() {
        return sede_legale;
    }

    public String getCap() {
        return cap;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }
}