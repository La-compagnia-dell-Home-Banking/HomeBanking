package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.cliente.PersGiuridica;
import la_compagnia_dell_homebanking.homebanking.cliente.Persona;
import la_compagnia_dell_homebanking.homebanking.dao.PersonaDao;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Singleton
@Path("/persona")
public class CompanyResources {

    @Context
    ServletContext context;

    @GET
    @Path("/company")
    @Produces(MediaType.TEXT_PLAIN)
    public void generalCompany() {
        List<Persona> persone = PersonaDao.getAllPerson();
        List <PersGiuridica> persGiuridica = null;
        for (Persona person : persone) {
            if(person instanceof PersGiuridica) {
                persGiuridica.add((PersGiuridica) persGiuridica);
            }
        }
    }

    @GET
    @Path("/company/{companyId}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getCompany(@PathParam("companyId") String companyId) throws ExecutionException, InterruptedException {
        PersonaDao.getPersonaById(companyId);
    }

}
