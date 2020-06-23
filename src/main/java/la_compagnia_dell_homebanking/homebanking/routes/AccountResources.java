package la_compagnia_dell_homebanking.homebanking.routes;

import java.sql.SQLException;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import la_compagnia_dell_homebanking.homebanking.Account;
import la_compagnia_dell_homebanking.homebanking.dao.AccountDao;




@Singleton
@Path("/account")
public class AccountResources {
	
    @Context
    ServletContext context;
    
    @GET
    @Path("/{accountId}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getAccount(@PathParam("account_id") String account_id) throws SQLException {
        
    	AccountDao.getAccountFromDb(account_id);
    	
    }
    
    @POST
    @Path("/{accountId}/{personaId}/add_account_pf")
    @Produces(MediaType.APPLICATION_JSON)
    public String add_account_pf(@PathParam("accountId") String accountId, @PathParam("accountId") String personaId ) {
    	 
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
    public String add_account_pg(@PathParam("accountId") String accountId, @PathParam("accountId") String personaId ) {
    	 
    	try {
    		if(AccountDao.insertAccountToDb(personaId, false, accountId)) {
    			return "L'account è stato inserito nel database";
    		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	return "L'account non è stato inserito nel database";
    	
    }
    

}
