package tany.it;

import model.Ente;
import model.Model;
import model.UtentePassword;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.control.Tab;
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
	private Model model = Model.getModel();

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
	private TextArea noteTxt;

	@FXML
	private TextField userIdTxt;

	@FXML
	private TextField passwordTxt;

	@FXML
	void handleShow(MouseEvent event) {

		UtentePassword utente = utentePasswordTable.getSelectionModel().getSelectedItem();
		// la riga vuota contiene un UtentePassword con IdEnte = 0
		utente.setIdEnte(model.getEnteSelezionato().getId());
		model.setUtenteSelezionato(utente);

		setTextFields();

	}

	public void setTextFields() {
		UtentePassword utente = model.getUtenteSelezionato();
		userIdTxt.setText(utente.getUserId());
		passwordTxt.setText(utente.getPassword());
		noteTxt.setText(utente.getNote());
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
		Ente selezionato = model.getEnteSelezionato();
		setInitialState(selezionato);
		setTextFields();

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
		Model model = Model.getModel();

		if (selezionato != null) {
			setInitialState(selezionato);
		}

		model.setEnteSelezionato(selezionato);
		UtentePassword utente = new UtentePassword();
		utente.setIdEnte(selezionato.getId());
		model.setUtenteSelezionato(utente);
		
		setTextFields();

	}

	private void setInitialState(Ente ente) {
		urlTxt.setText(ente.getUrl());

		int enteID = ente.getId();
		ObservableList<UtentePassword> utenti = listUtenti(enteID);
		utentePasswordTable.setItems(utenti);

	}

	@FXML
	public void handleDBManagement(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dbManagement.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			DBManagementController controller = loader.getController();

			// set Model
			Model model = Model.getModel();
			controller.setModel(model);

			//fa il binding tra il valore di idEnte di utenteSelezionato e utenteTab
			controller.getUtenteTab().disableProperty().bind(new BooleanBinding() {
				{
					super.bind(model.getUtenteSelezionato().idEnteProperty());
				}

				@Override
				protected boolean computeValue() {

					if (model.getUtenteSelezionato().idEnteProperty().get() > 0) {

						return false;
					}

					return true;
				}
			});

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
