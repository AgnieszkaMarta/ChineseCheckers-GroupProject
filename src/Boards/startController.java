package Boards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class startController
{
	private Stage window;

	@FXML
    private void handleButtonPlay(MouseEvent event)
    {
		window = ((Stage) (((Button) event.getSource()).getScene().getWindow()));

		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("/Boards/serverCheck.fxml"));

			window.setTitle("Chinese Checker");
			window.setScene(new Scene(root, 614, 412));
			window.show();

		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}

    }
    
    @FXML
    private void handleButtonAbout(MouseEvent event)
    {
    	//((Node)(e3.getSource())).getScene().getWindow().hide();
        
         try
		 {
        	 Parent root = FXMLLoader.load(getClass().getResource("/boards/aboutAGame.fxml"));
        	 
        	 Stage stage = new Stage();
        	 stage.setTitle("Chinese Checker");
        	 stage.setScene(new Scene(root, 642, 402));
             stage.show();
             
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
    	
    }
}
