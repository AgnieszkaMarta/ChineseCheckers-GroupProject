package Main;

import javafx.application.Application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Client
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

        /*try
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


            for (int i = 0; i < 3; i++) {
               // gui.chat.messageArea.append(in.readLine() + "\n");
            }

        }

        catch (IOException e)
        {

        }*/

        Application.launch(GUI.class);



    }

    public String getNick()
    {
        return nick;
    }
}