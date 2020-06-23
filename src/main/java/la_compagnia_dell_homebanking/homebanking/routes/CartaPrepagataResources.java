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

@Singleton
@Path("/carta_prepagata")
public class CartaPrepagataResources {
	
    @GET
    @Path("/{accountId}/{numeroCarta}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCartaPrepagata(@PathParam("numeroCarta") String numeroCarta) throws SQLException {
        Jsonb jsonb = JsonbBuilder.create();
    	return jsonb.toJson(CartaPrepagataDao.readCarta(numeroCarta));
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
    
    
    @PUT
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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

			e.printStackTrace();
		}
    	return res;
    }
    
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
