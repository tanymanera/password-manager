package model;

import java.util.List;

import db.PasswordDAO;

public class Model {
	
	private static Model model = null;

	PasswordDAO dao;

	private List<Ente> listaEnti;
	private static Ente enteSelezionato = new Ente();
	private UtentePassword utenteSelezionato;
	
	private Model() {
		this.dao = new PasswordDAO();
		
		this.utenteSelezionato = new UtentePassword();

	}

	public UtentePassword getUtenteSelezionato() {
		return utenteSelezionato;
	}

	public void setUtenteSelezionato(UtentePassword utente) {
		this.utenteSelezionato = utente;
	}

	public static void setEnteSelezionato(Ente es) {
		enteSelezionato = es;
	}
	
	public static Ente getEnteSelezionato() {
		return enteSelezionato;
	}

	public static synchronized Model getModel() {
		
		if(model == null) {
			model = new Model();
		}
		return model;
		
	}

	public List<Ente> getEnti() {

		if (listaEnti == null) {
			listaEnti = dao.listaEnti();

		}

		return listaEnti;
	}

	public List<UtentePassword> getUtenti(int idEnte) {

		return dao.listaUtenti(idEnte);
	}

	public void aggiornaUtente(int id, String userId, String password, String nota) {

		dao.aggiornaUtente(id, userId, password, nota);

	}

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

	public void deleteUtente(UtentePassword utente) {
		int id = utente.getId();
		dao.deleteUtente(id);
	}

	public int saveNewUtente(UtentePassword newUtente) {
		int idEnte = newUtente.getIdEnte();
		String utente = newUtente.getUtente();
		String email = newUtente.getEmail();
		String userId = newUtente.getUserId();
		String password = newUtente.getPassword();
		String note = newUtente.getNote();
		
		return dao.saveNewUtente(idEnte, utente, email, userId, password, note);
	}

	public void updateUtente(UtentePassword newUtente) {
		int id = newUtente.getId();
		int idEnte = newUtente.getIdEnte();
		String utente = newUtente.getUtente();
		String email = newUtente.getEmail();
		String userId = newUtente.getUserId();
		String password = newUtente.getPassword();
		String note = newUtente.getNote();
		
		dao.updateUtente(id, idEnte, utente, email, userId, password, note);
		
	}

}
