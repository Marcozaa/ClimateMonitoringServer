package com.example.climatemonitoringserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import climatemonitoringserver.*;
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

    public List<AreaInteresse> getAreeInteresse(String areaCercata){
        try {
            List<AreaInteresse> aree = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT nome,latitudine,longitudine FROM areeinteresse WHERE nome iLIKE ?");
            stmt.setString(1,  "%" + areaCercata + "%");
            ResultSet rs = stmt.executeQuery();
            String value= null;
            while (rs.next()) {

                String nome = rs.getString(1);
                double latitudine = rs.getDouble(2);
                double longitudine = rs.getDouble(3);
                AreaInteresse area = new AreaInteresse(nome, latitudine, longitudine);
                aree.add(area);

            }
            System.out.println(aree);
            return aree;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("database");
        }
        return null;

    }

    public synchronized void insertMonitoringCenterData(String nome ,String via,int cap ,int numeroCivico,String comune,String provincia , ArrayList<String> citta ) {

    System.out.println("eseguo query...");
        try {

            PreparedStatement stmt1 = connection.prepareStatement("INSERT INTO centrimonitoraggio (nome, via, cap, numero_civico, comune, provincia) VALUES (?, ?, ?, ?, ?, ?)");
            stmt1.setString(1, nome);
            stmt1.setString(2, via);
            stmt1.setInt(3, cap);  // Codice postale
            stmt1.setInt(4, numeroCivico);     // Numero civico
    		stmt1.setString(5, comune);
    		stmt1.setString(6, provincia);
    		//stmt1.setInt(7, idAreaInteresse);
    		stmt1.executeUpdate();
    		for (String area : citta){
                System.out.print(area);
                PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO cittacontrollate (id_areainteresse, id_centromonitoraggio) VALUES ((SELECT codice from areeinteresse WHERE nome = ?), (SELECT codice from centrimonitoraggio WHERE nome = ?))");
                    		stmt2.setString(1, area);
                                        stmt2.setString(2, nome);
                    		stmt2.executeUpdate();

            }

            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void insertMonitoringCenterDataUser(String centromonitoraggio, String nomeUser) {

        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE operatore SET centromonitoraggio = (SELECT codice from centrimonitoraggio WHERE nome = ?) WHERE nome = ?");
            stmt.setString(1, centromonitoraggio);
            stmt.setString(2, nomeUser);
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
    
    public boolean checkExistingMonitoringCenter(String nome){
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT centromonitoraggio FROM operatore WHERE nome = ?");
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            String value= null;
            while (rs.next()) {
                value = rs.getString(1);

            }
            if(value==null){
                return false;
            }else {
                return true;
            }

            /*if(rs.next()){
                return true;
            }else {
                return false;
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public CentroMonitoraggio getMonitoringCenterCode(String nome){
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT codice,nome,via,cap,numero_civico,comune,provincia FROM centrimonitoraggio WHERE codice = (SELECT centromonitoraggio FROM operatore WHERE nome = ?)");
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            CentroMonitoraggio centro = null;
            while (rs.next()) {
                int codice = rs.getInt(1);
                String nomeCentro = rs.getString(2);
                String via = rs.getString(3);
                int cap = rs.getInt(4);
                int numeroCivico = rs.getInt(5);
                String comune = rs.getString(6);
                String provincia = rs.getString(7);

                centro = new CentroMonitoraggio(nomeCentro, codice, via, provincia, comune, cap, numeroCivico);
                return centro;

            }
            return centro;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AreaInteresse> getMonitoringCenterCities(int idCentro){
        try {
            List<AreaInteresse> citta = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT nome,areeinteresse.codice  FROM cittacontrollate JOIN areeinteresse ON cittacontrollate.id_areainteresse = areeinteresse.codice WHERE id_centromonitoraggio = ?");
            stmt.setInt(1, idCentro);
            ResultSet rs = stmt.executeQuery();
            AreaInteresse area= null;
            while (rs.next()) {
                String nome = rs.getString(1);
                int codice = rs.getInt(2);
                area = new AreaInteresse(nome, codice);
                citta.add(area);
            }
            return citta;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertClimateParameters(int idCentro, int idAreaInteresse, int temperatura, int umidita, int pressione, int precipitazioni, int altitudineGhiacciai, int massaGhiaccia, int vento){

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO rilevazione (centro_di_monitoraggio, area_di_interesse, data_di_rilevazione, ora, temperatura, umidita, pressione, precipitazioni, altitudine_ghiacciai, massa_ghiacciai, vento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, idCentro);
            stmt.setInt(2, idAreaInteresse);
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        Time currentTime = new Time(Calendar.getInstance().getTimeInMillis());

            stmt.setTime(4, currentTime);
            stmt.setInt(5, temperatura);
            stmt.setInt(6, umidita);
            stmt.setInt(7, pressione);
            stmt.setInt(8, precipitazioni);
            stmt.setInt(9, altitudineGhiacciai);
            stmt.setInt(10, massaGhiaccia);
            stmt.setInt(11, vento);
            stmt.executeUpdate();
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
