package Main;

import Boards.ClientStatus;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server
{
    private static ArrayList<Player> players = new ArrayList<>();

    private static int randomClient;

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
        private String nick, nickname, input;
        public ClientStatus state;
        private Player player;
        Random generator;

        public Player(Socket socket, int clientNumber)
        {
            try
            {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                generator = new Random();
                System.out.println(in);
                System.out.println(out);
                this.socket = socket;
                this.clientNumber = clientNumber;
                state = ClientStatus.UNREADY;
                log("New connection with player# " + clientNumber + " at " + socket);
                nick=in.readLine();
                log(nick);
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

                    //out.println("NOT PASSED");
                    nick=in.readLine();
                }
                nickname=nick;
                out.println("PASSED");
                out.println(players.size());
                while (true)
                {
                    input = in.readLine();
                    //log(input);
                    //pane=in.read();
                    System.out.println(input);
                    if (input.equals("exit"))
                    {
                        players.remove(this);
                        break;
                    }
                    else if(input.substring(0,7).equals("MESSAGE"))
                    {
                        input=input.substring(7);
                        for(i=0; i<players.size();i++)
                        {
                            players.get(i).getOut().println("MESSAGE"+nick+": "+input);
                        }
                    }
                    else if(input.substring(0,5).equals("STATE"))
                    {
                        input=input.substring(5);
                        switch (input)
                        {
                            case "READY":
                                state = ClientStatus.READY;
                                if(checkReady()==true)
                                {
                                    startGame();
                                }
                                break;

                        }
                    }
                    else if(input.substring(0,4).equals("MOVE"))
                    {
                        input=input.substring(4);
                        switch (input)
                        {
                            case "DONE":
                                state=ClientStatus.UNTURN;
                                randomClient=(randomClient+1)%(players.size());
                                player=players.get(randomClient);
                                player.state=ClientStatus.TURN;
                                player.getOut().println("STATE"+player.state);
                                break;
                            default:
                                sendMove();
                                break;
                        }

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

        private boolean checkReady()
        {
            for(i=0; i<players.size();i++)
            {
                if(!players.get(i).getClientState().equals(ClientStatus.READY))
                {
                    return false;
                }
            }
            return true;
        }

        private void startGame()
        {
            randomClient = generator.nextInt(players.size());
            for(i=0; i<players.size(); i++)
            {
                player=players.get(i);
                player.state=ClientStatus.UNTURN;
                player.out.println("STATEGAMESTARTED");
                player.out.println("STATE"+player.getClientState());
            }
            players.get(randomClient).getOut().println("STATE"+ClientStatus.TURN);
        }

        private void sendMove()
        {
            for(i=0; i<players.size(); i++)
            {
                player=players.get(i);
                if(!player.equals(this))
                {
                    player.getOut().println("MOVE"+input);
                }
            }
        }

        private boolean validateNickname()
        {
            for(i=0; i<players.size();i++)
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

        public ClientStatus getClientState()
        {
            return state;
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