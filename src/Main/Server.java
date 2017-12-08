package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{

    private static ArrayList<Player> players = new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        Player player;
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(6969);
        Socket client;
        try {
            while (true)
            {
                client = listener.accept();
                player = new Player(client, players.size());
                player.start();
                players.add(player);

            }
        }
        finally
        {
            listener.close();
        }
    }

    public static class Player extends Thread
    {
        private Socket socket;
        private int clientNumber;
        private String nickname;
        private BufferedReader in;
        private PrintWriter out;
        private String input;
        private String nick;

        public Player(Socket socket, int clientNumber)
        {
            this.socket = socket;
            this.clientNumber = clientNumber;
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
                out.println("Enter exit to quit.\n---------------------------");

                while (true)
                {
                    input = in.readLine();
                    log(input);

                    if (input.equals("exit"))
                    {
                        players.remove(this);
                        break;
                    }
                    for(int i=0; i<players.size();i++)
                    {
                        players.get(i).getOut().println(nick+": "+input);
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

        public PrintWriter getOut()
        {
            return out;
        }
    }

}