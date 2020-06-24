package la_compagnia_dell_homebanking.homebanking.dao;

import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.PersGiuridica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.cliente.UpdatedPersonaFisica;

import java.util.List;

public interface PersonaDaoI {

    static List<Persona> getAllPerson() {
        return PersonaDao.getAllPerson();
    }
    static Persona getPersonaById(String id) {
        return PersonaDao.getPersonaById(id);
    }
    static List<PersFisica> getPersonaByCognome(String cognome) {
        return PersonaDao.getPersonaByCognome(cognome);
    }
    static List<PersFisica> getPersonByBirthDate(String data) {
        return PersonaDao.getPersonByBirthDate(data);
    }
    static PersFisica updatePersonF(UpdatedPersonaFisica personaFisica) {
        return PersonaDao.updatePersonF(personaFisica);
    }
    static PersGiuridica updatePersonG(String personId, String nome_rappresentante, String cognome_rappresentante,
                                              String sede_legale, String indirizzo, String cap, String email,
                                              String telefono) {
        return PersonaDao.updatePersonG(personId, nome_rappresentante, cognome_rappresentante, sede_legale,
                indirizzo, cap, email, telefono);
    }
    static String removePersonById(String id) {
        return PersonaDao.removePersonById(id);
    }
    static String removeCompanyById(String id) {
        return PersonaDao.removeCompanyById(id);
    }

}