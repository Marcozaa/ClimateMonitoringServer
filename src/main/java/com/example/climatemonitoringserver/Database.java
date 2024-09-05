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

    public List<Rilevazione> getRilevazioniCitta(String cittaCercata){
        try {
            List<Rilevazione> rilevazioni = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rilevazione JOIN centrimonitoraggio ON rilevazione.centro_di_monitoraggio = centrimonitoraggio.codice JOIN areeinteresse ON rilevazione.area_di_interesse = areeinteresse.codice  WHERE area_di_interesse = (SELECT codice FROM areeinteresse WHERE nome = ?)");
            stmt.setString(1, cittaCercata);



            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                int idCentro = rs.getInt(2);
                int idAreaInteresse = rs.getInt(3);
                Date dataRilevazione = rs.getDate(4);
                Time oraRilevazione = rs.getTime(5);
                int vento = rs.getInt(6);
                int umidita = rs.getInt(7);
                int pressione = rs.getInt(8);
                int temperatura = rs.getInt(9);
                int precipitazioni = rs.getInt(12);
                int altitudineGhiacciai = rs.getInt(10);
                int massaGhiacciai = rs.getInt(11);
                int codiceCentro = rs.getInt(13);
                String nomeCentro = rs.getString(14);
                String via = rs.getString(15);
                int cap = rs.getInt(16);
                int numeroCivico = rs.getInt(17);
                String comune = rs.getString(18);
                String provincia = rs.getString(19);
                int codiceArea = rs.getInt(21);
                String nomeArea = rs.getString(22);
                Double latitudine = rs.getDouble(23);
                Double longitudine = rs.getDouble(24);
                AreaInteresse area = new AreaInteresse(nomeArea, Integer.toString(codiceArea), latitudine, longitudine);
                CentroMonitoraggio centro = new CentroMonitoraggio(nomeCentro, codiceCentro, via, provincia, comune, cap, numeroCivico);
                Rilevazione rilevazione = new Rilevazione(id, centro, area, dataRilevazione, oraRilevazione, temperatura, umidita, pressione, precipitazioni, altitudineGhiacciai, massaGhiacciai, vento);
                rilevazioni.add(rilevazione);
            }
            return rilevazioni;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AreaInteresse> getAreeByCoordinate(double lat, double lon){
        try{
            List<AreaInteresse> aree = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(" SELECT codice,nome, latitudine, longitudine, distance FROM (SELECT nome, codice, latitudine, longitudine,SQRT(POW(111.2 * (latitudine - ?), 2) +POW(111.2 * (?-  longitudine) * COS(latitudine / 57.3), 2)) AS distance FROM areeinteresse) AS subquery WHERE distance < 500 ORDER BY distance");

            stmt.setDouble(1, lat);
            stmt.setDouble(2, lon);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                int codice = rs.getInt(1);
                String nome = rs.getString(2);
                double latitudine = rs.getDouble(3);
                double longitudine = rs.getDouble(4);
                double distanza = rs.getDouble(5); // distanza in km
                AreaInteresse area = new AreaInteresse(nome, Integer.toString(codice), latitudine, longitudine, distanza);

                aree.add(area);
            }
            return aree;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Double> getStatistics(String cittaCercata){
            try {
                List<Rilevazione> rilevazioni = new ArrayList<>();
                List <Double> averages = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rilevazione JOIN centrimonitoraggio ON rilevazione.centro_di_monitoraggio = centrimonitoraggio.codice JOIN areeinteresse ON rilevazione.area_di_interesse = areeinteresse.codice  WHERE area_di_interesse = (SELECT codice FROM areeinteresse WHERE nome = ?)");
                stmt.setString(1, cittaCercata);
                double averageTemperature = 0;
                double averageHumidity = 0;
                double averagePressure = 0;
                double averagePrecipitations = 0;
                double averageAltitude = 0;
                double averageMass = 0;
                double averageWind = 0;


                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    int idCentro = rs.getInt(2);
                    int idAreaInteresse = rs.getInt(3);
                    Date dataRilevazione = rs.getDate(4);
                    Time oraRilevazione = rs.getTime(5);
                    int vento = rs.getInt(6);
                    int umidita = rs.getInt(7);
                    int pressione = rs.getInt(8);
                    int temperatura = rs.getInt(9);
                    int precipitazioni = rs.getInt(12);
                    int altitudineGhiacciai = rs.getInt(10);
                    int massaGhiacciai = rs.getInt(11);
                    int codiceCentro = rs.getInt(13);
                    String nomeCentro = rs.getString(14);
                    String via = rs.getString(15);
                    int cap = rs.getInt(16);
                    int numeroCivico = rs.getInt(17);
                    String comune = rs.getString(18);
                    String provincia = rs.getString(19);
                    int codiceArea = rs.getInt(21);
                    String nomeArea = rs.getString(22);
                    Double latitudine = rs.getDouble(23);
                    Double longitudine = rs.getDouble(24);

                    AreaInteresse area = new AreaInteresse(nomeArea, Integer.toString(codiceArea), latitudine, longitudine);
                    CentroMonitoraggio centro = new CentroMonitoraggio(nomeCentro, codiceCentro, via, provincia, comune, cap, numeroCivico);
                    Rilevazione rilevazione = new Rilevazione(id, centro, area, dataRilevazione, oraRilevazione, temperatura, umidita, pressione, precipitazioni, altitudineGhiacciai, massaGhiacciai, vento);
                    rilevazioni.add(rilevazione);
                }
                averageAltitude = rilevazioni.stream().mapToDouble(Rilevazione::getAltitudineGhiacciai).average().orElse(0);
                averageHumidity = rilevazioni.stream().mapToDouble(Rilevazione::getUmidita).average().orElse(0);
                averageMass = rilevazioni.stream().mapToDouble(Rilevazione::getMassaGhiacciai).average().orElse(0);
                averagePrecipitations = rilevazioni.stream().mapToDouble(Rilevazione::getPrecipitazioni).average().orElse(0);
                averagePressure = rilevazioni.stream().mapToDouble(Rilevazione::getPressione).average().orElse(0);
                averageTemperature = rilevazioni.stream().mapToDouble(Rilevazione::getTemperatura).average().orElse(0);
                averageWind = rilevazioni.stream().mapToDouble(Rilevazione::getVento).average().orElse(0);

                averages.add(averageTemperature);
                averages.add(averageHumidity);
                averages.add(averagePressure);
                averages.add(averagePrecipitations);
                averages.add(averageAltitude);
                averages.add(averageMass);
                averages.add(averageWind);

                return averages;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
     }
