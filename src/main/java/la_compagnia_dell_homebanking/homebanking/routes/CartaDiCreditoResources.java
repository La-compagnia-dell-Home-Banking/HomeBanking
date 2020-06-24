package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.TokenService;
import la_compagnia_dell_homebanking.homebanking.dao.CartaDiCreditoDao;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**@author Gianmarco Polichetti*/
@Singleton
@Path("/carta_di_credito")
public class CartaDiCreditoResources {
    @Context
    ServletContext context;

	/**
	 * @author Gianmarco Polichetti
	 * @param iban
	 * @version 0.0.1
	 * Show a selected credit card linked to an existing checking account*/
    @GET
    @Path("/{accountId}/{iban}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCartaDiCredito(@PathParam("iban") String iban) throws SQLException {

    	return CartaDiCreditoDao.readCarta(iban).toString();
    	
    }

	/**
	 * @author Gianmarco Polichetti
	 * @param iban
	 * @version 0.0.1
	 * Lock a card linked to an existing checking account*/
    @PUT
    @Path("/{accountId}/{iban}/blocca_carta_credito")
    @Produces(MediaType.TEXT_PLAIN)
    public String bloccaCarta(@PathParam("iban") String iban) {
    	
    	if(CartaDiCreditoDao.bloccaCarta(iban))
    		return "La carta collegata al conto "+iban+" è stata bloccata";
    	else 
    		return "Non è stato possibile bloccare la carta";
    }

	/**
	 * @author Gianmarco Polichetti
	 * @param accountId
	 * @param iban
	 * @param amount
	 * @param code
	 * @version 0.0.1
	 * Pay an amount with the credit card after insert a generated security code*/
    @POST
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
    		if(CartaDiCreditoDao.isblocked(iban)) res="La carta è bloccata!";
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

	/**
	 * @author Gianmarco Polichetti
	 * @param accountId
	 * @param iban
	 * @version 0.0.1
	 * Renew an expired credit card*/
    @PUT
    @Path("/{accountId}/{iban}/rinnova")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String rinnova_carta_di_credito(@PathParam("iban") String iban, @PathParam("accountId") String accountId) {
    	
    	String res=null;
    	
    	if(CartaDiCreditoDao.rinnovaCarta(iban, accountId))
    		res="Carta rinnovata";
    	else res="Impossibile rinnovare la carta";
    	
    	return res;
    }

	/**
	 * @author Gianmarco Polichetti
	 * @param iban
	 * @version 0.0.1
	 * Delete an expired credit card*/
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
