package boards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class startController
{
	private Stage window;
	private String nick, serverAddress, test;
	private BufferedReader in;
	private PrintWriter out;

	@FXML
	private Button play;

	@FXML
	private TextField ip;

	@FXML
	private TextField username;

	@FXML
	private void handleIPLabel()
	{

	}

    @FXML
    private void handleButtonPlay(MouseEvent event)
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
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);
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
					Parent root = FXMLLoader.load(getClass().getResource("/boards/board.fxml"));

					window.setTitle("Chinese Checker");
					window.setScene(new Scene(root, 600, 400));
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
			out.println(nick);
			test=in.readLine();
			System.out.println(test);
			if(test.equals("PASSED"))
			{
				return true;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public BufferedReader getIn()
	{
		return in;
	}

	public PrintWriter getOut()
	{
		return out;
	}
}
