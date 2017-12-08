package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Playertest extends Thread
{
    private Socket socket;
    private int clientNumber;
    private String nickname;
    private BufferedReader in;
    private PrintWriter out;
    private String input;
    private String nick;

    public Playertest(Socket socket, int clientNumber)
    {
        this.socket = socket;
        this.clientNumber = clientNumber;
        //this.nickname=();
        log("New connection with player# " + clientNumber + " at " + socket);
    }

    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            nick=(in.readLine());

            // Send a welcome message to the client.
            out.println("Hello, " + nick + "!");
            out.println("Enter exit to quit.\n");

            while (true)
            {
                input = in.readLine();
                log(input);

                if (input.equals("exit"))
                {
                    break;
                }
            }
        }
        catch (IOException e)
        {
            log("Error handling client# " + clientNumber + ": " + e);
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                log("Couldn't close a socket, what's going on?");
            }
            log("Connection with client# " + clientNumber + " closed");
        }
    }

    private void log(String message)
    {
        System.out.println(message);
    }

    public Socket getSocket()
    {
        return socket;
    }

    public int getClientNumber()
    {
        return clientNumber;
    }

    public String getNickname()
    {
        return nickname;
    }
}
