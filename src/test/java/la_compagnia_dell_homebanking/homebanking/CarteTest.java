package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarteTest {

	/**
	 * @author oleskiy.OS
	 * This test checks if a credit card was blocked.
	 */
	@Test
	@DisplayName("Card was blocked.")
	void test_blockCard() {
		//given
		boolean actual = Carta_di_Credito.bloccareCarta(" asdf1234qwer5678");
		//expect
		boolean expect = true;
		assertEquals(expect, actual, "carta should be blocked.");
	}

	@Test
	@DisplayName("Card was created and saved.")
	void test_cartaPrepagata() throws SQLException {
		//given
		Carta_Prepagata newCard = new Carta_Prepagata("8671204139373831");
		boolean actual = newCard.equals(Carta_Prepagata.readCarte("1111111111").get(0));

		//expect
		boolean expected = true;
		assertEquals(expected,actual, "cards should be equal");
	}

	public static void main(String[] args) throws SQLException {
//
//		x.pagaConCarta(100);

		Carta_di_Credito y=new Carta_di_Credito("2839176403921784");
		y.pagaConCarta(100);

	}

}
