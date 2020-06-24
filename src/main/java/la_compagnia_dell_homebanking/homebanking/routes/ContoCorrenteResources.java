package la_compagnia_dell_homebanking.homebanking.routes;

import java.sql.SQLException;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.ContoCorrente;
import la_compagnia_dell_homebanking.homebanking.cliente.PersFisica;
import la_compagnia_dell_homebanking.homebanking.dao.ContoCorrenteDao;

@Singleton
@Path("/conto_corrente")
public class ContoCorrenteResources {

    @Context
    ServletContext context;
    
    @PUT
    @Path("/{accountId}/{iban}/add_conto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertConto(@PathParam("accountId") String accountId, @PathParam("iban") String iban) throws SQLException {
    	
    	ContoCorrente nuovo = new ContoCorrente(accountId, iban);
    	
    	Jsonb jsonb = JsonbBuilder.create();
    	if(ContoCorrenteDao.insertCCToDb(nuovo)) {
    		return "Conto "+nuovo.getIBAN()+" iserito correttamente";
    	}
    	return "Non è stato possibile inserire il conto "+nuovo.getIBAN();
    }

    
}
