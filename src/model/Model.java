package model;

import java.util.List;

import db.PasswordDAO;

public class Model {

	private static Model model = null;

	PasswordDAO dao;
	private Ente enteSelezionato;
	private UtentePassword utenteSelezionato;

	private Model() {
		this.dao = new PasswordDAO();
		this.enteSelezionato = new Ente();
		this.utenteSelezionato = new UtentePassword();

	}

	public static synchronized Model getModel() {

		if (model == null) {
			model = new Model();
		}
		return model;

	}
	
	//getters and setters of utenteSelezionato and enteSelezionato
	public UtentePassword getUtenteSelezionato() {
		return utenteSelezionato;
	}

	public void setUtenteSelezionato(UtentePassword utente) {
		this.utenteSelezionato = utente;
	}

	public void setEnteSelezionato(Ente es) {
		enteSelezionato = es;
	}

	public Ente getEnteSelezionato() {
		return enteSelezionato;
	}
	
	//Nella scene password aggiorna, da db, la lista degli enti e degli utenti
	public List<Ente> getEnti() {

		return dao.listaEnti();
	}

	public List<UtentePassword> getUtenti(int idEnte) {

		return dao.listaUtenti(idEnte);
	}
	
	/*
	 * le 4 procedure seguenti:
	 * 	-salvano nuovo Ente
	 * 	-interrogano il db sulla esistenza di record con quel nome di Ente
	 * 	-aggiornano un ente esistente
	 * 	-cancellano un record di ente dal db
	 */

	public int saveNewEnte(String nome, String url) {

		return dao.saveNewEnte(nome, url);

	}

	public boolean isExisting(String nome) {

		return dao.isExisting(nome);
	}

	public void updateEnte(Ente ente) {
		int id = ente.getId();
		String nome = ente.getNome();
		String url = ente.getUrl();

		dao.updateEnte(id, nome, url);

	}

	public int deleteEnte(Ente ente) {
		int id = ente.getId();
		return dao.deleteEnte(id);
	}
	
	/**
	 * metodi per gestione cancellazione utente
	 * 						inserimento nuovo utente
	 * 						modifica utente esistente
	 * @param utente
	 */

	public void deleteUtente(UtentePassword utente) {
		int id = utente.getId();
		dao.deleteUtente(id);
	}
	
	/*
	 * ritorna il valore dell'id (chiave della tavola utente_password) 
	 * prodotta dal salvataggio del record
	 */

	public int saveNewUtente(UtentePassword utentePassword) {
		int idEnte = utentePassword.getIdEnte();
		String utente = utentePassword.getUtente();
		String email = utentePassword.getEmail();
		String userId = utentePassword.getUserId();
		String password = utentePassword.getPassword();
		String note = utentePassword.getNote();

		return dao.saveNewUtente(idEnte, utente, email, userId, password, note);
	}

	public void updateUtente(UtentePassword utentePassword) {
		int id = utentePassword.getId();
		int idEnte = utentePassword.getIdEnte();
		String utente = utentePassword.getUtente();
		String email = utentePassword.getEmail();
		String userId = utentePassword.getUserId();
		String password = utentePassword.getPassword();
		String note = utentePassword.getNote();

		dao.updateUtente(id, idEnte, utente, email, userId, password, note);

	}

}
