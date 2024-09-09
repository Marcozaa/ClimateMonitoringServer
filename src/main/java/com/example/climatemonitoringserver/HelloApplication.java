package com.example.climatemonitoringserver;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.ServerSocket;
import java.net.Socket;

public class HelloApplication extends Application {

    private Database dataBase;
    public static final int PORT = 2345;
    private ServerSocket ss = null;


    @FXML
    private TextField dbNameInput, dbUsername, dbPassword;
    @FXML
    private Button dbInvioDati;

    Font font = Font.loadFont(getClass().getResourceAsStream("/font/Montserrat-VariableFont_wght.ttf"), 12);

    @FXML
    private void invioDatiDB(){
        String nomeDatabase = dbNameInput.getText();
        String usernameDB = dbUsername.getText();
        String passwordDB = dbPassword.getText();

        dataBase = new Database(nomeDatabase, usernameDB, passwordDB);
        //ClimateMonitoringApp
        //postgres
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Server ready");
            while (true) {
                Socket cliSocket = ss.accept();
                System.out.println("Client connected: socket = " + cliSocket);
                new Thread(new ServerThread(cliSocket, dataBase)).start();
                //cliSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void start(Stage stage) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader);
        stage.setTitle("Hello!");


        Label label1 = new Label("Name:");
        TextField textField = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField);
        hb.setSpacing(10);


        stage.setScene(scene);


        stage.show();

    }

    public void initialize() {

    }



    public static void main(String[] args) {
        launch();
    }
}