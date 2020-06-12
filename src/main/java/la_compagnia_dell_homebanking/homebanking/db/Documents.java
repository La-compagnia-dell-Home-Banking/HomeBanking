package la_compagnia_dell_homebanking.homebanking.db;

public class Documents {
    private String passaporto = null;
    private String codice_fiscale = null;
    private long p_iva;

    public Documents(String passaporto) {
        this.passaporto = passaporto;
    }

    public Documents(String passaporto, String codice_fiscale) {
        this.passaporto = passaporto;
        this.codice_fiscale = codice_fiscale;
    }

    public Documents(String passaporto, long p_iva) {
        this.passaporto = passaporto;
        this.p_iva = p_iva;
    }

    public String getPassaporto() {
        return passaporto;
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
