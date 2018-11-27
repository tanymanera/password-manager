package model;

import java.util.List;

import db.PasswordDAO;

public class Model {
	
	PasswordDAO dao;
	
	public Model() {
		this.dao = new PasswordDAO();
	}
	public List<Ente> listEnti(){

		return dao.listaEnti();
	}
	
	public List<UtentePassword> listUtenti(int idEnte){
		
		return dao.listaUtenti(idEnte);
	}
	
	public void aggiornaUtente(int id, String userId, String password, String nota) {
		
		dao.aggiornaUtente(id, userId, password, nota);
		
	}
}
