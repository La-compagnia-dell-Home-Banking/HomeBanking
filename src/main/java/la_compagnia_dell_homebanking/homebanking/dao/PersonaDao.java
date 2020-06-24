package la_compagnia_dell_homebanking.homebanking.dao;

import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.PersGiuridica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.cliente.UpdatedPersonaFisica;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDao implements PersonaDaoI {


   public static void insertPersonToDb(PersFisica personaFisica) {
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
            MySQLConnection.printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                MySQLConnection.printExceptions(e);
            }
        }
        System.out.println(new StringBuilder().append("Success. Client ").append(personaFisica.toString()).
                append(" was created.").toString());
    }

    public static void insertPersonToDb(PersGiuridica persona) {
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
            prstmt.execute();
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                MySQLConnection.printExceptions(e);
            }
        }
        System.out.println(new StringBuilder().append("Success. ").append(persona.toString()).
                append(" was created.").toString());
    }

    public static List<Persona> getAllPerson() {
        List<Persona> persone = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet = null;
        try {
            resultSet = connection.getStmt().executeQuery("SELECT * from persona_fisica");
            while(resultSet.next()) {
                persone.add(populatePersonaFisica(resultSet));
            }
            resultSet = connection.getStmt().executeQuery("SELECT * from persona_giuridica");
            while(resultSet.next()) {
                persone.add(populatePersonaGiuridica(resultSet));
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            MySQLConnection.closeAllConnections(connection, resultSet);
        }
        return persone;
    }

    private static PersFisica populatePersonaFisica(ResultSet resultSet) throws SQLException {
        String person_id = resultSet.getString("persona_id");
        String nome = resultSet.getString("nome");
        String cognome = resultSet.getString("cognome");
        String cf = resultSet.getString("codice_fiscale");
        String dataDiNascita = resultSet.getString("data_nascita");
        String luogoNascita = resultSet.getString("luogo_nascita");
        String residenza = resultSet.getString("residenza");
        String indirizzo = resultSet.getString("indirizzo");
        String cap = resultSet.getString("cap");
        String email = resultSet.getString("email");
        String telefono = resultSet.getString("telefono");
        String documento = resultSet.getString("documento");
        return new PersFisica(nome,cognome,telefono,email,cf,dataDiNascita,luogoNascita,
                indirizzo,documento,residenza,cap,person_id);
    }

    private static PersGiuridica populatePersonaGiuridica(ResultSet resultSet) throws SQLException {
        String azienda_id = resultSet.getString("azienda_id");
        String ragione_sociale = resultSet.getString("ragione_sociale");
        String email = resultSet.getString("email");
        String telefono = resultSet.getString("telefono");
        String indirizzo = resultSet.getString("indirizzo");
        String cap = resultSet.getString("cap");
        String cognome_rappresentante = resultSet.getString("cognome_rappresentante");
        String nome_rappresentante = resultSet.getString("nome_rappresentante");
        String sede_legale = resultSet.getString("sede_legale");
        String partita_iva = resultSet.getString("partita_iva");
        String documento_rappresentante = resultSet.getString("documento_rappresentante");
        return new PersGiuridica(telefono,email,ragione_sociale,partita_iva,indirizzo,documento_rappresentante,
                cap,nome_rappresentante,cognome_rappresentante,sede_legale,azienda_id);
    }

    public static Persona getPersonaById(String id) {
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet =null;
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * from persona_fisica" +
                    " WHERE persona_id =?");
            prstmt.setString(1, id);
            resultSet = prstmt.executeQuery();
                if(resultSet.next()) {
                    System.out.println("ID: '" + id + "'. One person found: " + resultSet.getString("nome"));
                    return populatePersonaFisica(resultSet);
                }
                prstmt = connection.getMyConnection().prepareStatement("SELECT * from persona_giuridica" +
                    " WHERE azienda_id =?");
            prstmt.setString(1, id);
            resultSet = prstmt.executeQuery();
            if(resultSet.next()) {
                System.out.println("ID: '" + id + "'. One company found: " + resultSet.getString("ragione_sociale"));
                return populatePersonaGiuridica(resultSet);
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            MySQLConnection.closeAllConnections(connection, resultSet);
        }
        System.out.println("ID: '" + id + "' doesn't exist" );
        return null;
    }

    public static List<PersFisica> getPersonaByCognome(String cognome) {
        List<PersFisica> listCognome = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet = null;
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica WHERE " +
                    "cognome=?");
            prstmt.setString(1, cognome);
            resultSet = prstmt.executeQuery();
            while(resultSet.next()) {
                PersFisica persFisica = populatePersonaFisica(resultSet);
                System.out.println(persFisica);
                listCognome.add(persFisica);
            }
            if(listCognome == null) {
                System.out.println("Person with the last name '" + cognome + "' not found.");
            } else {
                return listCognome;
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            MySQLConnection.closeAllConnections(connection, resultSet);
        }
        return null;
    }
    public static List<PersFisica> getPersonByBirthDate(String data) {
        PersFisica.isValidFormat(data);
        List<PersFisica> listCognome = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet = null;
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica WHERE " +
                    "data_nascita = ?");
            prstmt.setString(1, data);
            resultSet = prstmt.executeQuery();

            while(resultSet.next()) {
                PersFisica persFisica = populatePersonaFisica(resultSet);
                listCognome.add(persFisica);
            }
            if(listCognome == null) {
                System.out.println("Person born on "  + data +  " not found.");
            } else {
                return listCognome;
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            MySQLConnection.closeAllConnections(connection, resultSet);
        }
        return null;
    }

    public static PersFisica updatePersonF(UpdatedPersonaFisica updatedPersonaFisica) {
        MySQLConnection connection = new MySQLConnection();
        ResultSet resultSet = null;
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("UPDATE persona_fisica" +
                    " SET indirizzo=?, residenza=?, cap=?, email=?, telefono=?  WHERE persona_id=?");
            prstmt.setString(1, updatedPersonaFisica.getIndirizzo());
            prstmt.setString(2, updatedPersonaFisica.getResidenza());
            prstmt.setString(3, updatedPersonaFisica.getCap());
            prstmt.setString(4, updatedPersonaFisica.getEmail());
            prstmt.setString(5, updatedPersonaFisica.getTelefono());
            prstmt.setString(6, updatedPersonaFisica.getPersonId());
            prstmt.execute();
            System.out.println("Person with ID: '" + updatedPersonaFisica.getPersonId() + "' was successfully updated.");
            prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica " +
                    "WHERE persona_id=?");
            prstmt.setString(1, updatedPersonaFisica.getPersonId());
            resultSet = prstmt.executeQuery();
            resultSet.next();
            return populatePersonaFisica(resultSet);

        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            MySQLConnection.closeAllConnections(connection, resultSet);
        }
        System.out.println("Errore. Person does not exist.");
        return null;
    }

    public static PersGiuridica updatePersonG(String personId, String nome_rappresentante, String cognome_rappresentante,
                                     String sede_legale, String indirizzo, String cap, String email,
                                     String telefono) {
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("UPDATE persona_giuridica" +
                    " SET nome_rappresentante=?, cognome_rappresentante=?," +
                    " sede_legale=?, indirizzo=?, cap=?, email=?, telefono=?  WHERE azienda_id=?");
            prstmt.setString(1, nome_rappresentante);
            prstmt.setString(2, cognome_rappresentante);
            prstmt.setString(3, sede_legale);
            prstmt.setString(4, indirizzo);
            prstmt.setString(5, cap);
            prstmt.setString(6, email);
            prstmt.setString(7, telefono);
            prstmt.setString(8, personId);
            prstmt.execute();
            System.out.println("Person with ID: '" + personId + "' was successfully updated.");
            prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_giuridica " +
                    "WHERE azienda_id=?");
            prstmt.setString(1, personId);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            return populatePersonaGiuridica(rs);

        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                MySQLConnection.printExceptions(e);
            }
        }
        System.out.println("Errore. Person does not exist.");
        return null;
    }

    public static String removePersonById(String id) {
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().
                    prepareStatement("DELETE from persona_fisica WHERE persona_id =?");
            prstmt.setString(1, id);
            int status = prstmt.executeUpdate();
            System.out.println(status);
            if(status == 1) {
                return "One person deleted successfully. ID: '" + id + "'.";
            }

        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                MySQLConnection.printExceptions(e);
            }
        }
        return  "ID: '" + id + "' doesn't exist";
    }

    public static String removeCompanyById(String id) {
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("DELETE from persona_giuridica" +
                    " WHERE azienda_id =?");
            prstmt.setString(1, id);
            int status = prstmt.executeUpdate();
            System.out.println(status);
            if(status == 1) {
                return "One company deleted successfully. ID: '" + id + "'.";
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                MySQLConnection.printExceptions(e);
            }
        }
        return  "ID: '" + id + "' doesn't exist";
    }
}
