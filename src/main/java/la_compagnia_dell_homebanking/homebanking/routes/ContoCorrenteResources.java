package la_compagnia_dell_homebanking.homebanking.routes;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Singleton
@Path("/conto_corrente")
public class ContoCorrenteResources {

    @Context
    ServletContext context;
}
