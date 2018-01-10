package Main;

import Boards.ClientStatus;
import javafx.scene.layout.AnchorPane;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import static Boards.ClientStatus.*;

public class Server
{
    private static ArrayList<Player> players;
    private static ArrayList<Bot> bots;
    private static int randomClient, clientsReady=0, realPlayers;
    private static Bot bot;

    public static void main(String[] args) throws Exception
    {
        Player player;
        players = new ArrayList<>();
        bots = new ArrayList<>();
        bot = new Bot();
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
        private int clientNumber, i, j;
        private BufferedReader in;
        private PrintWriter out;
        private String nick, nickname, input;
        private ClientStatus state;
        private Player player;
        private Random generator;
        private Boolean passed;

        Player(Socket socket, int clientNumber)
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
                setState(UNREADY);
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
                while(!validateNickname())
                {
                    out.println("not passed");
                    input=in.readLine();
                    if(input.substring(0,4).equals("NICK"))
                    {
                        nick=input.substring(4);
                    }
                    else
                    {
                        players.remove(this);
                        passed=false;
                        break;
                    }
                }
                if(passed)
                {
                    nickname = nick;
                    out.println("PASSED");
                    out.println(players.size());
                    sendPlayers();
                    for (i = 0; i < players.size(); i++)
                    {
                        players.get(i).getOut().println("MESSAGE" + "+ " + getNickname() + " comes.");
                    }
                    while (true)
                    {
                        input = in.readLine();
                        System.out.println(input);
                        if (input.equals("exit"))
                        {
                            playerExit();
                            break;
                        }
                        else if (input.substring(0, 7).equals("MESSAGE"))
                        {
                            input = input.substring(7);
                            for (i = 0; i < players.size(); i++)
                            {
                                players.get(i).getOut().println("MESSAGE" + nick + ": " + input);
                            }
                        }
                        else if (input.substring(0, 5).equals("STATE"))
                        {
                            input = input.substring(5);
                            switch (input)
                            {
                                case "READY":
                                    setState(READY);
                                    clientsReady++;
                                    for (i = 0; i < players.size(); i++)
                                    {
                                        players.get(i).out.println("PLAYERR" + clientsReady);
                                    }
                                    if (checkReady()) {
                                        startGame();
                                    }
                                    break;
                                case "GAMEWON":
                                    for (i = 0; i < players.size(); i++)
                                    {
                                        players.get(i).out.println("STATEGAMEWON" +players.indexOf(this)+getNickname());
                                    }
                                    break;
                            }
                        }
                        else if (input.substring(0, 4).equals("MOVE"))
                        {
                            input = input.substring(4);
                            switch (input)
                            {
                                case "DONE":
                                    handleTurn();
                                    break;
                                default:
                                    sendMove();
                                    break;
                            }

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
            realPlayers = players.size();
            if(realPlayers>6)
            {
                realPlayers=6;
            }
            for(i=0; i<realPlayers;i++)
            {
                if(!players.get(i).getClientState().equals(READY))
                {
                    return false;
                }
                System.out.println("aktulanie: "+i+" na tyle: "+6%players.size()+" a jest tylu: "+players.size()+" "+players.get(i).getClientState());
            }
            for(i=0;i<players.size();i++)
            {
                players.get(i).out.println("PLAYERR"+clientsReady);
            }
            return true;
        }

        private void startGame()
        {
            randomClient = generator.nextInt(players.size()%6);
            for(i=0; i<players.size(); i++)
            {
                player=players.get(i);
                player.setState(UNTURN);
                player.out.println("STATEGAMESTARTED");
                player.out.println("STATE"+player.getClientState());
                player.out.println("PLAYERT"+randomClient);
            }
            players.get(randomClient).getOut().println("STATE"+TURN);
        }

        private void sendPlayers()
        {
            for(i=0;i<players.size();i++)
            {
                player=players.get(i);
                for(j=0;j<players.size();j++)
                {
                    player.out.println("PLAYER"+(j+1)+players.get(j).getNickname());
                }
                player.out.println("PLAYERS"+players.size());
                player.out.println("PLAYERR"+clientsReady);
            }
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
            passed = true;
            return true;
        }

        private void handleTurn()
        {
            setState(UNTURN);
            randomClient=((randomClient+1)%(players.size()))%6;
            System.out.println(randomClient);
            player=players.get(randomClient);
            player.setState(TURN);
            player.getOut().println("STATE"+player.getClientState());
            for(i=0; i<players.size(); i++)
            {
                player=players.get(i);
                player.out.println("PLAYERT"+randomClient);
            }
        }

        private void playerExit()
        {
            if(getClientState().equals(TURN))
            {
                handleTurn();
            }
            if(getClientState().equals(READY) || getClientState().equals(TURN) || getClientState().equals(UNTURN))
            {
                clientsReady--;
            }
            for(i=0; i<players.size();i++)
            {
                players.get(i).getOut().println("MESSAGE"+"- "+getNickname()+" leaves.");
            }
            players.remove(this);
            //players.set(players.indexOf(this), null);
            sendPlayers();
        }

        public void setState(ClientStatus state)
        {
            this.state = state;
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

    /*public static class Bot() extends Thread
    {

    }*/
}