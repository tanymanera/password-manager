package tany.it;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
		Ente enteSelezionato = Model.getEnteSelezionato();
		UtentePassword utenteSelezionato = Model.getUtenteSelezionato();
		setTextFields(enteSelezionato, utenteSelezionato);
	}

	public void handleGoBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			PasswordController controller = loader.getController();

			// set Model
			Model model = new Model();
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

//	@FXML
//	public void handelSaveBtn() {
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setHeaderText("Attenzione: modifica irreversibile dei dati.");
//		String userId = userIdTxt.getText();
//		String password = passwordTxt.getText();
//		String nota = noteTxt.getText();
//		String content = "Valori che saranno salvati:\n" + userId + "\n" + password + "\n" + nota;
//		alert.setContentText(content);
//		Optional<ButtonType> result = alert.showAndWait();
//		UtentePassword utente = utentePasswordTable.getSelectionModel().getSelectedItem();
//		int id = utente.getId();
//
//		if (result.get() == ButtonType.OK) {
//			model.aggiornaUtente(id, userId, password, nota);
//
//			ObservableList<UtentePassword> utenti = listUtenti(ente);
//			utentePasswordTable.setItems(utenti);
//			
//		} 
//		clearAll();
//	}

	@FXML
	public void handleSaveEnteBtn(ActionEvent e) {
		Ente enteSelezionato = Model.getEnteSelezionato();
		int id = enteSelezionato.getId();
		String nome = nomeTxt.getText().toUpperCase();
		String url = urlTxt.getText();

		if (nome.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Nome ente non specificato.");
			alert.showAndWait();
			return;
		}

		if (id == 0) {
			if (model.isExisting(nome)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Nome ente " + nome + " già presente.");
				alert.showAndWait();
				return;
			}

			int newID = model.saveNewEnte(nome, url);

			enteSelezionato = new Ente(newID, nome, url);

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Attenzione: modifica irreversibile dei dati.");
			String content = "Nuovi valori: " + nome + ", " + url;
			alert.setContentText(content);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				enteSelezionato = new Ente(id, nome, url);
				model.updateEnte(enteSelezionato);
			}

		}
		setTextFields(enteSelezionato, Model.getUtenteSelezionato());
		Model.setEnteSelezionato(enteSelezionato);
	}

	@FXML
	public void handleDeleteEnteBtn(ActionEvent e) {
		Ente enteSelezionato = Model.getEnteSelezionato();
		int rowErased = model.deleteEnte(enteSelezionato);
		if (rowErased == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			String content = "Presenti Utenti per " + enteSelezionato.getNome();
			alert.setContentText(content);
			alert.showAndWait();

		} else {
			Ente newEnte = new Ente();
			setTextFields(newEnte, Model.getUtenteSelezionato());
			Model.setEnteSelezionato(newEnte);

		}

	}

	@FXML
	public void handleSaveUtenteBtn(ActionEvent e) {
		System.out.println("Salvo un utente.");
	}

	@FXML
	public void handleDeleteUtenteBtn(ActionEvent e) {
		UtentePassword utente = Model.getUtenteSelezionato();
		if (utente.getId() == 0) {
			// TO DO: Alert ?
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		String content = "Cancellazione definitiva di:\n" + utente.toString();
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			model.deleteUtente();
			Ente ente = Model.getEnteSelezionato();
			Model.setUtenteSelezionato(new UtentePassword(ente.getId()));
//			System.out.println(Model.getUtenteSelezionato().getIdEnte());
			utente = Model.getUtenteSelezionato();
			setTextFields(ente, utente);
		}
	}

	@FXML
	void initialize() {
		assert nomeTxt != null : "fx:id=\"nomeTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert saveEnteBtn != null : "fx:id=\"saveEnteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert idTxt != null : "fx:id=\"idTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
		assert deleteEnteBtn != null : "fx:id=\"deleteEnteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
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

}
