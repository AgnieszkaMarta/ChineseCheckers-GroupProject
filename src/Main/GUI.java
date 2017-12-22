package Main;

import javafx.application.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

import static javafx.application.Application.launch;

public class GUI extends Application
{

    @Override
    public void start(Stage stage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("/boards/start.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/boards/start.fxml"));

        stage.setTitle("Chinese Checker");
        stage.setScene(new Scene(root, 704, 464));
        stage.show();
    }
}
