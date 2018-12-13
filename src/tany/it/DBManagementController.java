package tany.it;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ente;
import model.Model;
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

	
	public void setModel(Model model) {
		this.model = model;
		Ente enteSelezionato = Model.getEnteSelezionato();
        setTextFields(enteSelezionato);
	}

	public void handleGoBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			PasswordController controller = loader.getController();
			
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
	public void handleSaveBtn(ActionEvent e) {
		Ente enteSelezionato = Model.getEnteSelezionato();
		int id = enteSelezionato.getId();
		String nome = nomeTxt.getText().toUpperCase();
		String url = urlTxt.getText();
		
		if(nome.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Nome ente non specificato." );
			alert.showAndWait();
			return;
		}
	
		if(id == 0) {
			if(model.isExisting(nome)) {
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
			if(result.get() == ButtonType.OK) {
				enteSelezionato = new Ente(id, nome, url);
				model.updateEnte(enteSelezionato);
			}
			
		}
		setTextFields(enteSelezionato);
		Model.setEnteSelezionato(enteSelezionato);
	}
	
	@FXML
	public void handleDeletaBtn(ActionEvent e) {
		Ente enteSelezionato = Model.getEnteSelezionato();
		int rowErased = model.deleteEnte(enteSelezionato);
		if(rowErased == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			String content = "Presenti Utenti per " +
					enteSelezionato.getNome();
			alert.setContentText(content);
			alert.showAndWait();
			
		} else {
			Ente newEnte = new Ente();
			setTextFields(newEnte);
			Model.setEnteSelezionato(newEnte);
			
		}
	}
	
	@FXML
    void initialize() {
		assert nomeTxt != null : "fx:id=\"nomeTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert saveEnteBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert deleteEnteBtn != null : "fx:id=\"deleteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
        
    }
	
	private void setTextFields(Ente ente) {
		idTxt.setText(Integer.toString(ente.getId()));
        nomeTxt.setText(ente.getNome());
        urlTxt.setText(ente.getUrl());
	}
	
}
