package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		boolean actual = Carta_di_Credito.bloccareCarta("asdf1234qwer5678");
		//expect
		boolean expect = true;
		assertEquals(expect, actual, "carta should be blocked.");
	}
}
