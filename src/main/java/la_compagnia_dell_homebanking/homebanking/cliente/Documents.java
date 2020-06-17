package la_compagnia_dell_homebanking.homebanking.cliente;

public class Documents {
    private String document;
    private String codice_fiscale;
    private String p_iva;

    public Documents(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public String getP_iva() {
        return p_iva;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public void setP_iva(String p_iva) {
        this.p_iva = p_iva;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
