package boards;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class boardController
{
	//public SubScene sub;
	public SubScene sub;
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		}
	
	@FXML
    private void handleButtonPlay(MouseEvent e ) {
		Label newLabel = new Label("Hello! " );
		sub.add(newLabel);
	}
}
