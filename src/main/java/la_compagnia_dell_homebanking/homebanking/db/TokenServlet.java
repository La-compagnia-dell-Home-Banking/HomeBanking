package la_compagnia_dell_homebanking.homebanking.db;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


@WebServlet(urlPatterns = {"/token"})
public class TokenServlet extends HttpServlet{

	private static final long serialVersionUID = 10L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String account_id=request.getParameter("account_id");
		LocalDate data_ultimo = null;
		LocalTime orario_ultimo = null;
		String codice_attuale = null;
		try {
			Connection connection = new MySQLConnection().getMyConnection();
			Statement stmt = connection.createStatement();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM token WHERE account_id=?");
			pstmt.setString(1, account_id);
			ResultSet rs = pstmt.executeQuery();

			rs.next();
			data_ultimo=rs.getDate("data_transazione").toLocalDate();
			orario_ultimo=rs.getTime("orario_transazione").toLocalTime();
			codice_attuale=rs.getString("generated_token");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		long t=-(ChronoUnit.SECONDS.between(LocalTime.now(), orario_ultimo));
		if(t>=60) t=-1;
		boolean valid=((data_ultimo.equals(LocalDate.now())&&(t>=0 && t<=60)));
		if(!valid)
		{
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			int code=(int)(((Math.random())*999999)+1);
			out.println("Codice generato: "+code);
			out.println("Tempo rimanente: "+(60-Math.abs(t))+" secondi");


			try {
				String data=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String orario=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
				save_code(code, account_id, data, orario);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			out.println("Codice pregenerato: "+codice_attuale);
			out.println("Tempo rimanente: "+(60-t)+" secondi");
		}


	}

	private static void save_code(int code, String account_id, String data, String orario) throws SQLException {

		Connection connection = new MySQLConnection().getMyConnection();
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

	public static boolean chiedi_codice(String account_id) throws SQLException {
		Connection connection = new MySQLConnection().getMyConnection();
		Statement stmt = connection.createStatement();
		Scanner in=new Scanner(System.in);
		String code_in = null;
		String gen=null;


		do {
			System.out.println("Inserisci codice token generato");
			code_in=in.nextLine();
			ResultSet rs = stmt.executeQuery("SELECT * FROM token WHERE account_id='"+account_id+"'");
			rs.next();
			LocalDate data_ultimo=rs.getDate("data_transazione").toLocalDate();
			LocalTime orario_ultimo=rs.getTime("orario_transazione").toLocalTime();
			long t=-(ChronoUnit.SECONDS.between(LocalTime.now(), orario_ultimo));
			if(t>=60) t=-1;
			boolean valid=((data_ultimo.equals(LocalDate.now())&&(t>=0 && t<=60)));
			if(!valid) {
				TokenServlet.generate(account_id);
			}
			rs = stmt.executeQuery("SELECT * FROM token WHERE account_id='"+account_id+"'");
			rs.next();
			gen=rs.getString("generated_token");

			rs.close();

		}while(!(code_in.equals(gen)));

		connection.close();
		stmt.close();


		return true;

	}

	private static void generate(String account_id) throws SQLException {

		int code=(int)(((Math.random())*999999)+1);
		String data=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String orario=LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		save_code(code, account_id, data, orario);


	}


}