package la_compagnia_dell_homebanking.homebanking.db;

public class Documents {
    private String document = null;
    private String codice_fiscale = null;
    private long p_iva;

    public Documents(String document) {
        this.document = document;
    }

    public Documents(String document, String codice_fiscale) {
        this.document = document;
        this.codice_fiscale = codice_fiscale;
    }

    public Documents(String document, long p_iva) {
        this.document = document;
        this.p_iva = p_iva;
    }

    public String getDocument() {
        return document;
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public long getP_iva() {
        return p_iva;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public void setP_iva(long p_iva) {
        this.p_iva = p_iva;
    }
}
