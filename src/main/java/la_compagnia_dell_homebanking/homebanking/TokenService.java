package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TokenService {

	/**
	 * @author Gianmarco Polichetti
	 * @param code the code to be saved
	 * @param account_id the id that generates the code
	 * @param data 
	 * @param orario
	 * @version 0.0.1
	 * Save a usable token code to the database*/
	public static void save_code(int code, String account_id, String data, String orario) throws SQLException {

		Connection connection = new MySQLConnection(true).getMyConnection();
		Statement stmt = connection.createStatement();
		String query = "UPDATE token "
				+ "SET generated_token ='"+code+
				"', data_transazione='"+data+
				"', orario_transazione='"+orario+
				"' WHERE account_id="+account_id;
		stmt.execute(query);
		stmt.close();
		connection.close();

	}

	/**
	 * @author Gianmarco Polichetti
	 * @param codice_inserito the code to be compared
	 * @param account_id the id that needs to compare
	 * @version 0.0.1
	 * Asks for the current code and compares it. If more than 60 seconds have passed, the current code changes.*/
	public static boolean chiedi_codice(String account_id, String codice_inserito) throws SQLException {
		Connection connection = new MySQLConnection(true).getMyConnection();
		PreparedStatement pstmt = null;
		String gen = null;

	
		pstmt = connection.prepareStatement("SELECT * FROM token WHERE account_id=?");
		pstmt.setString(1, account_id);
		ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		LocalDate data_ultimo = rs.getDate("data_transazione").toLocalDate();
		LocalTime orario_ultimo = rs.getTime("orario_transazione").toLocalTime();
		long t = -(ChronoUnit.SECONDS.between(LocalTime.now(), orario_ultimo));
		System.out.println(t);
		System.out.println(orario_ultimo);
		System.out.println(LocalTime.now());

		if (t >= 60)
			t = -1;
		boolean valid = ((data_ultimo.equals(LocalDate.now()) && (t >= 0 && t <= 60)));
		if (!valid) {

			TokenService.generate(account_id);
		}
		rs = pstmt.executeQuery();
		rs.next();
		gen = rs.getString("generated_token");

		rs.close();
		connection.close();
		pstmt.close();
//		System.out.println(gen);
		if(!codice_inserito.equals(gen)) return false;
		return true;
	}
	
	
	public static void generate(String account_id) throws SQLException {

		int code=(int)(((Math.random())*999999)+1);
		String data=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String orario=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		save_code(code, account_id, data, orario);

	}
	
//	public static boolean chiedi_codice_OLD(String account_id, String codice_inserito) throws SQLException {
//		Connection connection = new MySQLConnection().getMyConnection();
//		PreparedStatement pstmt = null;
//		Scanner in=new Scanner(System.in);
//		String code_in = null;
//		String gen=null;
//
//		do {
//			System.out.println("Inserisci codice token generato");
//			code_in=in.nextLine(); //DECOMMENTARE DOPO I TEST!
//			pstmt = connection.prepareStatement("SELECT * FROM token WHERE account_id=?");
//			pstmt.setString(1, account_id);
//			ResultSet rs = pstmt.executeQuery();
//			rs.next();
//			LocalDate data_ultimo=rs.getDate("data_transazione").toLocalDate();
//			LocalTime orario_ultimo=rs.getTime("orario_transazione").toLocalTime();
//			long t=-(ChronoUnit.SECONDS.between(LocalTime.now(), orario_ultimo));
//			if(t>=60) t=-1;
//			boolean valid=((data_ultimo.equals(LocalDate.now())&&(t>=0 && t<=60)));
//			if(!valid) {
//				TokenService.generate(account_id);
//				//COMMENTARE DOPO I TEST!
//				//code_in=rs.getString("generated_token");
//			}
//			rs = pstmt.executeQuery();
//			rs.next();
//			gen=rs.getString("generated_token");
//
//			rs.close();
//
//		}while(!(code_in.equals(gen)));
//
//		connection.close();
//		pstmt.close();
//
//
//		return true;
//
//	}


}
