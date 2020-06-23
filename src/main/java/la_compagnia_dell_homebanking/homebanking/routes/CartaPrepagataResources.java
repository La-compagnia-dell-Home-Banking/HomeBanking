package la_compagnia_dell_homebanking.homebanking.routes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import la_compagnia_dell_homebanking.homebanking.TokenService;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;

@Singleton
@Path("/carta_prepagata")
public class CartaPrepagataResources {
	
    @GET
    @Path("/{accountId}/{numeroCarta}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getCartaPrepagata(@PathParam("numeroCarta") String numeroCarta) throws SQLException {
        
    	CartaPrepagataDao.readCarta(numeroCarta);
    	
    }
    
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
    
    @POST
    @Path("/{accountId}/{numeroCarta}/blocca_prepagata")
    @Produces(MediaType.TEXT_PLAIN)
    public String bloccaCarta(@PathParam("numeroCarta") String numeroCarta) {
    	
    	if(CartaPrepagataDao.bloccaCarta(numeroCarta))
    		return "La carta numero "+numeroCarta+" è stata bloccata";
    	else 
    		return "Non è stato possibile bloccare la carta";
    }
    
    @PUT
    @Path("/{accountId}/{numeroCarta}/paga/{amount}/{code}")
    public String pay
    (@PathParam("numeroCarta") String numeroCarta,
    @PathParam("accountId") String accountId,
    @PathParam("amount") Double amount,
    @PathParam("code") String code) {
    	String res=null;
    	try {
			if(TokenService.chiedi_codice(accountId, code)) {
				if(CartaPrepagataDao.pagaConCarta(amount, numeroCarta)) {
					res="Hai pagato "+amount+"€ con la carta "+numeroCarta;
				}
				else res="Non è stato possibile effettuare il pagamento";
			}
			else res="Codice errato, non è stato possibile effettuare il pagamento!";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
    }
    
    @PUT
    @Path("/{accountId}/{numeroCarta}/ricarica/{amount}/{code}")
    public String ricarica
    (@PathParam("numeroCarta") String numeroCarta,
    @PathParam("accountId") String accountId,
    @PathParam("amount") Double amount,
    @PathParam("code") String code) {
    	String res=null;
    	try {
			if(TokenService.chiedi_codice(accountId, code)) {
				if(CartaPrepagataDao.ricaricaCarta(amount, numeroCarta)) {
					res="Hai pagato "+amount+"€ con la carta "+numeroCarta;
				}
				else res="Non è stato possibile effettuare il pagamento";
			}
			else res="Codice errato, non è stato possibile effettuare il pagamento!";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
    }

}
