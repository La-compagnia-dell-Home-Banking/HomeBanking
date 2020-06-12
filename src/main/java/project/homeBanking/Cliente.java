package project.homeBanking;
public interface Cliente extends Comparable<Cliente> {
	public String getACCOUNT_NUMBER();
	public String getEmail();
	public String getTelefono();
	public String getNome();
	public Documents getDocs();
}
