package Boards;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.lang.Math;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
	private ClientStatus state;
	private static volatile boardController instance = null;
	private Circle firstCircle, secondCircle, clickedCircle;
	private Color counterColor, boardColor;
	private RadialGradient counterColor1;
	private LinearGradient counterColor2;
	private BufferedReader in;
	private PrintWriter out;
	private Communicate communicate;
	private String message, nick="";
	private double fX, fY, sX, sY, length;
	int i, fi, si;
	boolean executed;
	
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
	private AnchorPane game;

	@FXML
	private ArrayList<Circle> circleArrayList;

	@FXML
	private TextField field;

	@FXML
	private TextArea area;

	@FXML
	private Button readyButton;

	@FXML
	private void setReady(MouseEvent event)
	{
		state=ClientStatus.READY;
		out.println("STATE"+state);
	}

	@FXML
	private Label labelReady;

	@FXML
	private Rectangle rectangleReady;

	@FXML
	private Polygon polygon1, polygon2, polygon3, polygon4, polygon5, polygon6;

	@FXML
	private void sendMessage(ActionEvent event)
	{
		message=field.getText();
		if(!message.equals(""))
		out.println("MESSAGE"+message);
		field.setText("");
	}
	
	public void setNick(String nick){
		this.nick=nick;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		polygon1.setVisible(false);
		polygon2.setVisible(false);
		polygon3.setVisible(false);
		polygon4.setVisible(false);
		polygon5.setVisible(false);
		polygon6.setVisible(false);

		area.appendText(nick);
		area.setEditable(false);
		field.setText("");
		communicate = new Communicate();
		communicate.start();
		for(i=0;i<circleArrayList.size();i++)
		{
			game.getChildren().add(circleArrayList.get(i));
		}
	}

	boardController(BufferedReader in, PrintWriter out)
	{
		this.in=in;
		this.out=out;
		state = ClientStatus.UNREADY;
	}

	@FXML
	private void handleCheckers(MouseEvent event)
	{
		if(state.equals(ClientStatus.TURN))
		{
			clickedCircle = (Circle) event.getSource();

			if (firstCircle == null) {

				firstCircle = clickedCircle;
				firstCircle = (Circle) event.getSource();
				//firstCircle.setFill(Color.WHITE);
				firstCircle.setStroke(Color.GREEN);
				firstCircle.setStrokeWidth(3);
				fX = firstCircle.getLayoutX();
				fY = firstCircle.getLayoutY();
			} else if (clickedCircle.equals(firstCircle)) {
				firstCircle.setStroke(Color.BLACK);
				firstCircle.setStrokeWidth(1);
				firstCircle = null;
			} else {
				sX = clickedCircle.getLayoutX();
				sY = clickedCircle.getLayoutY();
				length = Math.sqrt(Math.pow(sX - fX, 2) + Math.pow(sY - fY, 2));
				if (length <= 40) {
					secondCircle = clickedCircle;
					counterColor = (Color) clickedCircle.getFill();

					//secondCircle.setStroke(Color.BLACK);
					secondCircle.setFill(firstCircle.getFill());
					firstCircle.setFill(counterColor);
					firstCircle.setStroke(Color.BLACK);
					firstCircle.setStrokeWidth(1);
					out.println("MOVE"+secondCircle.getLayoutX()+" "+secondCircle.getLayoutY()+" "+firstCircle.getLayoutX()+" "+firstCircle.getLayoutY());
					firstCircle = null;
					clickedCircle=null;
					secondCircle=null;
					System.out.println("END. MY. TURN.");
					out.println("MOVEDONE");
					state=ClientStatus.UNTURN;
				} else {
					System.out.println("This move is not possible");
					firstCircle.setStroke(Color.BLACK);
					firstCircle.setStrokeWidth(1);
					firstCircle = null;
				}

				//secondCircle.setFill();
			}
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

	public ClientStatus getState()
	{
		switch (state)
		{
			case READY:
				out.println("STATE"+state);
				break;
		}
		return state;
	}

	private class Communicate extends Thread
	{
		private String response;

		@Override
		public void run()
		{

			while(true)
			{
				try
				{
					response = in.readLine();
					System.out.println(response);
					if(response == null || response.equals(""))
					{
						System.exit(0);
					}
					else if(response.substring(0,7).equals("MESSAGE"))
					{
						response=response.substring(7);
						area.appendText(response+"\n");

					}
					else if(response.substring(0,5).equals("STATE"))
					{
						response=response.substring(5);
						switch (response)
						{
							case "GAMESTARTED":
								labelReady.setVisible(false);
								rectangleReady.setVisible(false);
								readyButton.setVisible(false);
							case "UNTURN":
								state = ClientStatus.UNTURN;
								break;
							case "TURN":
								executed=false;
								state = ClientStatus.TURN;
						}
					}
					else if(response.substring(0,4).equals("MOVE"))
					{
						response=response.substring(4);
						switch (response)
						{
							default:
								fX = Double.parseDouble(response.substring(0,5));
								fY = Double.parseDouble(response.substring(5,10));
								sX = Double.parseDouble(response.substring(12,17));
								sY = Double.parseDouble(response.substring(18,23));
								for(i=0;i<circleArrayList.size();i++)
								{
									clickedCircle = circleArrayList.get(i);
									if(clickedCircle.getLayoutX()==(fX) && clickedCircle.getLayoutY()==fY)
									{
										System.out.println(fX+" "+fY+" "+clickedCircle.getLayoutX()+" "+clickedCircle.getLayoutY());
										firstCircle = clickedCircle;
										fi=i;
									}
									else if(clickedCircle.getLayoutX()==sX & clickedCircle.getLayoutY()==sY)
									{
										System.out.println(sX+" "+sY+" "+clickedCircle.getLayoutX()+" "+clickedCircle.getLayoutY());

										secondCircle = clickedCircle;
										si=i;
									}
								}
								clickedCircle = firstCircle;
								try
								{
									counterColor=(Color)firstCircle.getFill();
								}
								catch (ClassCastException exception)
								{
									try
									{
										counterColor1 = (RadialGradient) firstCircle.getFill();
									}
									catch (ClassCastException exception1)
									{
										counterColor2 = (LinearGradient) firstCircle.getFill();
									}
								}
								//counterColor=(Color)firstCircle.getFill();
								firstCircle.setFill(secondCircle.getFill());
								secondCircle.setFill(counterColor);
								/*circleArrayList.set(fi, secondCircle);
								circleArrayList.set(si, firstCircle);
								game.getChildren().clear();
								for(i=0;i<circleArrayList.size();i++)
								{
									game.getChildren().add(circleArrayList.get(i));
								}*/
								firstCircle=null;
								secondCircle=null;
								clickedCircle=null;
								break;
						}

					}
				}
				catch (IOException ex)
				{
					response = "Error: " + ex;
				}
			}
		}
	}
}
