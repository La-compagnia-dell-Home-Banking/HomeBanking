package la_compagnia_dell_homebanking.homebanking;
import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**@author Gianmarco Polichetti
 * Servlet del generatore di token*/

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
			Connection connection = new MySQLConnection(true).getMyConnection();
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
		System.out.println(LocalDate.now()+" "+LocalTime.now()+" "+t);
		if(t>=60) t=-1;
		
		//se è passato un minuto o più dall'ultima generazione del codice token, ne genero uno nuovo e lo salvo sul database per
		//essere confrontato con quello inserito dall'utente
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
				TokenService.save_code(code, account_id, data, orario);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//altrimenti mostro quello già salvato sul database
		else {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			out.println("Codice pregenerato: "+codice_attuale);
			out.println("Tempo rimanente: "+(60-t)+" secondi");
		}


	}

	


}