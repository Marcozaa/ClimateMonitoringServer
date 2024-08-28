package com.example.climatemonitoringserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerThread implements Runnable {
    private Socket socket;
    private Database database;

    public ServerThread(Socket socket, Database database) {
        this.socket = socket;
        this.database = database;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            String command = (String) in.readObject();
            switch (command) {
                case "searchData":
                    String query = (String) in.readObject();  // da modificare
                    ResultSet rs = database.searchData(query);
                    out.writeObject(rs);
                    break;
                case "insertMonitoringCenterData":
                    System.out.println("Ricevuto comando insertMonitoringCenterData");
                    String data = (String) in.readObject();     //vedere come inserire i dati
                    database.insertMonitoringCenterData(data);
                    out.writeObject("Data inserted");
                    break;
                case "registerOperator":
                    System.out.println("Ricevuto comando registerOperator");
                    String username = (String) in.readObject();   //mettere tutti i dati dell'operatore
                    String password = (String) in.readObject();
                    database.registerOperator(username, password);
                    out.writeObject("Operator registered");
                    break;
                case "insertAreaOfInterest":
                    System.out.println("Ricevuto comando insertAreaOfInterest");
                    String nome = (String) in.readObject();   //gestire come funziona l'inserimento

                    String stato = (String) in.readObject();
                    double latitudine = (double) in.readObject();
                    double longitudine = (double) in.readObject();
                    System.out.println("Nome: " + nome + " Stato: " + stato + " Latitudine: " + latitudine + " Longitudine: " + longitudine);
                    database.insertAreaOfInterest(nome, stato, latitudine, longitudine);
                    out.writeObject("Area of interest inserted");
                    break;
                case "validateUser":
                    System.out.println("Ricevuto comando validateUser");
                    String user = (String) in.readObject();
                    String pass = (String) in.readObject();
                    boolean isValid = database.validateUser(user, pass);
                    System.out.println("is valid " + isValid);
                    out.writeObject(isValid);
                    break;
                case "insertClimateParameters":
                    System.out.println("Ricevuto comando insertClimateParameters");
                    String parameters = (String) in.readObject();  // gestire inserimento
                    database.insertClimateParameters(parameters);
                    out.writeObject("Climate parameters inserted");
                    break;
                case "viewClimateParameters":
                    System.out.println("Ricevuto comando viewClimateParameters");
                    ResultSet climateRs = database.viewClimateParameters();
                    out.writeObject(climateRs);
                    break;
                default:
                    out.writeObject("Unknown command");
                    break;
            }
        } catch (IOException | ClassNotFoundException  e) {
            e.printStackTrace();
        }
    }
}
