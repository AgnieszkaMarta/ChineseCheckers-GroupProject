package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class GUI extends Application
{
    Stage window;
    Parent root;
    Scene startScene;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        window = primaryStage;
        root = FXMLLoader.load(getClass().getResource("/Boards/start.fxml"));
        startScene = new Scene(root, 614, 412);
        window.setTitle("Chinese Checker");
        window.setScene(startScene);
        window.show();
    }

    public Stage getWindow()
    {
        return window;
    }
}
