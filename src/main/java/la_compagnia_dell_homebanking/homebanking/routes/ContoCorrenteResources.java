package la_compagnia_dell_homebanking.homebanking.routes;

import la_compagnia_dell_homebanking.homebanking.ContoCorrente;
import la_compagnia_dell_homebanking.homebanking.Transazione;
import la_compagnia_dell_homebanking.homebanking.dao.ContoCorrenteDao;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;

@Singleton
@Path("/conto_corrente")
public class ContoCorrenteResources {

    @Context
    ServletContext context;

    @GET
    @Path("/{accountId}/getConti")
    @Produces(MediaType.APPLICATION_JSON)
    public String getConti(@PathParam("accountId") String accountId)  {
        Jsonb jsonb = JsonbBuilder.create();
        ArrayList<ContoCorrente> listaConti = new ArrayList<>();
        try {
            listaConti = ContoCorrenteDao.readConti(accountId);
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        }
        if(!listaConti.isEmpty()){
            return jsonb.toJson(listaConti);
        }
        return "List is emty.";
    }

    @POST
    @Path("/{accountId}/add_conto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertConto(@PathParam("accountId") String accountId) throws SQLException {

        ContoCorrente nuovo = new ContoCorrente(accountId);

        Jsonb jsonb = JsonbBuilder.create();
        if(ContoCorrenteDao.insertCCToDb(nuovo)) {
            return "Conto "+nuovo.getIBAN()+" iserito correttamente";
        }
        return "Non è stato possibile inserire il conto "+nuovo.getIBAN();
    }

    @POST
    @Path("/{ibanOut}/paga/{amount}/{ibanIn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String pagaBonifico(@PathParam("ibanOut") String ibanOut,
                               @PathParam("amount") double amount,
                               @PathParam("ibanIn") String ibanIn)  {
        try {
            if(ContoCorrenteDao.pagaConBonifico(amount, ibanOut) && ContoCorrenteDao.entrataConto(amount, ibanIn)) {
                return "Operazione è andata a buon fine.";
            }
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        }
        return "Error. Operazione è fallita.";
    }

    @GET
    @Path("/{iban}/getEstrattoConto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getEstratto(@PathParam("iban") String iban)  {
        ArrayList<Transazione> estratto = null;
        Jsonb jsonb = JsonbBuilder.create();
        try {
            estratto = ContoCorrenteDao.transazioniConto(iban);
            System.out.println(estratto);
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        }
        if(!estratto.isEmpty()) {
            return jsonb.toJson(estratto);
        }
        return "Error. La lista è vuota.";
    }

    @GET
    @Path("/{iban}/getEntrate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getEntrate(@PathParam("iban") String iban)  {
        ArrayList<Transazione> estratto = null;
        Jsonb jsonb = JsonbBuilder.create();
        try {
            estratto = ContoCorrenteDao.transazioniConto(iban);
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        }
        if(!estratto.isEmpty()) {
            estratto.removeIf(t -> !t.isAccredito());
            return jsonb.toJson(estratto);
        }
        return "Error. La lista è vuota.";
    }

    @GET
    @Path("/{iban}/getUscite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUscite(@PathParam("iban") String iban)  {
        ArrayList<Transazione> estratto = null;
        Jsonb jsonb = JsonbBuilder.create();
        try {
            estratto = ContoCorrenteDao.transazioniConto(iban);
        } catch (SQLException e) {
            MySQLConnection.printExceptions(e);
        }
        if(!estratto.isEmpty()) {
            estratto.removeIf(Transazione::isAccredito);
            return jsonb.toJson(estratto);
        }
        return "Error. La lista è vuota.";
    }
    
}
