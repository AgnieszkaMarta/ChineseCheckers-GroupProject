package Main;

import javafx.application.Application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client
{

    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;

    Client()
    {
        Application.launch(GUI.class);
    }
}