package tany.it;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ente;
import model.Model;
import model.UtentePassword;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DBManagementController {
	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField idTxt;

	@FXML
	private TextField nomeTxt;

	@FXML
	private TextField urlTxt;

	@FXML
	private Button saveEnteBtn;

	@FXML
	private Button deleteEnteBtn;

	@FXML
	private Tab utenteTab;

	public Tab getUtenteTab() {
		return utenteTab;
	}

	@FXML
	private TextField idUtenteTxt;

	@FXML
	private TextField enteUtenteTxt;

	@FXML
	private TextField utenteTxt;

	@FXML
	private TextField emailTxt;

	@FXML
	private TextField userIdTxt;

	@FXML
	private TextField passwordTxt;

	@FXML
	private TextArea noteTxt;

	@FXML
	private Button saveUtenteBtn;

	@FXML
	private Button deleteUtenteBtn;

	public void setModel(Model model) {
		this.model = model;
		Ente enteSelezionato = model.getEnteSelezionato();
		UtentePassword utenteSelezionato = model.getUtenteSelezionato();
		setTextFields(enteSelezionato, utenteSelezionato);

	}

	public void handleGoBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			PasswordController controller = loader.getController();

			// set Model
			Model model = Model.getModel();
			controller.setModel(model);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSaveEnteBtn(ActionEvent e) {
		Ente enteSelezionato = model.getEnteSelezionato();
		UtentePassword utenteSelezionato = model.getUtenteSelezionato();

		// acquisisce i campi
		int id = enteSelezionato.getId();
		String nome = nomeTxt.getText().toUpperCase();
		String url = urlTxt.getText();

		if (nome.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Nome ente non specificato.");
			alert.showAndWait();
			return;
		}

		if (model.isExisting(nome)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Nome ente " + nome + " già presente.");
			alert.showAndWait();
			return;
		}

		if (id == 0) { // Nuovo ente da salvare

			int newID = model.saveNewEnte(nome, url);
			enteSelezionato = new Ente(newID, nome, url);

			// il nuovo ente può accogliere un nuovo utente con enteId = newID
			// non si può usare il costruttore perchè si rompe il binding
			utenteSelezionato.setId(0);
			utenteSelezionato.setIdEnte(newID);
			utenteSelezionato.setUtente("");
			utenteSelezionato.setEmail("");
			utenteSelezionato.setUserId("");
			utenteSelezionato.setPassword("");
			utenteSelezionato.setNote("");

		} else { // id diverso da zero e quindi corrispondente ad un ente già esistente
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Attenzione: modifica dei dati.");
			String content = "Nuovi valori: " + nome + ", " + url;
			alert.setContentText(content);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				enteSelezionato = new Ente(id, nome, url);
				model.updateEnte(enteSelezionato);
			}

		}
		
		updateModelAndTextFields(enteSelezionato, utenteSelezionato);

	}

	@FXML
	public void handleDeleteEnteBtn(ActionEvent e) {
		Ente enteSelezionato = model.getEnteSelezionato();
		UtentePassword utenteSelezionato = model.getUtenteSelezionato();
		
		if(enteSelezionato.getId() == 0) {
			return;
		}

		int rowErased = model.deleteEnte(enteSelezionato);

		if (rowErased == 0) { // Non è stato cancellato nessun record
			Alert alert = new Alert(AlertType.INFORMATION);
			String content = "Presenti Utenti per " + enteSelezionato.getNome();
			alert.setContentText(content);
			alert.showAndWait();

		} else {
			enteSelezionato = new Ente();
			
			utenteSelezionato.setId(0);
			utenteSelezionato.setIdEnte(0);
			utenteSelezionato.setUtente("");
			utenteSelezionato.setEmail("");
			utenteSelezionato.setUserId("");
			utenteSelezionato.setPassword("");
			utenteSelezionato.setNote("");

		}
		updateModelAndTextFields(enteSelezionato, utenteSelezionato);

	}

	@FXML
	public void handleSaveUtenteBtn(ActionEvent e) {
		Ente enteSelezionato = model.getEnteSelezionato();
		UtentePassword utenteSelezionato = model.getUtenteSelezionato();

		int id = utenteSelezionato.getId();
		int idEnte = utenteSelezionato.getIdEnte();
		String utente = utenteTxt.getText();
		String email = emailTxt.getText();
		String userId = userIdTxt.getText();
		String password = passwordTxt.getText();
		String note = noteTxt.getText();

		if (userId.equals("") || password.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Campo obbligatorio assente");
			alert.showAndWait();
			return;
		}

		UtentePassword newUtente = new UtentePassword(id, idEnte, utente, email, userId, password, note);

		if (id == 0) {
			// leggo il id creato e aggiorno Utente appena creato.
			int newID = model.saveNewUtente(newUtente);
			newUtente.setId(newID);
			utenteSelezionato = newUtente;

		} else { // id utente diverso da zero quindi si tratta di modificare un record esistente
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Attenzione: modifica dei dati.");
			String content = "Nuovi valori: " + newUtente.toString();
			alert.setContentText(content);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				utenteSelezionato = newUtente;
				model.updateUtente(utenteSelezionato);;			
				}
		}

		// Settaggio dei campi di testo e aggiornamento del modello
		updateModelAndTextFields(enteSelezionato, newUtente);
	}

	@FXML
	public void handleDeleteUtenteBtn(ActionEvent e) {
		Ente enteSelezionato = model.getEnteSelezionato();
		UtentePassword utenteSelezionato = model.getUtenteSelezionato();

		if (utenteSelezionato.getId() == 0) {
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		String content = "Cancellazione definitiva di:\n" + utenteSelezionato.toString();
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			model.deleteUtente(utenteSelezionato);
			
			//non si usa costruttore per non interrompre il bindig
			utenteSelezionato.setId(0);
			utenteSelezionato.setIdEnte(enteSelezionato.getId());
			utenteSelezionato.setUtente("");
			utenteSelezionato.setEmail("");
			utenteSelezionato.setUserId("");
			utenteSelezionato.setPassword("");
			utenteSelezionato.setNote("");

			updateModelAndTextFields(enteSelezionato, utenteSelezionato);
		}
	}

	@FXML
	void initialize() {
		assert nomeTxt != null : "fx:id=\"nomeTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert saveEnteBtn != null : "fx:id=\"saveEnteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert idTxt != null : "fx:id=\"idTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert deleteEnteBtn != null : "fx:id=\"deleteEnteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert utenteTab != null : "fx:id=\"utenteTab\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert idUtenteTxt != null : "fx:id=\"idUtenteTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert enteUtenteTxt != null : "fx:id=\"enteUtenteTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert utenteTxt != null : "fx:id=\"utenteTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert emailTxt != null : "fx:id=\"emailTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert userIdTxt != null : "fx:id=\"userIdTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert passwordTxt != null : "fx:id=\"password\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert noteTxt != null : "fx:id=\"noteTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert saveUtenteBtn != null : "fx:id=\"saveUtenteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert deleteUtenteBtn != null : "fx:id=\"deleteUtenteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";

	}

	private void setTextFields(Ente ente, UtentePassword utente) {
		// tab Ente
		idTxt.setText(Integer.toString(ente.getId()));
		nomeTxt.setText(ente.getNome());
		urlTxt.setText(ente.getUrl());

		// tab Utente
		idUtenteTxt.setText(Integer.toString(utente.getId()));
		enteUtenteTxt.setText(ente.getNome());
		utenteTxt.setText(utente.getUtente());
		emailTxt.setText(utente.getEmail());
		userIdTxt.setText(utente.getUserId());
		passwordTxt.setText(utente.getPassword());
		noteTxt.setText(utente.getNote());

	}

	/*
	 * dopo la modifica di ente o utente setta: -enteSelezionato e utenteSelezionato
	 * in Model -aggiorna le caselle di testo delle 2 tabs della scene
	 */
	private void updateModelAndTextFields(Ente ente, UtentePassword utente) {
		model.setEnteSelezionato(ente);
		model.setUtenteSelezionato(utente);

		setTextFields(ente, utente);

	}

}
