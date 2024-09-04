package climatemonitoringserver;

import java.io.Serializable;



//prova

/**
 * Classe che rappresenta un'area di interesse
 * Contiene il nome, le coordinate e lo stato di un'area di interesse
 */
public class AreaInteresse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private Double coordX;
    private Double coordY;
    private String stato;

    private double distanza;



    private int id;


    /*public AreaInteresse(String nome, String stato, String coordX, String coordY) {
        this.nome = nome;
        this.coordX = coordX;
        this.coordY = coordY;
        this.stato = stato;
    }*/

    public AreaInteresse(String nome) {
        this.nome = nome;

    }

    public AreaInteresse(String nome, String stato, double latitudine, double longitudine) {
        this.nome = nome;
        this.coordX = latitudine;
        this.coordY = longitudine;
        this.stato = stato;
    }

    public AreaInteresse(String nome, String stato, double latitudine, double longitudine, double distanza) {
        this.nome = nome;
        this.coordX = latitudine;
        this.coordY = coordY;
        this.stato = stato;
        this.distanza = distanza;
    }

    public AreaInteresse(String nome, double latitudine, double longitudine) {
        this.nome = nome;
        this.coordX = latitudine;
        this.coordY = longitudine;
    }

    public AreaInteresse(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    /**
     * Metodo che restituisce il nome dell'area di interesse
     * @return
     */
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Metodo che restituisce le coordinate dell'area di interesse
     * @return
     */
    public Double getCoordX() {
        return coordX;
    }

    /**
     * Metodo che restituisce le coordinate dell'area di interesse
     * @return
     */
    public Double getCoordY() {
        return coordY;
    }

    /**
     * Metodo che restituisce lo stato dell'area di interesse
     * @return
     */
    public String getStato() {
        return stato;
    }

    @Override
    public String toString() {
        return nome;
    }

    public double getDistanza() {
        return distanza;
    }

    public void setDistanza(double distanza) {
        this.distanza = distanza;
    }
}
