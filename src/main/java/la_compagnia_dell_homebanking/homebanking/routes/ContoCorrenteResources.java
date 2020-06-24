package la_compagnia_dell_homebanking.homebanking.routes;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;

@Singleton
@Path("/conto_corrente")
public class ContoCorrenteResources {

    @Context
    ServletContext context;
    
//    @DELETE
//    @Path("/delete/{persFisica}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public String createPerson(@PathParam("persFisica") PersFisica persFisica) {
//    	
//    }
}
