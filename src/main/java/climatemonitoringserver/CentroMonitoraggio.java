package climatemonitoringserver;
import climatemonitoringserver.*;

import java.io.Serializable;


import java.util.*;
public class CentroMonitoraggio implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String nome;
    private int id;

    private String via, provincia,comune;
    private int cap, numeroCivico;

    private List<AreaInteresse> areeInteresse = new ArrayList<>();


    public CentroMonitoraggio(String nome, int id, String via, String provincia, String comune, int cap, int numeroCivico) {
        this.nome = nome;
        this.id = id;
        this.via = via;
        this.provincia = provincia;
        this.comune = comune;
        this.cap = cap;
        this.numeroCivico = numeroCivico;

    }


    public void addAreaInteresse(AreaInteresse area){
        areeInteresse.add(area);
    }

    public void removeAreaInteresse(AreaInteresse area){
        areeInteresse.remove(area);
    }

    public List<AreaInteresse> getAreeInteresse(){
        return areeInteresse;
    }

    public String getNome(){
        return nome;
    }

    public int getId(){
        return id;
    }

    public String getVia(){
        return via;
    }

    public String getProvincia(){
        return provincia;
    }

    public String getComune(){
        return comune;
    }

    public int getCap(){
        return cap;
    }

    public int getNumeroCivico(){
        return numeroCivico;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setVia(String via){
        this.via = via;
    }

    public void setProvincia(String provincia){
        this.provincia = provincia;
    }

    public void setComune(String comune){
        this.comune = comune;
    }

    public void setCap(int cap){
        this.cap = cap;
    }

    public void setNumeroCivico(int numeroCivico){
        this.numeroCivico = numeroCivico;
    }

    public void setAreeInteresse(List<AreaInteresse> areeInteresse){
        this.areeInteresse = areeInteresse;
    }


    public String toString(){
        return "Nome: " + nome + "\n" + "Id: " + id + "\n" + "Via: " + via + "\n" + "Provincia: " + provincia + "\n" + "Comune: " + comune + "\n" + "Cap: " + cap + "\n" + "Numero Civico: " + numeroCivico + "\n";
    }

    public void setAreaInteresse(List<AreaInteresse> areeInteresse){
        this.areeInteresse = areeInteresse;
    }

}
