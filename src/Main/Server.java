package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static ArrayList<Socket> socketList = new ArrayList<Socket>();

    public static void main(String[] args) throws Exception
    {

        System.out.println("The server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(6969);
        try {
            while (true)
            {
                new Player(listener.accept(), clientNumber++).start();
            }
        }
        finally
        {
            listener.close();
        }
    }

    private static class Player extends Thread
    {
        private Socket socket;
        private int clientNumber;

        public Player(Socket socket, int clientNumber)
        {
            this.socket = socket;
            socketList.add(socket);
            this.clientNumber = clientNumber;
            log("New connection with player# " + clientNumber + " at " + socket);
        }

        public void run()
        {
            try
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are player#" + clientNumber + ".");
                out.println("Enter exit to quit.\n");

                while (true)
                {
                    String input = in.readLine();
                    if (input.equals("exit"))
                    {
                        break;
                    }
                    for(int i=0;i<socketList.size();i++)
                    {
                        //System.out.println(input);
                        PrintWriter out1 = new PrintWriter(socketList.get(i).getOutputStream(), true);
                        out1.println("player#"+clientNumber+": "+input);
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
    }
}