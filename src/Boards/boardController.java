package Boards;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class boardController
{
	//public SubScene sub;
	private Circle firstCircle, secondCircle, clickedCircle, helpCircle;
	private BufferedReader in;
	private PrintWriter out;
	public SubScene sub;
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		}

	boardController(BufferedReader in, PrintWriter out)
	{
		this.in=in;
		this.out=out;
	}

	public void setIn(BufferedReader serverIn)
	{
		this.in=serverIn;
	}

	public void setOut(PrintWriter serverOut)
	{
		this.out=serverOut;
	}

	@FXML
	private void handleCheckers(MouseEvent event)
	{
		System.out.println(in);
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
			firstCircle.setStroke(Color.BLACK);
			firstCircle.setStrokeWidth(1);
			firstCircle.setFill(clickedCircle.getFill());
			//secondCircle.setFill();
		}

		System.out.println(firstCircle.getLayoutX()+" "+firstCircle.getLayoutY());
	}


}
