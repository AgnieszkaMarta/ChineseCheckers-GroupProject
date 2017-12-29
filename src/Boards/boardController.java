package Boards;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class boardController implements Initializable
{
	//public SubScene sub;
	private Circle firstCircle, secondCircle, clickedCircle, helpCircle;
	private Color counterColor;
	private BufferedReader in;
	private PrintWriter out;
	private Chat chat;
	private String message;
	private Stage window;

	@FXML
	private TextField field;

	@FXML
	private TextArea area;

	@FXML
	private void sendMessage(ActionEvent event)
	{
		message=field.getText();
		if(!message.equals(""))
		out.println(message);
		field.setText("");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{

		//area.appendText("Hehe");
		area.setEditable(false);
		field.setText("");
		chat = new Chat();
		chat.start();
	}

	boardController(BufferedReader in, PrintWriter out)
	{
		this.in=in;
		this.out=out;
	}

	@FXML
	private void handleCheckers(MouseEvent event)
	{
		clickedCircle = (Circle)event.getSource();

		if(firstCircle==null)
		{
			firstCircle=clickedCircle;
			firstCircle = (Circle)event.getSource();
			//firstCircle.setFill(Color.WHITE);
			firstCircle.setStroke(Color.GREEN);
			firstCircle.setStrokeWidth(3);
		}
		else if(clickedCircle.equals(firstCircle))
		{
			firstCircle.setStroke(Color.BLACK);
			firstCircle.setStrokeWidth(1);
			firstCircle=null;
		}
		else
		{
			secondCircle=clickedCircle;
			counterColor = (Color) clickedCircle.getFill();
			//firstCircle.setStroke(Color.BLACK);
			//firstCircle.setStrokeWidth(1);
			//secondCircle.setStroke(Color.BLACK);
			secondCircle.setFill(firstCircle.getFill());
			firstCircle.setFill(counterColor);
			
			
			//secondCircle.setFill();
		}

		System.out.println(firstCircle.getLayoutX()+" "+firstCircle.getLayoutY());
	}
	
	/*@FXML
	private void handleButtonExit(ActionEvent exit)
	{
		window = ((Stage) (((MenuItem) exit.getSource()).getScene().getWindow()));
		window.hide();
	}*/

	public BufferedReader getIn()
	{
		return in;
	}

	public PrintWriter getOut()
	{
		return out;
	}

	public TextArea getArea()
	{
		return area;
	}

	public TextField getField()
	{
		return field;
	}

	private class Chat extends Thread
	{
		private String response, text="";
		//private TextField field;
		//private TextArea area;


		@Override
		public void run()
		{

			while(true)
			{
				//out.println(field.getText());
				//field.setText("");
				try
				{
					response = in.readLine();
					if(response == null || response.equals(""))
					{
						System.exit(0);
					}
				}
				catch (IOException ex)
				{
					response = "Error: " + ex;
				}
				System.out.println(response);
				area.appendText(response+"\n");
			}
		}
	}
}
