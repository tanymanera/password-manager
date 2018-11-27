package db;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Ente;
import model.UtentePassword;

public class PasswordDAO {
	
	public List<Ente> listaEnti(){
		List<Ente> enti = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		String sql = "SELECT id, nome, url FROM ente";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				Ente e = new Ente();
				int id = res.getInt("id");
				String nome = res.getString("nome");
				String url = res.getString("url");
				e.setId(id);
				e.setNome(nome);
				if(url != null) {
					e.setUrl(url);
				}
				enti.add(e);
			}
			res.close();
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
		return enti;
		
	}
	
	public List<UtentePassword> listaUtenti(int idEnte){
		List<UtentePassword> utenti = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		String sql = "SELECT id, id_ente, utente, " +
				"email, user_id, pw, note " + 
				"FROM utente_password " +
				"WHERE id_ente=?;";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, idEnte);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				UtentePassword u = new UtentePassword();
				u.setId(res.getInt("id"));
				u.setIdEnte(res.getInt("id_ente"));
				String utente = res.getString("utente");
				if(utente != null) {
					u.setUtente(utente);
				}
				String email = res.getString("email");
				if(email != null) {
					u.setEmail(email);
				}
				u.setUserId(res.getString("user_id"));
				u.setPassword(res.getString("pw"));
				u.setNote(res.getString("note"));
				
				utenti.add(u);
			}
			res.close();
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
		return utenti;
		
	}
	
	public void aggiornaUtente(int id, String userId, String password, String nota) {
		Connection conn = DBConnect.getConnection();
		String sql = "UPDATE utente_password " + 
		"SET user_id=?, pw=?, note=? " +
				"WHERE id=?;";
		PreparedStatement st;
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setString(1, userId);
			st.setString(2, password);
			st.setString(3, nota);
			st.setInt(4, id);
			
			st.executeUpdate();
			
			st.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		return;
	}

}
