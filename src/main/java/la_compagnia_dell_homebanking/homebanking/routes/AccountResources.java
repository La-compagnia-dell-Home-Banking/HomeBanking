package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.dao.AccountDao;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;




@Singleton
@Path("/account")
public class AccountResources {
	
    @Context
    ServletContext context;
    
    @GET
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccount(@PathParam("accountId") String account_id) throws SQLException {
        Jsonb jsonb = JsonbBuilder.create();
        System.out.println(account_id);
    	return jsonb.toJson(AccountDao.getAccountFromDb(account_id));
    	
    }
    
    @POST
    @Path("/{accountId}/{personaId}/add_account_pf")
    @Produces(MediaType.APPLICATION_JSON)
    public String add_account_pf(@PathParam("accountId") String accountId, @PathParam("personaId") String personaId ) {
    	 
      	try {
    		if(AccountDao.insertAccountToDb(personaId, true, accountId)) {
    			return "L'account è stato inserito nel database";
    		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	return "L'account non è stato inserito nel database";
    }
    
    @POST
    @Path("/{accountId}/{personaId}/add_conto_pg")
    @Produces(MediaType.APPLICATION_JSON)
    public String add_account_pg(@PathParam("accountId") String accountId, @PathParam("personaId") String personaId ) {
    	 
    	try {
    		if(AccountDao.insertAccountToDb(personaId, false, accountId)) {
    			return "L'account è stato inserito nel database";
    		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	return "L'account non è stato inserito nel database";
    	
    }
    
    @POST
    @Path("/{accountId}/add_token")
    @Produces(MediaType.APPLICATION_JSON)
    public String add_token(@PathParam("accountId") String accountId ) throws SQLException {
    	 
    	if(AccountDao.addToken(accountId)) {
			return "Generatore token attivato con successo!";
		}
    	return "Non è stato possibile attivare il generatore di token, contatta un amministratore";
    	
    }
    

}
