package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("/persona")
public class PersonResources {

    @Context
    ServletContext context;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String general() {
        List<Persona> listPersone = PersonaDao.getAllPerson();
            return listPersone.toString();
    }
}

//        CompletableFuture.supplyAsync(() -> {
//            return "Completed";
//        }).thenApply(result -> {
//            List<Persona> listPersone = PersonaDao.getAllPerson();
//            for (Persona persona : listPersone) {
//                return persona.toString();
//            }
//        });


