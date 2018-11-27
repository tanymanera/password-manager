package tany.it;

import model.Ente;
import model.Model;
import model.UtentePassword;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PasswordController {
	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Ente> entiCombo;

	@FXML
	private TextField urlTxt;

	@FXML
	private Button selectBtn;

	@FXML
	private TableView<UtentePassword> utentePasswordTable;

	@FXML
	private TableColumn<UtentePassword, String> utenteColumn;

	@FXML
	private TableColumn<UtentePassword, String> userIdColumn;

	@FXML
	private TableColumn<UtentePassword, String> passwordColumn;

	@FXML
	private TableColumn<UtentePassword, String> emailColumn;

	@FXML
	private Button showBtn;

	@FXML
	private TextArea noteTxt;

	@FXML
	private TextField userIdTxt;

	@FXML
	private TextField passwordTxt;

	@FXML
	private Button saveBtn;
	
	@FXML
	private Button editBtn;

	private int ente;
	
	@FXML
	public void handleEditBtn() {
		noteTxt.setEditable(true);
		userIdTxt.setEditable(true);
		passwordTxt.setEditable(true);
		saveBtn.setDisable(false);
		
	}

	@FXML
	public void handelSaveBtn() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Attenzione: modifica irreversibile dei dati.");
		String userId = userIdTxt.getText();
		String password = passwordTxt.getText();
		String nota = noteTxt.getText();
		String content = "Valori che saranno salvati:\n" + userId + "\n" + password + "\n" + nota;
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		UtentePassword utente = utentePasswordTable.getSelectionModel().getSelectedItem();
		int id = utente.getId();

		if (result.get() == ButtonType.OK) {
			model.aggiornaUtente(id, userId, password, nota);

			ObservableList<UtentePassword> utenti = listUtenti(ente);
			utentePasswordTable.setItems(utenti);
			
		} 
		clearAll();
	}

	@FXML
	void handleShow(MouseEvent event) {

		UtentePassword utente = utentePasswordTable.getSelectionModel().getSelectedItem();

		if (utente == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Selezionare una riga.");
			alert.showAndWait();
			return;
		}
		noteTxt.setText(utente.getNote());
		userIdTxt.setText(utente.getUserId());
		passwordTxt.setText(utente.getPassword());
		editBtn.setDisable(false);
		saveBtn.setDisable(true);
		noteTxt.setEditable(false);
		userIdTxt.setEditable(false);
		passwordTxt.setEditable(false);

	}

	private void clearAll() {
		saveBtn.setDisable(true);
		editBtn.setDisable(true);
		noteTxt.clear();
		userIdTxt.clear();
		passwordTxt.clear();
		noteTxt.setEditable(false);
		userIdTxt.setEditable(false);
		passwordTxt.setEditable(false);
	}

	@FXML
	void initialize() {
		assert entiCombo != null : "fx:id=\"entiCombo\" was not injected: check your FXML file 'password.fxml'.";
		assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert selectBtn != null : "fx:id=\"selectBtn\" was not injected: check your FXML file 'password.fxml'.";
		assert utentePasswordTable != null : "fx:id=\"utentePasswordTable\" was not injected: check your FXML file 'password.fxml'.";
		assert utenteColumn != null : "fx:id=\"nameColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert userIdColumn != null : "fx:id=\"userIdColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert passwordColumn != null : "fx:id=\"passwordColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert emailColumn != null : "fx:id=\"emailColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert showBtn != null : "fx:id=\"editBtn\" was not injected: check your FXML file 'password.fxml'.";
		assert noteTxt != null : "fx:id=\"noteTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert userIdTxt != null : "fx:id=\"userIdTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert passwordTxt != null : "fx:id=\"passwordTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert saveBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'password.fxml'.";

		utenteColumn.setCellValueFactory(new PropertyValueFactory<>("utente"));
		userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

	}

	public void setModel(Model model) {
		this.model = model;
		List<Ente> enti = model.listEnti();
		enti.sort(Comparator.comparing(Ente::getNome));
		entiCombo.getItems().addAll(enti);

	}

	public ObservableList<UtentePassword> listUtenti(int idEnte) {
		ObservableList<UtentePassword> lista = FXCollections.observableArrayList();
		lista.addAll(model.listUtenti(idEnte));
		return lista;

	}

	@FXML
	public void onEnteSelected(ActionEvent e) {
		noteTxt.clear();
		Ente selezionato = entiCombo.getValue();
		if (selezionato == null) {
			urlTxt.setText("Selezionare un ente.");
			return;
		}
		urlTxt.setText(selezionato.getUrl());

		ente = selezionato.getId();
		ObservableList<UtentePassword> utenti = listUtenti(ente);
		utentePasswordTable.setItems(utenti);

		clearAll();

	}
	
	@FXML
	public void handleDBManagement(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dbManagement.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			DBManagementController controller = loader.getController();
			
			//set Model
			Model model = new Model();
			controller.setModel(model);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
