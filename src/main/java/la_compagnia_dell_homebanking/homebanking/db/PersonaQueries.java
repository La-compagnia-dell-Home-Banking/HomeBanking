package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaQueries {

    static boolean insertPersonToDb(PersFisica personaFisica) {
        Boolean status = null;
        MySQLConnection connection = new MySQLConnection();
        String query = "INSERT INTO persona_fisica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
            prstmt.setString(1, personaFisica.getPersona_id());
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

    static boolean insertPersonToDb(PersGiuridica persona) {
        Boolean status = null;
        MySQLConnection connection = new MySQLConnection();
        String query = "INSERT INTO persona_giuridica VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement(query);
            prstmt.setString(1, persona.getPersona_id());
            prstmt.setString(2, persona.getNome());
            prstmt.setString(3, persona.getRagione_sociale());
            prstmt.setString(4, Long.toString(persona.getDocs().getP_iva()));
            prstmt.setString(5, persona.getNomeRappresentante());
            prstmt.setString(6, persona.getCognomeRappresentante());
            prstmt.setString(7, persona.getSedeLegale());
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


    public List<Persona> getAllPerson() throws SQLException {
        List<Persona> persone = new ArrayList<Persona>();
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet = connection.getSTMT().executeQuery("SELECT * from persona_fisica");

        while(resultSet.next()) {
            persone.add(populatePerson(resultSet));
        }
        return persone;
    }

    private Persona populatePerson(ResultSet resultSet) throws SQLException {
        String person_id = resultSet.getString("persona_id");
        String nome = resultSet.getString("nome");
        String email = resultSet.getString("email");
        String telefono = resultSet.getString("telefono");
        String indirizzo = resultSet.getString("indirizzo");
        String cap = resultSet.getString("cap");
        String cognome = resultSet.getString("cognome");
        String cf = resultSet.getString("codice_fiscale");
        String dataDiNascita = resultSet.getString("data_nascita");
        String luogoNascita = resultSet.getString("luogo_nascita");
        String residenza = resultSet.getString("residenza");
        String documento = resultSet.getString("documento");
        return new PersFisica(nome,cognome,telefono,email,cf,dataDiNascita,
                luogoNascita,indirizzo,documento,residenza,cap, person_id);
    }

}
