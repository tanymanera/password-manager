package model;

import java.util.List;

import db.PasswordDAO;

public class Model {

	PasswordDAO dao;

	private List<Ente> listaEnti;
	private static Ente enteSelezionato = new Ente();

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

}
