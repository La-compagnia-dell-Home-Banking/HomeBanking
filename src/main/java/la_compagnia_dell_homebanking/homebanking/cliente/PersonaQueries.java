package la_compagnia_dell_homebanking.homebanking.cliente;

import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaQueries {

    public static List<Persona> getAllPerson() {
        List<Persona> persone = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        try {
            ResultSet resultSet = connection.getStmt().executeQuery("SELECT * from persona_fisica");
            while(resultSet.next()) {
                persone.add(populatePersonaFisica(resultSet));
            }
            resultSet = connection.getStmt().executeQuery("SELECT * from persona_giuridica");
            while(resultSet.next()) {
                persone.add(populatePersonaGiuridica(resultSet));
            }
        } catch (SQLException e) {
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
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
                indirizzo,documento,residenza,cap,person_id,true);
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
                cap,nome_rappresentante,cognome_rappresentante,sede_legale,azienda_id, true);
    }


    public static Persona getPersonaById(String id) {
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * from persona_fisica" +
                    " WHERE persona_id =?");
            prstmt.setString(1, id);
            ResultSet rs = prstmt.executeQuery();
                if(rs.next()) {
                    System.out.println("ID: '" + id + "'. One person found: " + rs.getString("nome"));
                    return populatePersonaFisica(rs);
                }
                prstmt = connection.getMyConnection().prepareStatement("SELECT * from persona_giuridica" +
                    " WHERE azienda_id =?");
            prstmt.setString(1, id);
            rs = prstmt.executeQuery();
            if(rs.next()) {
                System.out.println("ID: '" + id + "'. One company found: " + rs.getString("ragione_sociale"));
                return populatePersonaGiuridica(rs);
            }
        } catch (SQLException e) {
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
        }
        System.out.println("ID: '" + id + "' doesn't exist" );
        return null;
    }

    public static List<PersFisica> getPersonaByCognome(String cognome) {
        List<PersFisica> listCognome = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica WHERE " +
                    "cognome=?");
            prstmt.setString(1, cognome);
            ResultSet rs = prstmt.executeQuery();
            while(rs.next()) {
                PersFisica persFisica = populatePersonaFisica(rs);
                System.out.println(persFisica);
                listCognome.add(persFisica);
            }
            if(listCognome == null) {
                System.out.println("Person with the last name '" + cognome + "' not found.");
            } else {
                return listCognome;
            }
        } catch (SQLException e) {
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
        }
        return null;
    }
    public static List<PersFisica> getPersonByBirthDate(String data) {
        PersFisica.isValidFormat(data);
        List<PersFisica> listCognome = new ArrayList<>();
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica WHERE " +
                    "data_nascita = ?");
            prstmt.setString(1, data);
            ResultSet rs = prstmt.executeQuery();

            while(rs.next()) {
                PersFisica persFisica = populatePersonaFisica(rs);
                listCognome.add(persFisica);
            }
            if(listCognome == null) {
                System.out.println("Person born on "  + data +  " not found.");
            } else {
                return listCognome;
            }
        } catch (SQLException e) {
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
        }
        return null;
    }

    public static PersFisica updatePersonF(String personId, String indirizzo, String residenza, String cap, String email,
                                   String telefono) {
        MySQLConnection connection = new MySQLConnection();
        try {
            PreparedStatement prstmt = connection.getMyConnection().prepareStatement("UPDATE persona_fisica" +
                    " SET indirizzo=?, residenza=?, cap=?, email=?, telefono=?  WHERE persona_id=?");
            prstmt.setString(1, indirizzo);
            prstmt.setString(2, residenza);
            prstmt.setString(3, cap);
            prstmt.setString(4, email);
            prstmt.setString(5, telefono);
            prstmt.setString(6, personId);
            prstmt.execute();
            System.out.println("Person with ID: '" + personId + "' was successfully updated.");
            prstmt = connection.getMyConnection().prepareStatement("SELECT * FROM persona_fisica " +
                    "WHERE persona_id=?");
            prstmt.setString(1, personId);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            return populatePersonaFisica(rs);

        } catch (SQLException e) {
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
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
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
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
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
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
            printExceptions(e);
        } finally {
            try {
                connection.getMyConnection().close();
            } catch (SQLException e) {
                printExceptions(e);
            }
        }
        return  "ID: '" + id + "' doesn't exist";
    }

    public static void printExceptions(SQLException e) {
        System.out.println(new StringBuilder().append("SQLException: ").append(e.getMessage()));
        System.out.println(new StringBuilder().append("SQLState: ").append(e.getSQLState()));
        System.out.println(new StringBuilder().append("VendorError: ").append(e.getErrorCode()));
    }

}
