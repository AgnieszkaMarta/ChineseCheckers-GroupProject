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

    private enum State
    {
        Validate_Name;
    }

    private int k=0;

    public static void main(String[] args) throws Exception
    {
        Player player;
        System.out.println("The server is running.");
        ServerSocket listener = new ServerSocket(6969);
        Socket client;
        try
        {
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
        private int clientNumber, i;
        private BufferedReader in;
        private PrintWriter out;
        private String input, nick, nickname;

        public Player(Socket socket, int clientNumber)
        {
            try
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                this.socket = socket;
                this.clientNumber = clientNumber;
                log("New connection with player# " + clientNumber + " at " + socket);
                nick=(in.readLine());
                //log(nick);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try
            {
                while(validateNickname()==false)
                {

                    out.println("NOT PASSED");
                    nick=(in.readLine());
                }
                nickname=nick;
                out.println("PASSED");

                while (true)
                {
                    input = in.readLine();
                    //log(input);

                    if (input.equals("exit"))
                    {
                        players.remove(this);
                        break;
                    }
                    for(i=0; i<players.size();i++)
                    {
                        players.get(i).getOut().println(nick+": "+input);
                    }
                }
            }
            catch (IOException e)
            {
                players.remove(this);
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

        private boolean validateNickname()
        {
            for(int i=0; i<players.size();i++)
            {
                if(nick.equals(players.get(i).getNickname()))
                {
                    System.out.println(nick+" "+i);
                    return false;
                }
            }
            return true;
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