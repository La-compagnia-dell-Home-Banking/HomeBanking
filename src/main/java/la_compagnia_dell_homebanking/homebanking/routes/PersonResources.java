package la_compagnia_dell_homebanking.homebanking.routes;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/persona")
public class PersonResources {

    @Context
    ServletContext context;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public void general() {
//           CompletableFuture.supplyAsync(() -> {
//            return "Completed";
//        }).thenAccept(result -> {
//               return PersonaDao.getAllPerson().toString();
//        });
    }
}




