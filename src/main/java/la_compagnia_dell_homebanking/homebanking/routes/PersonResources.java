package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.NumberGenerator;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.cliente.UpdatedPersonaFisica;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * API for PersonaFisica class
 * @author oleskiy.OS
 * @version 1.0
 */

@Singleton
@Path("/persona")

public class PersonResources {

    @Context
    ServletContext context;

    /**
     * Method gets all persons from DB and returns a list
     * @return List<Persona>
     */
    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String generalPersons() {
        List<Persona> persone = PersonaDao.getAllPerson();
        List <PersFisica> persFisica = new ArrayList<>();

        for (Persona person : persone) {
            if(person == null) continue;
            if(person.getClass() == PersFisica.class) {
                persFisica.add((PersFisica) person);
            }
        }
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(persFisica);
        if(!persFisica.isEmpty()) {
            return result;
        } else {
            return "List is empty.";
        }
    }

    /**
     * Method creates a person and adds information to DB
     * @return String in format JSON with a person created
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPerson(PersFisica persFisica) {
        Jsonb jsonb = JsonbBuilder.create();
        String id_generated = NumberGenerator.generateRandom();
        PersonaDao.insertPersonToDb(new PersFisica(persFisica.getNome(), persFisica.getCognome(), persFisica.getTelefono(), persFisica.getEmail(),
                persFisica.getDocs().getCodice_fiscale(), persFisica.getdataDiNascita(), persFisica.getLuogoDiNascita(),
                persFisica.getIndirizzo(),persFisica.getDocs().getDocument(), persFisica.getResidenza(),
                persFisica.getCap(), id_generated));
        return jsonb.toJson(PersonaDao.getPersonaById(id_generated));
    }


    /**
     * Method gets a persons from DB by Id
     * @return String in format JSON with a person found
     */
    @GET
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPerson(@PathParam("personId") String personId) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(PersonaDao.getPersonaById(personId));
    }

    /**
     * Method updates a person in DB
     * @return String in format JSON with a person updated
     */
    @PUT
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePerson(UpdatedPersonaFisica updatedPersonaFisica) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(PersonaDao.updatePersonF(updatedPersonaFisica));
    }

}





