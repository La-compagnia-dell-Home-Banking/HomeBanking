package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.carta.Carta_Prepagata;
import la_compagnia_dell_homebanking.homebanking.carta.Carta_di_Credito;
import la_compagnia_dell_homebanking.homebanking.dao.CartaDiCreditoDao;
import la_compagnia_dell_homebanking.homebanking.dao.CartaPrepagataDao;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCarte {

	
	/**
     * This test try to insert a Carta_di_Credito Object to DB
     * @throws SQLException
     */
    @Test
    void test_1() throws SQLException {
    	//then 
    	Carta_di_Credito nuova=new Carta_di_Credito("1234567890",  "qwer0987mnbv4567");
    	boolean actual=CartaDiCreditoDao.inserisciCartaToDb(nuova);
    	//expected
    	
    	assertEquals(false, actual, "la carta è stata inserita nel DB");
    	
    }
	
	/**
     * This test checks a Carta_di_Credito object from DB
     * @throws SQLException
     */

    @Test
    void test_2() throws SQLException {
    	
    	//then
    	String actual=CartaDiCreditoDao.readCarta("qwer0987mnbv4567").toString();
    	//expected
    	String expected="Carta_di_Credito [accountId=1234567890, numeroCarta=2839153403921784, "
    			+ "cvv=112, conto_corrente=qwer0987mnbv4567, dataScadenza="+LocalDate.now().minusDays(10)+"]";
    	assertEquals(expected, actual, "le due carte devono essere uguali!");
    	
    }
    
    /**
     * This test checks a Carta_di_Credito object
     * @throws SQLException
     */

    @Test
    void test_3() throws SQLException {
    	
    	//then
    	boolean actual=CartaDiCreditoDao.pagaConCarta(100, "qwer0987mnbv4567");
    	//expected

    	assertEquals(true, actual, "la transazione è andata a buon fine!");
    	
    }
    
    /**
     * This test try to insert a Carta_di_Credito Object to DB
     * @throws SQLException
     */

//    @Test
//    void test_4() throws SQLException {
//    	
//    	boolean expected=false;
//    	//String iban, String nuovoNumero, String nuovoCvv
//    	//then 
//    	Carta_di_Credito actual=CartaDiCreditoDao.readCarta("qwer0987mnbv4567");
//    	//expected
//    	if(actual.isScaduta()) 
//    		expected=CartaDiCreditoDao.rinnovaCarta("qwer0987mnbv4567");
//    	else
//    		expected=actual;
//    		
//    	assertNotEquals(expected.toString(), actual.toString(), "la carta è rinnovata correttamente");
//    	
//    }
    
	/**
	 * @author oleskiy.OS
	 * This test checks if a credit card was blocked.
	 */
	@Test
	@DisplayName("Card was blocked.")
	void test_5() {
		//given
		boolean actual = CartaDiCreditoDao.bloccaCarta("asdf1234qwer5678");
		//expect
		boolean expect = true;
		assertEquals(expect, actual, "carta should be blocked.");
	}
    
	/**
     * This test try to insert a Carta_di_Credito Object to DB
     * @throws SQLException
     */
    @Test
    void test_6() throws SQLException {
    	//then
    	boolean actual = CartaDiCreditoDao.eliminaCartaFromDb("asdf1234qwer5678");
    	//expected
    	assertEquals(true, actual, "la carta è stata rimossa dal DB");
    	
    }
    

    
	/**
     * This test try to insert a Carta_di_Credito Object to DB
     * @throws SQLException
     */
    @Test
    void test_7() throws SQLException {
    	//then 
    	Carta_Prepagata nuova=new Carta_Prepagata("1111111111");
    	//expected
    	boolean actual= CartaPrepagataDao.inserisciCartaToDb(nuova);
    	assertEquals(true, actual, "la carta è stata aggiunta al DB");
    	
    }
    
	/**
     * This test try to insert a Carta_di_Credito Object to DB
     * @throws SQLException
     */
    @Test
    void test_8() throws SQLException {

    	//then 
    	String actual=CartaPrepagataDao.readCarta("7612679469284369").toString();
    	
    	//expected
    	String expected="Carta_Prepagata [accountId=1111111111, numeroCarta=7612679469284369, cvv=601, creditoResiduo=0.0, dataScadenza=2024-06-24]";
    	
    	assertEquals(expected, actual, "la carta è stata letta correttamente");
    	
    }
    
    
}
