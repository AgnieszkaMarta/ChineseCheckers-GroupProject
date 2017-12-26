package boards;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class serverCheckController {
    @FXML
    private void handleButtonNext(MouseEvent e ){
    	((Node)(e.getSource())).getScene().getWindow().hide();
        
         try {
        	 Parent root = FXMLLoader.load(getClass().getResource("/boards/board.fxml"));
        	 
        	 Stage stage = new Stage();
        	 stage.setTitle("Chinese Checker");
        	 stage.setScene(new Scene(root, 704, 464));
             stage.show();
             
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    	
    }
    
    
    @FXML
    private void handleButtonBack(MouseEvent e3 ){
    	((Node)(e3.getSource())).getScene().getWindow().hide();
        
         try {
        	 Parent root = FXMLLoader.load(getClass().getResource("/boards/start.fxml"));
        	 
        	 Stage stage = new Stage();
        	 stage.setTitle("Chinese Checker");
        	 stage.setScene(new Scene(root, 704, 464));
             stage.show();
             
		} catch (IOException e4) {
			e4.printStackTrace();
		}
    }     
}

