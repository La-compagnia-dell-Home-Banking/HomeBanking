package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.TokenService;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;

/**@author Gianmarco Polichetti*/
@Singleton
@Path("/carta_prepagata")
public class CartaPrepagataResources {


    /**
     * @author Gianmarco Polichetti
     * @param numeroCarta
     * @version 0.0.1
     * Show a selected prepaid card linked to an existing account*/
    @GET
    @Path("/{accountId}/{numeroCarta}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCartaPrepagata(@PathParam("numeroCarta") String numeroCarta) throws SQLException {
        Jsonb jsonb = JsonbBuilder.create();
    	return jsonb.toJson(CartaPrepagataDao.readCarta(numeroCarta));
    }

    /**
     * @author Gianmarco Polichetti
     * @param accountId
     * @version 0.0.1
     * Show a list of prepaid cards linked to an existing account*/
    @GET
    @Path("/{accountId}/lista_carte_prepagate")
    @Produces(MediaType.APPLICATION_JSON)
    public String getListaCartePrepagate(@PathParam("accountId") String accountId) throws SQLException {
        ArrayList<Carta_Prepagata> list= CartaPrepagataDao.readCarte(accountId);

    	Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(list);
        if(!list.isEmpty()) {
            return result;
        } else {
            return "List is empty.";
        }
    }

    /**
     * @author Gianmarco Polichetti
     * @param numeroCarta
     * @version 0.0.1
     * Displays a list of transactions made with a prepaid card*/
    @GET
    @Path("/{accountId}/{numeroCarta}/transazioni")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTransazioniCartaPrepagata(@PathParam("numeroCarta") String numeroCarta) throws SQLException {
        ArrayList<Transazione> list= CartaPrepagataDao.transazioniCarta(numeroCarta);

    	Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(list);
        if(!list.isEmpty()) {
            return result;
        } else {
            return "List is empty.";
        }
        
    }

    /**
     * @author Gianmarco Polichetti
     * @param numeroCarta
     * @version 0.0.1
     * Lock a prepaid card linked to an existing account*/
    @PUT
    @Path("/{accountId}/{numeroCarta}/blocca_prepagata")
    @Produces(MediaType.TEXT_PLAIN)
    public String bloccaCarta(@PathParam("numeroCarta") String numeroCarta) {
    	
    	if(CartaPrepagataDao.bloccaCarta(numeroCarta))
    		return "La carta numero "+numeroCarta+" è stata bloccata";
    	else 
    		return "Non è stato possibile bloccare la carta";
    }

    /**
     * @author Gianmarco Polichetti
     * @param accountId
     * @param numeroCarta
     * @param amount
     * @param code
     * @version 0.0.1
     * Pay an amount with the prepaid card after insert a generated security code*/
    @PUT
    @Path("/{accountId}/{numeroCarta}/paga/{amount}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pay
    (@PathParam("numeroCarta") String numeroCarta,
    @PathParam("accountId") String accountId,
    @PathParam("amount") Double amount,
    @PathParam("code") String code) {
    	String res=null;
    	try {
    		if(CartaPrepagataDao.isblocked(numeroCarta)) res="La carta è bloccata!";
			if(TokenService.chiedi_codice(accountId, code)) {
				if(CartaPrepagataDao.pagaConCarta(amount, numeroCarta)) {
					res="Hai pagato "+amount+"€ con la carta "+numeroCarta;
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
     * @param numeroCarta
     * @param amount
     * @param code
     * @version 0.0.1
     * Recharge an amount to the prepaid card after insert a generated security code*/
    @PUT
    @Path("/{accountId}/{numeroCarta}/ricarica/{amount}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String ricarica
    (@PathParam("numeroCarta") String numeroCarta,
    @PathParam("accountId") String accountId,
    @PathParam("amount") Double amount,
    @PathParam("code") String code) {
    	String res=null;
    	try {
    		if(CartaPrepagataDao.isblocked(numeroCarta)) res="La carta è bloccata!";
			if(TokenService.chiedi_codice(accountId, code)) {
				if(CartaPrepagataDao.ricaricaCarta(amount, numeroCarta)) {
					res="Hai ricaricato "+amount+"€ sulla carta "+numeroCarta;
				}
				else res="Non è stato possibile effettuare la ricarica";
			}
			else res="Codice errato, non è stato possibile effettuare la ricarica!";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
    }

    /**
     * @author Gianmarco Polichetti
     * @param accountId
     * @version 0.0.1
     * Add a new prepaid card to an existing account*/
    @POST
    @Path("/{accountId}/add_carta")
    @Produces(MediaType.APPLICATION_JSON)
    public String add_carta_prepagata(@PathParam("accountId") String accountId) {
    	
    	Carta_Prepagata nuova= new Carta_Prepagata(accountId);
    	try {
            if (CartaPrepagataDao.inserisciCartaToDb(nuova)) {
                return "La carta " + nuova.getNumeroCarta() + " è stata aggiunta.";
            }

		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return "Errore. La carta " + nuova.getNumeroCarta() + " non è stata aggiunta.";
    }

    /**
     * @author Gianmarco Polichetti
     * @param accountId
     * @param numeroCarta
     * @version 0.0.1
     * Renew an expired prapaid card*/
    @PUT
    @Path("/{accountId}/{numeroCarta}/rinnova")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String rinnova_carta_prepagata(@PathParam("numeroCarta") String numeroCarta, @PathParam("accountId") String accountId) {

        String res=null;

        if(CartaPrepagataDao.rinnovaCarta(numeroCarta))
            res="Carta rinnovata";
        else res="Impossibile rinnovare la carta";

        return res;
    }


    /**
     * @author Gianmarco Polichetti
     * @param numeroCarta
     * @version 0.0.1
     * Delete an expired prepaid card*/
    @DELETE
    @Path("/{accountId}/{numeroCarta}/remove_carta")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete_carta(@PathParam("numeroCarta") String numeroCarta) {

        try {
            if (CartaPrepagataDao.eliminaCartaFromDb(numeroCarta)) {
                return "La carta " + numeroCarta + " è stata eleminata.";
            }

        } catch (SQLException e) {
			e.printStackTrace();
		}
        return "Errere. La carta " + numeroCarta + " non è stata eliminata.";
    }

}
