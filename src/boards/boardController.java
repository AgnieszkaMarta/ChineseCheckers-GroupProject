package boards;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class boardController
{
	//public SubScene sub;
	public SubScene sub;
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		Label newLabel = new Label("Hello! " );
		sub.add(newLabel);
		}
}
