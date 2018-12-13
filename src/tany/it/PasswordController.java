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

	private int enteID;

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

	}

	private void clearAll() {

		noteTxt.clear();
		userIdTxt.clear();
		passwordTxt.clear();

	}

	@FXML
	void initialize() {
		assert entiCombo != null : "fx:id=\"entiCombo\" was not injected: check your FXML file 'password.fxml'.";
		assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert utentePasswordTable != null : "fx:id=\"utentePasswordTable\" was not injected: check your FXML file 'password.fxml'.";
		assert utenteColumn != null : "fx:id=\"nameColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert userIdColumn != null : "fx:id=\"userIdColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert passwordColumn != null : "fx:id=\"passwordColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert emailColumn != null : "fx:id=\"emailColumn\" was not injected: check your FXML file 'password.fxml'.";
		assert showBtn != null : "fx:id=\"editBtn\" was not injected: check your FXML file 'password.fxml'.";
		assert noteTxt != null : "fx:id=\"noteTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert userIdTxt != null : "fx:id=\"userIdTxt\" was not injected: check your FXML file 'password.fxml'.";
		assert passwordTxt != null : "fx:id=\"passwordTxt\" was not injected: check your FXML file 'password.fxml'.";

		utenteColumn.setCellValueFactory(new PropertyValueFactory<>("utente"));
		userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

	}

	public void setModel(Model model) {
		this.model = model;
		// aggiunge una riga vuota alla combo
		entiCombo.getItems().add(new Ente());
		List<Ente> enti = model.getEnti();
		enti.sort(Comparator.comparing(Ente::getNome));
		entiCombo.getItems().addAll(enti);
		Ente selezionato = Model.getEnteSelezionato();
		if(selezionato != null) {
			entiCombo.getSelectionModel().select(selezionato);
			
			setInitialState();
		}
		

	}

	public ObservableList<UtentePassword> listUtenti(int idEnte) {
		ObservableList<UtentePassword> lista = FXCollections.observableArrayList();
		lista.add(new UtentePassword());
		lista.addAll(model.getUtenti(idEnte));
		return lista;

	}

	@FXML
	public void onEnteSelected(ActionEvent e) {
		
		Ente selezionato = entiCombo.getValue();
		if (selezionato != null) {
			setInitialState();
		}
		
		Model.setEnteSelezionato(selezionato);

	}
	
	private void setInitialState() {
		Ente selezionato = entiCombo.getValue();
		urlTxt.setText(selezionato.getUrl());

		enteID = selezionato.getId();
		ObservableList<UtentePassword> utenti = listUtenti(enteID);
		utentePasswordTable.setItems(utenti);

		clearAll();
	}

	@FXML
	public void handleDBManagement(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dbManagement.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			DBManagementController controller = loader.getController();

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

}
