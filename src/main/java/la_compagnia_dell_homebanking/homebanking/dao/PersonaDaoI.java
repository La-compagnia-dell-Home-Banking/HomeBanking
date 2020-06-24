package la_compagnia_dell_homebanking.homebanking.dao;

import la_compagnia_dell_homebanking.homebanking.cliente.*;

import java.util.List;

/**
 * @author oleskiy.OS
 * @version 1.0
 * interface for all DB operations
 */

public interface PersonaDaoI {

    /**
     * @return from DB List of All persons
     */
    static List<Persona> getAllPerson() {
        return PersonaDao.getAllPerson();
    }

    /**
     * These methods insert company or person into DB respectively
     * @param personaFisica
     */
    static void insertPersonToDb(PersFisica personaFisica) {}
    static void insertPersonToDb(PersGiuridica personaGiuridica) {}

    /**
     * @param id - person's id
     * @return from DB a Person by id number
     */
    static Persona getPersonaById(String id) {
        return PersonaDao.getPersonaById(id);
    }

    /**
     * @param cognome - person's last name
     * @return a Personfrom DB by her Last name
     */
    static List<PersFisica> getPersonaByCognome(String cognome) {
        return PersonaDao.getPersonaByCognome(cognome);
    }

    /**
     * @param data - person's birthday
     * @return a Person from DB by birthday date
     */
    static List<PersFisica> getPersonByBirthDate(String data) {
        return PersonaDao.getPersonByBirthDate(data);
    }

    /**
     * @param personaFisica - takes this special class - UpdatedPersonaFisica and updates the person in DB with it
     * @return updated person
     */
    static PersFisica updatePersonF(UpdatedPersonaFisica personaFisica) {
        return PersonaDao.updatePersonF(personaFisica);
    }

    /**
     * @param updatedPersonaGiuridica - takes this special class - UpdatedPersonaGiuridica and updates the company in DB with it
     * @return updated company
     */
    static PersGiuridica updatePersonG(UpdatedPersonaGiuridica updatedPersonaGiuridica) {
        return PersonaDao.updatePersonG(updatedPersonaGiuridica);
    }

    /**
     * @param id - takes person's id and removes person from DB
     * @return String with relative information about removing
     */
    static String removePersonById(String id) {
        return PersonaDao.removePersonById(id);
    }

    /**
     * @param id - takes company's id and removes company from DB
     * @return String with relative information about removing
     */
    static String removeCompanyById(String id) {
        return PersonaDao.removeCompanyById(id);
    }
}