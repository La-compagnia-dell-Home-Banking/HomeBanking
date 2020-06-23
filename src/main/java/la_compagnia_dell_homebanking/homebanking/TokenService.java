package la_compagnia_dell_homebanking.homebanking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

public class TokenService {

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

	public static boolean chiedi_codice(String account_id, String codice_inserito) throws SQLException {
		Connection connection = new MySQLConnection(true).getMyConnection();
		PreparedStatement pstmt = null;
		String code_in =  codice_inserito;
		String gen = null;

	
		pstmt = connection.prepareStatement("SELECT * FROM token WHERE account_id=?");
		pstmt.setString(1, account_id);
		ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		LocalDate data_ultimo = rs.getDate("data_transazione").toLocalDate();
		LocalTime orario_ultimo = rs.getTime("orario_transazione").toLocalTime();
		long t = -(ChronoUnit.SECONDS.between(LocalTime.now(), orario_ultimo));
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

		if(!code_in.equals(gen)) return false;
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
