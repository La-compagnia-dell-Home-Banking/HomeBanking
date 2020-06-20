package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.NumberGenerator;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
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
import java.util.concurrent.ExecutionException;

@Singleton
@Path("/persona")

public class PersonResources {

    @Context
    ServletContext context;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
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

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersFisica createPerson(PersFisica persFisica) throws ExecutionException, InterruptedException {
        return new PersFisica(persFisica.getNome(), persFisica.getCognome(), persFisica.getTelefono(), persFisica.getEmail(),
                persFisica.getDocs().getCodice_fiscale(), persFisica.getdataDiNascita(), persFisica.getLuogoDiNascita(),
                persFisica.getIndirizzo(),persFisica.getDocs().getDocument(), persFisica.getResidenza(),
                persFisica.getCap(), NumberGenerator.generateRandom(), false);
    }

    @GET
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPerson(@PathParam("personId") String personId) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(PersonaDao.getPersonaById(personId));
    }

    @PUT
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public void updatePerson(@PathParam("personId") String personId) {
//        PersonaDao.updatePersonG(personId);
    }

    @GET
    @Path("/person/{personId}/account")
    @Produces(MediaType.TEXT_PLAIN)
    public void getAccount(@PathParam("companyId") String companyId) throws ExecutionException, InterruptedException {

    }

}

//        return CompletableFuture.supplyAsync(() -> {
//                return "Completed";
//                }).thenApplyAsync(result -> {
//
//                }).get();




