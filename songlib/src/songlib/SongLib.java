

package songlib;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SongLib extends Application{
	
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(); 
	
		loader.setLocation(getClass().getResource("/songlib/SongLib.fxml")); 

		AnchorPane root = (AnchorPane)loader.load();
		
		SongLibController list = loader.getController();
		
		list.start();

		Scene scene = new Scene(root);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene); 
	
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);

	}

}
