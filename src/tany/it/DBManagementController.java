package tany.it;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ente;
import model.Model;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DBManagementController {
	private Model model;
	
	private Ente enteSelezionato;
	
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
    private Button saveBtn;
    
    @FXML
    private Button deleteBtn;

	
	public void setModel(Model model) {
		this.model = model;
		this.enteSelezionato = Model.getEnteSelezionato();
        setTextFields();
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
	
		if(enteSelezionato.getId() == 0) {
			String nome = nomeTxt.getText();
			nome = nome.toUpperCase();
			//TO DO: non permettere nomi uguali
			String url = urlTxt.getText();
			int id = model.saveNewEnte(nome, url);
			
			enteSelezionato = new Ente(id, nome, url);
			setTextFields();
			
			System.out.println("Salvato nuovo ente");
		} else {
			System.out.println("Aggiornato ente esistente");
		}
		Model.setEnteSelezionato(this.enteSelezionato);
	}
	
	@FXML
	public void handleDeletaBtn(ActionEvent e) {
		
	}
	
	@FXML
    void initialize() {
		assert nomeTxt != null : "fx:id=\"nomeTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert urlTxt != null : "fx:id=\"urlTxt\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert saveBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
        assert deleteBtn != null : "fx:id=\"deleteBtn\" was not injected: check your FXML file 'dbManagement.fxml'.";
        
    }
	
	private void setTextFields() {
		idTxt.setText(Integer.toString(enteSelezionato.getId()));
        nomeTxt.setText(enteSelezionato.getNome());
        urlTxt.setText(enteSelezionato.getUrl());
	}
	
}
