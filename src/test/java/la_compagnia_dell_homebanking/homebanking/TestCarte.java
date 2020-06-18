package la_compagnia_dell_homebanking.homebanking;

import java.sql.SQLException;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;

public class TestCarte {

	public static void main(String[] args) throws SQLException {
//		Carta_Prepagata x=new Carta_Prepagata("8671204139373831");
//		x.pagaConCarta(100);
		
		Carta_di_Credito y=new Carta_di_Credito("2839176403921784");
		y.pagaConCarta(100);

	}

}
