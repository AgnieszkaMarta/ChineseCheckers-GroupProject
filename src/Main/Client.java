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
    private GUI gui;
    String nick;

    public Client()
    {
        Application.launch(GUI.class);
    }

    public String getNick()
    {
        return nick;
    }
}