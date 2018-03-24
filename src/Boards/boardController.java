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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
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
	private BufferedReader in;
	private PrintWriter out;
	private Communicate communicate;
	private String message, nick="", clientNumber, playerTurn;
	private double fX, fY, sX, sY, length, halfLength, futureLength;
	private int i, fi, si, clientsReady, playersNumber, j, client;
	private int move=0, jump=0, oneLength=40, jumpLength=80, betweenLength=55;
	boolean executed, moveable;
	private ArrayList<Color> colorArrayList;
	private ArrayList<Polygon> polygonArrayList;
	private ArrayList<Label> labelArrayList;
	private ArrayList<Circle> goal, blueCheckers, brownCheckers, redCheckers, greenCheckers, whiteCheckers, yellowCheckers;
	private ArrayList<Rectangle> rectangleArrayList;
	private Stage window;

	boardController(BufferedReader in, PrintWriter out, Stage window) throws IOException
	{
		this.in=in;
		this.out=out;
		this.window=window;
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t)
			{
				out.println("exit");
				Platform.exit();
			}
		});;
		clientNumber = in.readLine();
		setState(ClientStatus.UNREADY);
		System.out.println(getState());
		polygonArrayList = new ArrayList<>();
		labelArrayList = new ArrayList<>();
		blueCheckers = new ArrayList<>();
		brownCheckers = new ArrayList<>();
		redCheckers = new ArrayList<>();
		greenCheckers = new ArrayList<>();
		whiteCheckers = new ArrayList<>();
		yellowCheckers = new ArrayList<>();

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
	private AnchorPane game, winner, player1, player2, player3, player4, player5, player6;

	@FXML
	private ArrayList<Circle> circleArrayList;

	@FXML
	private TextField field;

	@FXML
	private TextArea area;

	@FXML
	private Button readyButton;
	
	@FXML
	private Button endOfTurnButton;

	@FXML
	private Polyline blueGoal, brownGoal, redGoal, greenGoal, whiteGoal, yellowGoal;

	@FXML
	private Label labelReady, labelWinner, label1, label2, label3, label4, label5, label6;

	@FXML
	private Rectangle rectangleReady, rectangleWinner, rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6;

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

		for(i=0;i<10;i++)
		{
			blueCheckers.add(circleArrayList.get(i));
		}
		for(i=10;i<20;i++)
		{
			brownCheckers.add(circleArrayList.get(i));
		}
		for(i=20;i<30;i++)
		{
			redCheckers.add(circleArrayList.get(i));
		}
		for(i=30;i<40;i++)
		{
			greenCheckers.add(circleArrayList.get(i));
		}
		for(i=40;i<50;i++)
		{
			whiteCheckers.add(circleArrayList.get(i));
		}
		for(i=50;i<60;i++)
		{
			yellowCheckers.add(circleArrayList.get(i));
		}

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

		for(i=0;i<circleArrayList.size();i++)
		{
			helperCircle = circleArrayList.get(i);
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
		//winner.setVisible(false);
		game.getChildren().remove(winner);
		field.setText("");
		communicate = new Communicate();
		communicate.start();
		for(j=0;j<circleArrayList.size();j++)
		{
			game.getChildren().add(circleArrayList.get(j));
		}
		boardColor=(Color)circleArrayList.get(80).getFill();
		colorArrayList.add(boardColor);
		for(i=0;i<circleArrayList.size();i++)
		{
			circleArrayList.get(i).setFill(boardColor);
		}
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
	private void setEndOfTurn(MouseEvent event)
	{
		if(!firstCircle.equals(null)) {
			firstCircle.setStroke(Color.BLACK);
			firstCircle.setStrokeWidth(1);
		}
	//	out.println("MOVED");
		firstCircle = null;
		clickedCircle=null;
		secondCircle=null;
		move=0;
		jump=0;
		System.out.println("END. MY. TURN.");
		if(checkWin())
		{
			out.println("STATEGAMEWON");
		}
		else
		{
			out.println("MOVEDONE");
		}
		state=ClientStatus.UNTURN;
	}
	
	@FXML
	private void handleCheckers(MouseEvent event)
	{
		clickedCircle = (Circle) event.getSource();
		clickedColor = (Color)clickedCircle.getFill();
		if(state.equals(ClientStatus.TURN))
		{
			if (firstCircle == null  && clickedColor.equals(playerColor) && move==0 && jump==0) {
				firstCircle = clickedCircle;
				firstCircle = (Circle) event.getSource();
				firstCircle.setStroke(Color.GREEN);
				firstCircle.setStrokeWidth(3);
				fX = firstCircle.getLayoutX();
				fY = firstCircle.getLayoutY();
			} else if (clickedCircle.equals(firstCircle)&& move==0 && jump==0) {
				firstCircle.setStroke(Color.BLACK);
				firstCircle.setStrokeWidth(1);
				firstCircle = null;
			} else if(clickedColor.equals(boardColor)){
				sX = clickedCircle.getLayoutX();
				sY = clickedCircle.getLayoutY();
				length = len(sX , sY, fX,fY);
				if(length<oneLength && jump==0 && move==0) {
					moveable=true;
					move=1;
				}
				else if (length<betweenLength)
					moveable=false;
				else if(length <jumpLength) {
					moveable=isMovementPossible(fX, fY, sX, sY, length, circleArrayList);
						if(moveable) {
							jump++;
						}
				}
				else
					moveable=false;
				
				
				if (moveable) 
				{
					movement();
				} else {
					System.out.println("This move is not possible");
					/*firstCircle.setStroke(Color.BLACK);
					firstCircle.setStrokeWidth(1);
					firstCircle = null;*/
				}

			}
		}
	}
	
	
	private boolean isMovementPossible(double x1, double y1, double x2, double y2, double length, ArrayList<Circle> circleArrayList){
		if(move==0) {
			for(int j=0; j<circleArrayList.size(); j++) {
				counterColor=(Color)circleArrayList.get(j).getFill();
				halfLength=len(circleArrayList.get(j).getLayoutX(), circleArrayList.get(j).getLayoutY(), x1, y1);
				if(!(x1==circleArrayList.get(j).getLayoutX()&& y1==circleArrayList.get(j).getLayoutY())&& halfLength<oneLength && futureLength<length) {
					if((!(counterColor.equals(boardColor))) && (((circleArrayList.get(j).getLayoutX()>=x1 && circleArrayList.get(j).getLayoutX()<=x2)||(circleArrayList.get(j).getLayoutX()<=x1 && circleArrayList.get(j).getLayoutX()>=x2)) && ((circleArrayList.get(j).getLayoutY()>=y1 && circleArrayList.get(j).getLayoutY()<=y2)||(circleArrayList.get(j).getLayoutY()<=y1 && circleArrayList.get(j).getLayoutY()>=y2)))){
						return true;
					}
				}
			}
		}
	
		return false;
	}	
 
	/*private boolean isMovementPossible(double x1, double y1, double x2, double y2, double length, ArrayList<Circle> circleArrayList){
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
	}*/
	/*private boolean halfMovement(double x1, double y1, double x2, double y2, double length, ArrayList<Circle> circleArrayList) {
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
	}*/
	private double len(double sX, double sY, double fX, double fY) {
		return Math.sqrt(Math.pow(sX - fX, 2) + Math.pow(sY - fY, 2));
	}
	private void movement() {
		secondCircle = clickedCircle;
		moveColor = (Color) clickedCircle.getFill();

		secondCircle.setFill(firstCircle.getFill());
		secondCircle.setStroke(Color.GREEN);
		secondCircle.setStrokeWidth(3);
		firstCircle.setFill(moveColor);
		firstCircle.setStroke(Color.BLACK);
		firstCircle.setStrokeWidth(1);
		out.println("MOVE"+secondCircle.getLayoutX()+" "+secondCircle.getLayoutY()+" "+firstCircle.getLayoutX()+" "+firstCircle.getLayoutY());
		firstCircle = secondCircle;
		fX = firstCircle.getLayoutX();
		fY = firstCircle.getLayoutY();
		clickedCircle=null;
		secondCircle=null;
	/*	System.out.println("END. MY. TURN.");
		if(checkWin())
		{
			out.println("STATEGAMEWON");
		}
		else
		{
			out.println("MOVEDONE");
		}
		state=ClientStatus.UNTURN;*/

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
								announceWinner(Integer.parseInt(response.substring(0,1)));
								break;

						}
					}
					else if(response.substring(0,6).equals("PLAYER"))
					{
						response=response.substring(6);
						switch(response.substring(0,1))
						{
							case "1":
								setLabel(label1, response.substring(1));
								setCheckers(blueGoal, blueCheckers, 0);
								setPlayers(response.substring(0,1));
								break;
							case "2":
								setLabel(label2, response.substring(1));
								setCheckers(brownGoal, brownCheckers, 1);
								setPlayers(response.substring(0,1));
								break;
							case "3":
								setLabel(label3, response.substring(1));
								setCheckers(redGoal, redCheckers, 2);
								setPlayers(response.substring(0,1));
								break;
							case "4":
								setLabel(label4, response.substring(1));
								setCheckers(greenGoal, greenCheckers, 3);
								setPlayers(response.substring(0,1));
								break;
							case "5":
								setLabel(label5, response.substring(1));
								setCheckers(whiteGoal, whiteCheckers, 4);
								setPlayers(response.substring(0,1));
								break;
							case "6":
								setLabel(label6, response.substring(1));
								setCheckers(yellowGoal, yellowCheckers, 5);
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
							case "DONE":
								firstCircle=null;
								secondCircle=null;
								clickedCircle=null;
								break;
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
								moveColor=(Color)firstCircle.getFill();
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
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				label.setText(response);
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

	private void setCheckers(Polyline polyline, ArrayList<Circle> arrayList, int number)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				int k;
				polyline.setFill(colorArrayList.get(number));
				for(k=0;k<arrayList.size();k++)
				{
					arrayList.get(k).setFill(colorArrayList.get(number));
				}
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
			switch (i)
			{
				case 1:
					setCheckers(brownGoal, brownCheckers, 6);
					break;
				case 2:
					setCheckers(redGoal, redCheckers, 6);
					break;
				case 3:
					setCheckers(greenGoal, greenCheckers, 6);
					break;
				case 4:
					setCheckers(whiteGoal, whiteCheckers, 6);
					break;
				case 5:
					setCheckers(yellowGoal, yellowCheckers, 6);
					break;

			}
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

	private void announceWinner(int player)
	{
		//labelWinner.setText("Player #"+player);
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				game.getChildren().add(winner);
				rectangleWinner.setFill(colorArrayList.get(player));
				winner.setVisible(true);
			}
		});
		setLabel(labelWinner, "Player #"+(player+1));
	}

	private ClientStatus getState()
	{
		return state;
	}

	private void setState(ClientStatus state)
	{
		this.state = state;
	}

}
