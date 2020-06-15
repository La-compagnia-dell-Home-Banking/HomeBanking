package la_compagnia_dell_homebanking.homebanking.db;

/**
 * @author oleskiy OS, Alessio d'Inverno
 * @version 1.0
 */

public class NumberGenerator {

    /**
     * The methods generateRandom(long minNumber, long maxNumber) takes min value and max value and generate random number between these values;
     * @param minNumber
     * @param maxNumber
     * @return - new string with random number
     */

    public static String generateRandom(long minNumber, long maxNumber) {
        int requiredLength = Long.toString(maxNumber).length();
        long random_int = (long)(Math.random() * (maxNumber - minNumber + 1) + minNumber);
        return addZeros(Long.toString(random_int), requiredLength);
    }

    /**
     * The method generateRandom() by default generates number between 1 - 9999999999
     * @return - new string with random number
     */

    public static String generateRandom() {
        int min = 1;
        long max = 9999999999L;
        long random_int = (long)(Math.random() * (max - min+ 1) + min);
        return addZeros(Long.toString(random_int), 10);
    }

    /**
     * Method adds zeros to numbers the align necessary length of digits
     * @param num
     * @param length
     * @return
     */

    private static String addZeros(String num,int length) {
        int numlength=num.length(); //1
        StringBuilder s= new StringBuilder(num);//1
        for(int i=0; i< length-numlength; i++)
            s.insert(0, "0");
        return s.toString();
    }
}
