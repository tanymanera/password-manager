package model;

import java.util.List;

import db.PasswordDAO;

public class Model {

	PasswordDAO dao;

	private List<Ente> listaEnti;
	private static Ente enteSelezionato = new Ente();
	private static UtentePassword utenteSelezionato = new UtentePassword();

	public static UtentePassword getUtenteSelezionato() {
		return utenteSelezionato;
	}

	public static void setUtenteSelezionato(UtentePassword utente) {
		Model.utenteSelezionato = utente;
	}

	public static void setEnteSelezionato(Ente es) {
		enteSelezionato = es;
	}
	
	public static Ente getEnteSelezionato() {
		return enteSelezionato;
	}

	public Model() {

		this.dao = new PasswordDAO();

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

	public void deleteUtente() {
		// TODO Auto-generated method stub
		System.out.println("Utente cancellato.");
	}

}
