package db;

import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import javafx.scene.control.Alert.AlertType;

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

	public int saveNewEnte(String nome, String url) {
		Connection conn = DBConnect.getConnection();
		String sql = "INSERT INTO ente (nome, url) " + 
		"VALUES ( ?, ?);";
		PreparedStatement st;
		
		try {
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, nome);
			st.setString(2, url);
			
			st.executeUpdate();
			
			ResultSet ids = st.getGeneratedKeys();
			ids.next();
			int id = ids.getInt(1);
			
			st.close();
			conn.close();
			
			return id;
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
	}
	
	public int saveNewUtente(int idEnte, String utente, String email, String userId, String password, String note) {
		Connection conn = DBConnect.getConnection();
		String sql = "INSERT INTO utente_password (id_ente, utente, email, user_id, pw, note) " + 
		"VALUES ( ?, ?, ?, ?, ?, ?);";
		PreparedStatement st;
		int id;
		
		try {
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, idEnte);
			st.setString(2, utente);
			st.setString(3, email);
			st.setString(4, userId);
			st.setString(5, password);
			st.setString(6, note);
			
			st.executeUpdate();
			
			ResultSet ids = st.getGeneratedKeys();
			ids.next();
			id = ids.getInt(1);
			
			st.close();
			conn.close();
			
			return id;
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
	}

	public boolean isExisting(String nome) {
		Connection conn = DBConnect.getConnection();
		String sql = "SELECT id, nome, url FROM ente WHERE nome=?;";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, nome);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		return false;
	}

	public void updateEnte(int id, String nome, String url) {
		Connection conn = DBConnect.getConnection();
		String sql = "UPDATE ente " +
				"SET nome=?, url=? " + 
				"WHERE id=?;";
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, nome);
			st.setString(2, url);
			st.setInt(3, id);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
	}
	
	public void updateUtente(int id, int idEnte, String utente, String email, String userId, String password,
			String note) {
		Connection conn = DBConnect.getConnection();
		String sql = "UPDATE utente_password " +
				"SET id_ente=?, utente=?, email=?, user_id=?, pw=?, note=? " + 
				"WHERE id=?;";
		
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(7, id);
			st.setInt(1, idEnte);
			st.setString(2, utente);
			st.setString(3, email);
			st.setString(4, userId);
			st.setString(5, password);
			st.setString(6, note);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
	}

	public int deleteEnte(int id) {
		Connection conn = DBConnect.getConnection();
		String sql = "DELETE FROM ente WHERE id=? " +
		"AND id NOT IN (SELECT id_ente FROM utente_password);";
		PreparedStatement st;
		int rowsErased = 0;
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			
			rowsErased = st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		return rowsErased;
	}

	public void deleteUtente(int id) {
		Connection conn = DBConnect.getConnection();
		String sql = "DELETE FROM utente_password WHERE id=? ";
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Errore nella connessione al database.");
		}
		
	}

}
