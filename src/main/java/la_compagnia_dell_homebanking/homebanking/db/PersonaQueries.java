package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonaQueries {

    static boolean insertPersonToDb(PersFisica personaFisica, String persona_id) {
        Boolean status = null;
        MySQLConnection connection = new MySQLConnection();
        String query = "INSERT INTO persona_fisica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
            prstmt.setString(1, persona_id);
            prstmt.setString(2, personaFisica.getNome());
            prstmt.setString(3, personaFisica.getCognome());
            prstmt.setString(4, personaFisica.getDocs().getCodice_fiscale());
            prstmt.setString(5, personaFisica.getdataDiNascita());
            prstmt.setString(6, personaFisica.getLuogoDiNascita());
            prstmt.setString(7, personaFisica.getResidenza());
            prstmt.setString(8, personaFisica.getIndirizzo());
            prstmt.setString(9, personaFisica.getCap());
            prstmt.setString(10, personaFisica.getEmail());
            prstmt.setString(11, personaFisica.getTelefono());
            prstmt.setString(12, personaFisica.getDocs().getDocument());
            status = prstmt.execute();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
                System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
                System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
            }

        }
        if(status != null && status) {
            StringBuilder stringBuilder = new StringBuilder().append("Success. ").append(personaFisica.toString()).
                    append(" was created.");
            System.out.println(stringBuilder);
        }
        return status;
    }

    static boolean insertPersonToDb(PersGiuridica persona, String persona_id) {
        Boolean status = null;
        MySQLConnection connection = new MySQLConnection();
        String query = "INSERT INTO persona_giuridica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
            prstmt.setString(1, persona_id);
            prstmt.setString(2, persona.getNome());
            prstmt.setString(3, persona.getRagione_sociale());
            prstmt.setString(4, Long.toString(persona.getDocs().getP_iva()));

            prstmt.setString(5, persona.getdataDiNascita());
            prstmt.setString(6, persona.getLuogoDiNascita());
            prstmt.setString(7, persona.getResidenza());
            prstmt.setString(8, persona.getIndirizzo());
            prstmt.setString(9, persona.getCap());
            prstmt.setString(10, persona.getEmail());
            prstmt.setString(11, persona.getTelefono());
            prstmt.setString(12, persona.getDocs().getDocument());
            status = prstmt.execute();
        } catch (SQLException e) {
            System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
            System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
            System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
                System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
                System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
            }

        }
        if(status != null && status) {
            StringBuilder stringBuilder = new StringBuilder().append("Success. ").append(persona.toString()).
                    append(" was created.");
            System.out.println(stringBuilder);
        }
        return status;
    }

//    public List<>

}
