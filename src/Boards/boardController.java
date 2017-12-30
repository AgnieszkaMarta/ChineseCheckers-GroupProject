package Boards;

import javafx.event.ActionEvent;
import java.lang.Math;
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
import java.util.ArrayList;
import java.util.EventObject;
import java.util.ResourceBundle;

import Main.Server.Player;

public class boardController implements Initializable
{
	
	private static volatile boardController instance = null;
	private Circle firstCircle, secondCircle, clickedCircle;
	private Color counterColor, boardColor;
	private BufferedReader in;
	private PrintWriter out;
	private Chat chat;
	private String message, nick="";
	private double fX, fY, sX, sY, length;
	
	//boardColor=Color.valueOf("ffe91f")
	
	protected boardController() {};
	
	public static boardController getInstance() {
        if (instance == null) {
            synchronized (boardController.class) {
                if (instance == null) {
                    instance = new boardController();
                }
            }
        }
        return instance;
    }
	
	public static void resetInstance() {
		instance = null;

	}

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
	
	public void setNick(String nick){
		this.nick=nick;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		area.appendText(nick);
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
			fX=firstCircle.getLayoutX();
			fY=firstCircle.getLayoutY();
		}
		else if(clickedCircle.equals(firstCircle))
		{
			firstCircle.setStroke(Color.BLACK);
			firstCircle.setStrokeWidth(1);
			firstCircle=null;
		}
		else
		{	
			sX=clickedCircle.getLayoutX();
			sY=clickedCircle.getLayoutY();
			length=Math.sqrt(Math.pow(sX-fX,2)+Math.pow(sY-fY,2));
			if(length<=40) {
				secondCircle=clickedCircle;
				counterColor = (Color) clickedCircle.getFill();
				
				//secondCircle.setStroke(Color.BLACK);
				secondCircle.setFill(firstCircle.getFill());
				firstCircle.setFill(counterColor);
				firstCircle.setStroke(Color.BLACK);
				firstCircle.setStrokeWidth(1);
				firstCircle=null;
			}
			else
			{
				System.out.println("This move is not possible");
				firstCircle.setStroke(Color.BLACK);
				firstCircle.setStrokeWidth(1);
				firstCircle=null;
			}
			
			//secondCircle.setFill();
		}
	}
	
	/*@FXML
	private void handleButtonExit(ActionEvent ex)
	{
		//((Node)(ex.getSource())).getScene().getWindow().hide();
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
