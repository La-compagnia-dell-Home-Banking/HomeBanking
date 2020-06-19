package la_compagnia_dell_homebanking.homebanking;

import la_compagnia_dell_homebanking.homebanking.db.MySQLConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Transazione {

	private LocalDate data;
	private LocalTime orario;
	private String numero;
	private double saldo, movimento;
	private boolean accredito;
	
	
	public Transazione(LocalDate data, LocalTime orario, String numero, double saldo, double movimento,
			boolean accredito) {

		this.data = data;
		this.orario = orario;
		this.numero = numero;
		this.saldo = saldo;
		this.movimento = movimento;
		this.accredito = accredito;
	}
	

	public LocalDate getData() {
		return data;
	}

	public LocalTime getOrario() {
		return orario;
	}

	public String getNumero() {
		return numero;
	}

	public double getSaldo() {
		return saldo;
	}

	public double getMovimento() {
		return movimento;
	}

	public boolean isAccredito() {
		return accredito;
	}
	
	
	
	
}
