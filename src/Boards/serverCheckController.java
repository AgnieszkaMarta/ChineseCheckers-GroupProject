package Boards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class serverCheckController
{
	private Stage window;
	private String nick, serverAddress, test;
	private BufferedReader in;
	private PrintWriter out;
	private Object input;

	@FXML
	private Button play;

	@FXML
	private TextField ip;

	@FXML
	private TextField username;

	@FXML
	private void handleButtonBack(MouseEvent event)
	{

		window = ((Stage) (((Button) event.getSource()).getScene().getWindow()));

		try
		{
			Parent root = FXMLLoader.load(getClass().getResource("/Boards/start.fxml"));

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
	private void handleButtonNext(MouseEvent event)
	{
		serverAddress=ip.getText();
		nick=username.getText();

		if(nick.length()!=0 && serverAddress.length()!=0)
		{
			try
			{
				if(in == null && out == null)
				{

					Socket socket = new Socket(serverAddress, 6969);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					System.out.println(in);
					System.out.println(out);

				}
			}
			catch (IOException exception)
			{
				ip.setText("Incorrect server address.");
			}
			if(validateNick()==true)
			{
				window = ((Stage) (((Button) event.getSource()).getScene().getWindow()));

				try
				{
					FXMLLoader root = new FXMLLoader(getClass().getResource("/Boards/board.fxml"));
					boardController controller = new boardController(in, out);
					root.setController(controller);
					window.setTitle("Chinese Checker");
					window.setScene(new Scene(root.load(), 1117, 691));
					window.show();

				}
				catch (IOException e2)
				{
					e2.printStackTrace();
				}
			}
			else
			{
				username.setText("Username already in use.");
			}
		}
		if(nick.length()==0)
		{
			username.setText("Incorrect username.");
		}
		if(serverAddress.length()==0)
		{
			ip.setText("Incorrect server address.");
		}

	}

	private boolean validateNick()
	{
		try
		{
			System.out.println(2);
			out.println(nick);
			System.out.println(2);

			test=in.readLine();
			System.out.println(2);
			System.out.println(test);
			if(test.equals("PASSED"))
			{
				return true;
			}
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return false;
	}

}