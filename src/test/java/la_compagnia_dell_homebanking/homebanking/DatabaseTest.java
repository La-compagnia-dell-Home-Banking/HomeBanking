package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.PersGiuridica;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * DatabaseTest class - tests the MySQLConnection class
 * @author oleskiy OS
 * @version 1.0
 */

public class DatabaseTest {

    /**
     * This test checks database name.
     * @throws SQLException
     */

    @Test
    void test_getDatabaseName() throws SQLException {
        MySQLConnection connection = new MySQLConnection();
        ResultSet rs = connection.getStmt().executeQuery("SHOW DATABASES;");
        //then
        String actual = null;
        rs.next();
            actual = rs.getString(1);
        //expect
        String expected = "home_banking";


        assertEquals(expected, actual, "deve essere uguale al nome del nostro db");
        connection.getStmt().close();
    }

    /**
     * The method checks database connection status by using isClosed() method
     * @throws SQLException
     * isClosed() returns false - if connection is opened
     */
    @Test
    @DisplayName("Connection status: isClose()")
    void test_getConnection() throws SQLException {
        // given
        MySQLConnection connection = new MySQLConnection();

        //then
        Boolean actual = connection.getStmt().isClosed();
        //expect
        Boolean expected = false;
        assertEquals(expected, actual, "la connessione deve essere aperta");
        connection.getStmt().close();
    }

    /**
     * This method tests if the person created is equals to person inserted to DB.
     * Person class takes next params: name, lastname, phone, email, fiscal_code, birth_date, birth_place,
     * address, document, residence, postal code, person_id and bolean to indicate if the person has been inserted
     * into the DB previously. Also it tests getPersonaById() method.
     */
    @Test
    @DisplayName("Person was created.")
    void test_insertPersonaFisicaToDb() {
        //given
        PersFisica persFisica = new PersFisica("Alex", "X", "3487218089", "@",
                "SCSVSSVSD", "0950-01-02", "Visantia", "via",
                "non c'è", "Costantinopoli", "?", "5555555555", false);
        //then
        boolean isTheSamePerson = PersonaDao.getPersonaById("5555555555").equals(persFisica);
        //expect
        boolean expected = true;
        assertEquals(expected, isTheSamePerson, "person should be the same");
    }

    /**
     * This method tests if the created company is equals to company inserted to DB.
     * PersGiuridica class takes next params: phone, email, ragine_sociale, address, document,
     * postal code, name and lastname of representative agent, official address, company_id,
     * and bolean to indicate if the company has been already inserted into the DB previously.
     * Also it tests getPersonaById() method.
     */
    @Test
    @DisplayName("Company was created.")
    void test_insertPersonaGiuridicaToDb() {
        //given
        PersGiuridica persGiuridica = new PersGiuridica("1234567", "azienda@mail.com", "Pippo spa", "911",
                "via del Pippo", "dsf23124", "02345", "Edgar",
                "Alan Po", "USA", "1111111111", false);
        //then
        boolean isTheSameCompany = PersonaDao.getPersonaById("1111111111").equals(persGiuridica);
        //expect
        boolean expected = true;
        assertEquals(expected, isTheSameCompany, "company should be the same.");
    }

    /**
     * This test makes sure that we can get an ArrayList of Persons with the same lastname from DB.
     */
    @Test
    @DisplayName("Person with this lastname exist in DB.")
    void test_igetLastName() {
        //given
        String cognome = "cognome";
        PersFisica persFisica = new PersFisica("Mister", cognome, "3487218089", "@",
                "DFRD3453SD234", "0950-01-02", "Visantia", "via",
                "12345632", "Costantinopoli", "?", "1111111111", false);
        PersFisica persFisica2 = new PersFisica("Mister2", cognome, "2324252323", "@2",
                "ACASDASD1241", "0923-02-01", "Australia", "via a",
                "asd34512", "Australia", "444356", "2222222222", false);
        //then
        List<PersFisica> persons = PersonaDao.getPersonaByCognome(cognome);
        boolean isTheSame = false;
        for (PersFisica persona : persons) {
            if(!persona.getCognome().equals(cognome)) {
                isTheSame = false;
                break;
            }
            isTheSame = true;
        }
        //expect
        boolean expected = true;
        assertEquals(expected, isTheSame, "person should be the same");
    }

    /**
     * This test checks person's updated information from the DB.
     */
    @Test
    @DisplayName("Person was updated to DB.")
    void test_checkUpdated() {
        //given
        PersFisica persFisica = new PersFisica("MisterX", "Bz", "43521234", "@mail",
                "FHSDLA!@#!", "1991-01-02", "Torino", "via T",
                "FG31242", "Italia", "12019", "3333333333", false);
        //then
        PersFisica modificatedPerson = PersonaDao.updatePersonF("3333333333", "via Nuova",
                "Nuova", "nuovo", "nuova_email", "nuovo_t");

        boolean actual = modificatedPerson.getResidenza().equals("Nuova") &&
                modificatedPerson.getTelefono().equals("nuovo_t");
        //expect
        boolean expected = true;
        assertEquals(expected, actual, "data should be updated");
    }

    /**
     * This test checks either removing of a company from the DB was done properly or not.
     */
    @Test
    @DisplayName("Company was deleted from DB.")
    void test_checkDeleted() {
        //given
        PersGiuridica persGiuridica = new PersGiuridica("321413124", "azienda@mail.com", "Pippo&Co", "141235",
                "via del Pippo", "FRE43453", "02345", "Edgar",
                "Alan Po", "USA", "4444444444", false);

        String actual = PersonaDao.removeCompanyById("4444444444");
        //expect
        String expected = "One company deleted successfully. ID: '4444444444'.";
        assertEquals(expected, actual, "person should be the same");
    }

    /**
     * This test tries to remove a company which does not exists in DB, so it gets an expected message.
     */
    @Test
    @DisplayName("Company doesn't exist in DB.")
    void test_checkExist() {
        //given
        PersGiuridica persGiuridica = new PersGiuridica("321413124", "azienda@mail.com", "Pippo&Partners", "131415113",
                "via del Pippo", "ASDFASD123", "02345", "Edgar",
                "Alan Po", "USA", "2222222222", false);

        String actual = PersonaDao.removeCompanyById("1231325231");
        //expect
        String expected = "ID: '1231325231' doesn't exist";
        assertEquals(expected, actual, "person should be the same");
    }
}
