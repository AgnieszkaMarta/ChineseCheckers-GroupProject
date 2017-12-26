package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

import javafx.application.Application;

public class Client extends Thread
{

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Client");
    private JTextField dataField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 60);
    private GUI gui;
    String response;
    String nick;
    String serverAddress;

    public Client()
    {

        try
        {
            serverAddress = JOptionPane.showInputDialog(
                    frame,
                    "Enter IP Address of the Server:",
                    "Chinese Checkers",
                    JOptionPane.QUESTION_MESSAGE);

            // Make connection and initialize streams
            Socket socket = new Socket(serverAddress, 6969);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            do
            {
                nick = JOptionPane.showInputDialog(
                        frame,
                        "Enter Your username:",
                        "Chinese Checkers",
                        JOptionPane.QUESTION_MESSAGE);
                if(nick.length()!=0)
                {
                    out.println(nick);
                }
            }
            while(nick.length()==0);
            Application.launch(GUI.class);

            for (int i = 0; i < 3; i++) {
               // gui.chat.messageArea.append(in.readLine() + "\n");
            }

        }

        catch (IOException e)
        {

        }

    }


    @Override
    public void run()
    {


        while(true)
        {
            try
            {
                response = in.readLine();
                if (response == null || response.equals("")) {
                    System.exit(0);
                }
            } catch (IOException ex) {
                response = "Error: " + ex;
            }
            System.out.println(response);
            //gui.chat.messageArea.append(response + "\n");

        }
    }

    public String getNick()
    {
        return nick;
    }
}