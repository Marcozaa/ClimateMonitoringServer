package com.example.climatemonitoringserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import climatemonitoringserver.AreaInteresse;



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
            while(true) {
                String command = (String) in.readObject();
                switch (command) {
                    case "searchData":
                        String query = (String) in.readObject();  // da modificare
                        ResultSet rs = database.searchData(query);
                        out.writeObject(rs);
                        break;
                    case "insertMonitoringCenterData":
                        System.out.println("Ricevuto comando insertMonitoringCenterData");
                        String nomeCentro = (String) in.readObject();
                        String via = (String) in.readObject();
                        int cap = (int) in.readObject();
                        int numeroCivico = (int) in.readObject();
                        String comune = (String) in.readObject();
                        String provincia = (String) in.readObject();
                        //int idAreaInteresse = (int) in.readObject();
                        ArrayList<String> citta = (ArrayList<String>) in.readObject();
                        database.insertMonitoringCenterData(nomeCentro, via, cap, numeroCivico, comune, provincia, citta);
                        //out.writeObject("Data inserted");
                        break;
                    case "insertMonitoringCenterDataUser":
                        System.out.println("Ricevuto comando insertMonitoringCenterDataUser");
                        String nomeCentroUser = (String) in.readObject();
                        String nomeUser = (String) in.readObject();
                        database.insertMonitoringCenterDataUser(nomeCentroUser, nomeUser);
                        //out.writeObject("Data inserted");
                        break;
                    case "registerOperator":
                        System.out.println("Ricevuto comando registerOperator");
                        String name = (String) in.readObject();   //mettere tutti i dati dell'operatore
                        String surname = (String) in.readObject();
                        String cf = (String) in.readObject();
                        String email = (String) in.readObject();
                        String password = (String) in.readObject();
                        database.registerOperator(name, surname, cf, email, password);
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
                        //out.writeObject("Area of interest inserted");
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
                        int idCentroMonitoraggio = (int) in.readObject();  // gestire inserimento
                        int idAreaInteresse = (int) in.readObject();  // gestire inserimento
                        int temperatura = (int) in.readObject();
                        int umidita = (int) in.readObject();
                        int pressione = (int) in.readObject();
                        int precipitazioni = (int) in.readObject();
                        int altitudineGhiacciai = (int) in.readObject();
                        int massaGhiacciai = (int) in.readObject();
                        int velocitaVento = (int) in.readObject();

                        database.insertClimateParameters(idCentroMonitoraggio, idAreaInteresse, temperatura, umidita, pressione, precipitazioni, altitudineGhiacciai, massaGhiacciai, velocitaVento);
                        break;
                    case "viewClimateParameters":
                        System.out.println("Ricevuto comando viewClimateParameters");
                        ResultSet climateRs = database.viewClimateParameters();
                        out.writeObject(climateRs);
                        break;
                    case "getAreeInteresse":
                        System.out.println("Ricevuto comando getAreeInteresse");
                        String cittaRicercata = (String) in.readObject();

                        out.writeObject(database.getAreeInteresse(cittaRicercata));
                        break;
                    case "checkExistingMonitoringCenter":
                        System.out.println("Ricevuto comando checkExistingMonitoringCenter");
                        String nomeUtente = (String) in.readObject();
                        out.writeObject(database.checkExistingMonitoringCenter(nomeUtente));
                        break;
                    case "getAllCity":
                        System.out.println("Ricevuto comando getAllCity");

                        out.writeObject(database.getAllCity());
                        break;
                    case "getCentroFromOperatore":
                        System.out.println("Ricevuto comando getCentroFromOperatore");
                        String idOperatore = (String) in.readObject();
                        out.writeObject(database.getMonitoringCenterCode(idOperatore));
                        break;
                    case "getCittaControllate":
                        System.out.println("Ricevuto comando getCittaControllate");
                        int idCentro = (int) in.readObject();
                        out.writeObject(database.getMonitoringCenterCities(idCentro));
                        break;
                    case "getRilevazioniCitta":
                        System.out.println("Ricevuto comando getRilevazioniCitta");
                        String cittaCercata = (String) in.readObject();
                        out.writeObject(database.getRilevazioniCitta(cittaCercata));
                        break;
                    case "getAreeByCoordinate":
                        System.out.println("Ricevuto comando getAreeByCoordinate");
                        double lat = (double) in.readObject();
                        double lon = (double) in.readObject();
                        out.writeObject(database.getAreeByCoordinate(lat, lon));
                        break;
                    case "getStatistics":
                        System.out.println("Ricevuto comando getStatistics");
                        String citta_cercata = (String) in.readObject();
                        out.writeObject(database.getStatistics(citta_cercata));
                        break;



                    default:
                        out.writeObject("Unknown command");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException  e) {
            e.printStackTrace();
            //System.out.println("serverthread");
        }
    }
}
