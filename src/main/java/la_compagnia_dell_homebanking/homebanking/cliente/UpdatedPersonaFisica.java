package la_compagnia_dell_homebanking.homebanking.cliente;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

/**
 * This class serves for updating person and serializing and deserializing it within JSON-B
 * @author oleskiy.OS
 */

public class UpdatedPersonaFisica {

    private String personId;
    private String indirizzo;
    private String residenza;
    private String cap;
    private String email;
    private String telefono;

    @JsonbCreator
    public UpdatedPersonaFisica(@JsonbProperty("persona_id") String personId,
                                @JsonbProperty("indirizzo") String indirizzo,
                                @JsonbProperty("residenza") String residenza,
                                @JsonbProperty("cap") String cap,
                                @JsonbProperty("email") String email,
                                @JsonbProperty("telefono") String telefono) {
        this.personId = personId;
        this.indirizzo = indirizzo;
        this.residenza = residenza;
        this.cap = cap;
        this.email = email;
        this.telefono = telefono;
    }

    public String getPersonId() {
        return personId;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getResidenza() {
        return residenza;
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
