package Boards;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.lang.Math;

import javafx.event.EventHandler;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

public class boardController implements Initializable
{
	private ClientStatus state;
	private Circle firstCircle, secondCircle, clickedCircle, helperCircle;
	private Color counterColor, boardColor, playerColor, clickedColor, moveColor, helperColor;
	private RadialGradient counterColor1;
	private LinearGradient counterColor2;
	private BufferedReader in;
	private PrintWriter out;
	private Communicate communicate;
	private String message, nick="", clientNumber, playerTurn;
	private double fX, fY, sX, sY, length, halfLength, futureLength;
	private int i, fi, si, clientsReady, playersNumber, j, client;
	boolean executed, moveable;
	private ArrayList<Color> colorArrayList;
	private ArrayList<Polygon> polygonArrayList;
	private ArrayList<Label> labelArrayList;
	private ArrayList<Circle> goal;
	private ArrayList<Rectangle> rectangleArrayList;
	private Stage window;

	boardController(BufferedReader in, PrintWriter out, Stage window) throws IOException
	{
		this.in=in;
		this.out=out;
		this.window=window;
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				out.println("exit");
				Platform.exit();
			}
		});;
		clientNumber = in.readLine();
		setState(ClientStatus.UNREADY);
		System.out.println(getState());
		polygonArrayList = new ArrayList<>();
		labelArrayList = new ArrayList<>();
		goal = new ArrayList<>();

		clientsReady=0;
		colorArrayList = new ArrayList<>();
		colorArrayList.add(DODGERBLUE);
		colorArrayList.add(BROWN);
		colorArrayList.add(RED);
		colorArrayList.add(GREEN);
		colorArrayList.add(WHITE);
		colorArrayList.add(YELLOW);
		client = Integer.parseInt(clientNumber);
		if(client<7)
		{
			playerColor = colorArrayList.get(client-1);
		}
	}

	@FXML
	private AnchorPane game, player1, player2, player3, player4, player5, player6;

	@FXML
	private ArrayList<Circle> circleArrayList;

	@FXML
	private TextField field;

	@FXML
	private TextArea area;

	@FXML
	private Button readyButton;

	@FXML
	private Label labelReady, label1, label2, label3, label4, label5, label6;

	@FXML
	private Rectangle rectangleReady, rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6;

	@FXML
	private Polygon polygon1, polygon2, polygon3, polygon4, polygon5, polygon6, polygonhelper;

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		polygonArrayList.add(polygon1);
		polygonArrayList.add(polygon2);
		polygonArrayList.add(polygon3);
		polygonArrayList.add(polygon4);
		polygonArrayList.add(polygon5);
		polygonArrayList.add(polygon6);

		labelArrayList.add(label1);
		labelArrayList.add(label2);
		labelArrayList.add(label3);
		labelArrayList.add(label4);
		labelArrayList.add(label5);
		labelArrayList.add(label6);

		for(i=0;i<circleArrayList.size();i++)
		{
			helperCircle = circleArrayList.get(i);
			switch (client)
			{
				case 1:
					helperColor = colorArrayList.get(1);
					break;
				case 2:
					helperColor = colorArrayList.get(0);
					break;
				case 3:
					helperColor = colorArrayList.get(3);
					break;
				case 4:
					helperColor = colorArrayList.get(2);
					break;
				case 5:
					helperColor = colorArrayList.get(5);
					break;
				case 6:
					helperColor = colorArrayList.get(4);
					break;
			}
			if(helperCircle.getFill().equals(helperColor))
			{
				goal.add(helperCircle);
			}
		}

		for(i=0;i<polygonArrayList.size();i++)
		{
			polygonArrayList.get(i).setVisible(false);
		}

		area.appendText(nick);
		area.setEditable(false);
		field.setText("");
		communicate = new Communicate();
		communicate.start();
		for(j=0;j<circleArrayList.size();j++)
		{
			game.getChildren().add(circleArrayList.get(j));
		}
		boardColor=(Color)circleArrayList.get(80).getFill();
	}

	@FXML
	private void sendMessage(ActionEvent event)
	{
		message=field.getText();
		if(!message.equals(""))
			out.println("MESSAGE"+message);
		field.setText("");
	}

	@FXML
	private void setReady(MouseEvent event)
	{
		if(client<7)
		{
			setState(ClientStatus.READY);
			out.println("STATE"+state);
		}
		else
		{
			readyButton.setDisable(true);
		}
		readyButton.setDisable(true);
	}

	@FXML
	private void handleCheckers(MouseEvent event)
	{
		clickedCircle = (Circle) event.getSource();
		clickedColor = (Color)clickedCircle.getFill();
		if(state.equals(ClientStatus.TURN))
		{
			if (firstCircle == null  && clickedColor.equals(playerColor)) {

				firstCircle = clickedCircle;
				firstCircle = (Circle) event.getSource();
				firstCircle.setStroke(Color.GREEN);
				firstCircle.setStrokeWidth(3);
				fX = firstCircle.getLayoutX();
				fY = firstCircle.getLayoutY();
			} else if (clickedCircle.equals(firstCircle)) {
				firstCircle.setStroke(Color.BLACK);
				firstCircle.setStrokeWidth(1);
				firstCircle = null;
			} else if(clickedColor.equals(boardColor)){
				sX = clickedCircle.getLayoutX();
				sY = clickedCircle.getLayoutY();
				length = len(sX , sY, fX,fY);
				if(length<40) {
					moveable=true;
				}
				else if (length<57)
					moveable=false;
				else if(length <75)
					moveable=isMovementPossible(fX, fY, sX, sY, length, circleArrayList);


				if (moveable) {
					movement();
				} else {
					System.out.println("This move is not possible");
					firstCircle.setStroke(Color.BLACK);
					firstCircle.setStrokeWidth(1);
					firstCircle = null;
				}

			}
		}
	}


	private boolean isMovementPossible(double x1, double y1, double x2, double y2, double length, ArrayList<Circle> circleArrayList){
		for(int j=0; j<circleArrayList.size(); j++) {
			counterColor=(Color)circleArrayList.get(j).getFill();
			halfLength=len(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x1, y1);
			futureLength=len(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x2, y2);
			if(!(x1==circleArrayList.get(j).getLayoutX()&& y1==circleArrayList.get(j).getLayoutY())&& halfLength<35 && futureLength<length) {
				if((!(counterColor.equals(boardColor))) && (((circleArrayList.get(j).getLayoutX()>=x1 && circleArrayList.get(j).getLayoutX()<=x2)||(circleArrayList.get(j).getLayoutX()<=x1 && circleArrayList.get(j).getLayoutX()>=x2)) && ((circleArrayList.get(j).getLayoutY()>=y1 && circleArrayList.get(j).getLayoutY()<=y2)||(circleArrayList.get(j).getLayoutY()<=y1 && circleArrayList.get(j).getLayoutY()>=y2)))){
					if(length>75) {
						length=futureLength;
						return halfMovement(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x2, y2, length, circleArrayList );
					}
					else
						return true;
				}
			}
		}
		return false;
	}
	private boolean halfMovement(double x1, double y1, double x2, double y2, double length, ArrayList<Circle> circleArrayList) {
		for(int j=0; j<circleArrayList.size(); j++) {
			counterColor=(Color)circleArrayList.get(j).getFill();
			halfLength=len(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x1, y1);
			futureLength=len(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x2, y2);
			if((!(x2==circleArrayList.get(j).getLayoutX()&& y2==circleArrayList.get(j).getLayoutY()))&& halfLength<35 && futureLength<length) {
				if((counterColor.equals(boardColor)) && (((circleArrayList.get(j).getLayoutX()>=x1 && circleArrayList.get(j).getLayoutX()<=x2)||(circleArrayList.get(j).getLayoutX()<=x1 && circleArrayList.get(j).getLayoutX()>=x2)) && ((circleArrayList.get(j).getLayoutY()>=y1 && circleArrayList.get(j).getLayoutY()<=y2)||(circleArrayList.get(j).getLayoutY()<=y1 && circleArrayList.get(j).getLayoutY()>=y2)))){
					length=futureLength;
					return isMovementPossible(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x2, y2, length, circleArrayList );
				}
			}
		}
		return false;
	}
	private double len(double sX, double sY, double fX, double fY) {
		return Math.sqrt(Math.pow(sX - fX, 2) + Math.pow(sY - fY, 2));
	}
	private void movement() {
		secondCircle = clickedCircle;
		moveColor = (Color) clickedCircle.getFill();

		secondCircle.setFill(firstCircle.getFill());
		firstCircle.setFill(moveColor);
		firstCircle.setStroke(Color.BLACK);
		firstCircle.setStrokeWidth(1);
		out.println("MOVE"+secondCircle.getLayoutX()+" "+secondCircle.getLayoutY()+" "+firstCircle.getLayoutX()+" "+firstCircle.getLayoutY());
		firstCircle = null;
		clickedCircle=null;
		secondCircle=null;
		System.out.println("END. MY. TURN.");
		if(checkWin()==true)
		{
			out.println("STATEGAMEWON");
		}
		out.println("MOVEDONE");
		state=ClientStatus.UNTURN;
	}


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

	public class Communicate extends Thread
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
								setState(ClientStatus.UNTURN);
								break;
							case "TURN":
								executed=false;
								setState(ClientStatus.TURN);
								break;
							default:
								response=response.substring(7);
								break;

						}
					}
					else if(response.substring(0,6).equals("PLAYER"))
					{
						response=response.substring(6);
						switch(response.substring(0,1))
						{
							case "1":
								setLabel(label1, response);
								setPlayers(response.substring(0,1));
								break;
							case "2":
								setLabel(label2, response);
								setPlayers(response.substring(0,1));
								break;
							case "3":
								setLabel(label3, response);
								setPlayers(response.substring(0,1));
								break;
							case "4":
								setLabel(label4, response);
								setPlayers(response.substring(0,1));
								break;
							case "5":
								setLabel(label5, response);
								setPlayers(response.substring(0,1));
								break;
							case "6":
								setLabel(label6, response);
								setPlayers(response.substring(0,1));
								break;
							case "S":
								playersNumber = Integer.parseInt(response.substring(1));
								if(playersNumber>6)
								{
									playersNumber=6;
								}
								setPlayersReady();
								break;
							case "R":
								clientsReady = Integer.parseInt(response.substring(1));
								setPlayersReady();
								break;
							case "T":
								playerTurn = response.substring(1);
								System.out.println(playerTurn);
								showPlayer();
								break;
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
									moveColor=(Color)firstCircle.getFill();
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
								firstCircle.setFill(secondCircle.getFill());
								secondCircle.setFill(moveColor);

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

	private void setLabel(Label label, String response)
	{
		String labelHelper;
		labelHelper = response.substring(1);
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				label.setText(labelHelper);
				label.setTextAlignment(TextAlignment.CENTER);
			}
		});
	}

	private void setPlayersReady()
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				labelReady.setText("Players ready: "+clientsReady+"/"+playersNumber);
			}
		});
	}

	private void showPlayer()
	{
		String helper;
		for(i=0;i<6;i++)
		{
			polygonhelper = polygonArrayList.get(i);
			helper = Integer.toString(Integer.parseInt(polygonhelper.getId().substring(7))-1);
			if(helper.equals(playerTurn))
			{
				polygonhelper.setVisible(true);
			}
			else
			{
				polygonhelper.setVisible(false);
			}
		}
	}

	private void setPlayers(String string)
	{
		for (i=Integer.parseInt(string);i<6;i++)
		{
			setLabel(labelArrayList.get(i), " Player #"+(i+1));
		}
	}

	private boolean checkWin()
	{
		int k;
		for(k=0; k<goal.size();k++)
		{
			if(!goal.get(k).getFill().equals(playerColor))
			{
				return false;
			}
		}
		return true;
	}

	private void announceWinner()
	{

	}

	public ClientStatus getState()
	{
		return state;
	}

	public void setState(ClientStatus state)
	{
		this.state = state;
	}

}
