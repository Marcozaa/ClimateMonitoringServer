package com.example.climatemonitoringserver;

import java.sql.*;

public class Database {
    private String dbHost;
    private String dbUsername;
    private String dbPassword;
    private Connection connection;

    public Database(String dbHost, String dbUsername, String dbPassword) {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbHost, dbUsername,dbPassword);

            if(connection != null){
                System.out.println("connection ok");
            }else{
                System.out.println("connection failed");
            }
            //connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public ResultSet searchData(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void insertMonitoringCenterData(String data) {

        try {

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO monitoring_centers (data) VALUES (?)");
            stmt.setString(1, data);
            stmt.executeUpdate();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public synchronized void registerOperator(String nome, String cognome, String cf, String email, String password  ) {  // aggiungere info operatore
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO operatore (nome, cognome, cf, email, password) VALUES (?, ?, ? , ?, ?)");
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, cf);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.executeUpdate();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void insertAreaOfInterest(String nome, String stato, double latitudine, double longitudine ) { //gestire inserimento area interesse
        try {


            PreparedStatement stmt = connection.prepareStatement("INSERT INTO areeinteresse (nome, latitudine, longitudine) VALUES (?, ?, ?)");
            stmt.setString(1, nome);
            stmt.setDouble(2, latitudine);
            stmt.setDouble(3, longitudine);
            stmt.executeUpdate();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean validateUser(String username, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM operatore WHERE nome = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void insertClimateParameters(String parameters) { //gestire inserimento parametri
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO rilevazione (parameters) VALUES (?)");
            stmt.setString(1, parameters);
            stmt.executeUpdate();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet viewClimateParameters() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rilevazione");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
