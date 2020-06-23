package la_compagnia_dell_homebanking.homebanking.routes;

import java.sql.SQLException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import la_compagnia_dell_homebanking.homebanking.TokenService;
import la_compagnia_dell_homebanking.homebanking.dao.CartaDiCreditoDao;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;


@Singleton
@Path("/carta_di_credito")
public class CartaDiCreditoResources {
	
    @GET
    @Path("/{accountId}/{iban}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getCartaDiCredito(@PathParam("iban") String iban) throws SQLException {
        
    	CartaDiCreditoDao.readCarta(iban);
    	
    }
    
    @POST
    @Path("/{accountId}/{iban}/blocca_carta_credito")
    @Produces(MediaType.TEXT_PLAIN)
    public String bloccaCarta(@PathParam("iban") String iban) {
    	
    	if(CartaDiCreditoDao.bloccaCarta(iban))
    		return "La carta collegata al conto "+iban+" è stata bloccata";
    	else 
    		return "Non è stato possibile bloccare la carta";
    }
    
    
    @PUT
    @Path("/{accountId}/{iban}/paga/{amount}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pay
    (@PathParam("iban") String iban,
    @PathParam("accountId") String accountId,
    @PathParam("amount") Double amount,
    @PathParam("code") String code) {
    	String res=null;
    	try {
    		if(CartaPrepagataDao.isblocked(iban)) res="La carta è bloccata!";
			if(TokenService.chiedi_codice(accountId, code)) {
				if(CartaDiCreditoDao.pagaConCarta(amount, iban)) {
					res="Hai pagato "+amount+"€ con la carta collegata al conto "+iban;
				}
				else res="Non è stato possibile effettuare il pagamento";
			}
			else res="Codice errato, non è stato possibile effettuare il pagamento!";
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
    	return res;
    }
    
    @PUT
    @Path("/{accountId}/{iban}/rinnova")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String rinnova_carta_di_credito(@PathParam("iban") String iban) {
    	
    	String res=null;
    	
    	if(CartaDiCreditoDao.rinnovaCarta(iban))
    		res="Carta rinnovata";
    	else res="Impossibile rinnovare la carta";
    	
    	return res;
    }
    
    @DELETE
    @Path("/{accountId}/{iban}/remove_carta")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete_carta(@PathParam("iban") String iban) {
    	
    	String res=null;
    	try {
			if(CartaDiCreditoDao.eliminaCartaFromDb(iban)) {
				res="Carta eliminata correttamente";
			}
			else res="Non è stato possibile eliminare la carta";
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return res;
    }
    
    

}
