package tany.it;
	
import tany.it.PasswordController;

import java.sql.SQLException;

import com.mchange.v2.c3p0.DataSources;

import db.DBConnect;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.stage.Stage;
import model.Model;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			PasswordController controller = loader.getController();
			
			//set Model
			Model model = Model.getModel();
			controller.setModel(model);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
		try {
			DataSources.destroy(DBConnect.getDataSource());
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
