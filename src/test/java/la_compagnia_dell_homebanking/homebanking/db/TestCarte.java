package la_compagnia_dell_homebanking.homebanking.db;

import java.sql.SQLException;

public class TestCarte {

	public static void main(String[] args) throws SQLException {
		Carta_Prepagata x=new Carta_Prepagata("8671204139373831");
		x.pagaConCarta(100, "8671204139373831");

	}

}
