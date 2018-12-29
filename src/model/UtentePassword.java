package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class UtentePassword {

	@Override
	public String toString() {
		return String.format("utente=%s\nuserId=%s\npassword=%s", utente, userId, password);
	}

	private int id;
	private IntegerProperty idEnte = new SimpleIntegerProperty(0);
	private String utente;
	private String email;
	private String userId;
	private String password;
	private String note;
	
	public UtentePassword() {
		this.id = 0;
		this.idEnte.set(0);
		this.utente = "";
		this.email = "";
		this.userId = "";
		this.password = "";
		this.note = "N.N.";
	}

	public UtentePassword(int id, int idEnte, String utente, String email, String userId, String password,
			String note) {
		super();
		this.id = id;
		this.idEnte.set(idEnte);
		this.utente = utente;
		this.email = email;
		this.userId = userId;
		this.password = password;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public final int getIdEnte() {
		return idEnte.get();
	}

	public final void setIdEnte(int idEnte) {
		this.idEnte.set(idEnte);
	}
	
	public IntegerProperty idEnteProperty() {
		return idEnte;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
