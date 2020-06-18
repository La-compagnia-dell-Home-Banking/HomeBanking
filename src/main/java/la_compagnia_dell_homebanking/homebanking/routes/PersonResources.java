package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Singleton
@Path("/persona")
public class PersonResources {

    @Context
    ServletContext context;

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String general() throws ExecutionException, InterruptedException {
        System.out.println(PersonaDao.getAllPerson().toString();


    }
}

//        return CompletableFuture.supplyAsync(() -> {
//                return "Completed";
//                }).thenApplyAsync(result -> {
//
//                }).get();




