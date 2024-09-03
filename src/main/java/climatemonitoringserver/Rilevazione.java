package climatemonitoringserver;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rilevazione implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;

    private CentroMonitoraggio centroMonitoraggio;
    private AreaInteresse areaInteresse;

    private Date dataRilevazione;
    private Time oraRilevazione;

    private int vento;
    private int umidita;
    private int pressione;
    private int temperatura;
    private int precipitazioni;
    private int altitudineGhiacciai;
    private int massaGhiacciai;

    public Rilevazione(int id, CentroMonitoraggio centroMonitoraggio, AreaInteresse areaInteresse, Date dataRilevazione, Time oraRilevazione, int vento, int umidita, int pressione, int temperatura, int precipitazioni, int altitudineGhiacciai, int massaGhiacciai) {
        this.id = id;
        this.centroMonitoraggio = centroMonitoraggio;
        this.areaInteresse = areaInteresse;
        this.dataRilevazione = dataRilevazione;
        this.oraRilevazione = oraRilevazione;
        this.vento = vento;
        this.umidita = umidita;
        this.pressione = pressione;
        this.temperatura = temperatura;
        this.precipitazioni = precipitazioni;
        this.altitudineGhiacciai = altitudineGhiacciai;
        this.massaGhiacciai = massaGhiacciai;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CentroMonitoraggio getCentroMonitoraggio() {
        return centroMonitoraggio;
    }

    public void setCentroMonitoraggio(CentroMonitoraggio centroMonitoraggio) {
        this.centroMonitoraggio = centroMonitoraggio;
    }

    public AreaInteresse getAreaInteresse() {
        return areaInteresse;
    }

    public void setAreaInteresse(AreaInteresse areaInteresse) {
        this.areaInteresse = areaInteresse;
    }

    public String getDataRilevazione() {
        String pattern = "MM/dd/yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(dataRilevazione);

        return date;
    }

    public void setDataRilevazione(Date dataRilevazione) {

        this.dataRilevazione = dataRilevazione;
    }

    public String getOraRilevazione() {
        String pattern = "HH:mm";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String time = simpleDateFormat.format(oraRilevazione);
        return time;
    }

    public void setOraRilevazione(Time oraRilevazione) {
        this.oraRilevazione = oraRilevazione;
    }

    public int getUmidita() {
        return umidita;
    }

    public void setUmidita(int umidita) {
        this.umidita = umidita;
    }

    public int getPressione() {
        return pressione;
    }

    public void setPressione(int pressione) {
        this.pressione = pressione;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getPrecipitazioni() {
        return precipitazioni;
    }

    public void setPrecipitazioni(int precipitazioni) {
        this.precipitazioni = precipitazioni;
    }

    public int getAltitudineGhiacciai() {
        return altitudineGhiacciai;
    }

    public void setAltitudineGhiacciai(int altitudineGhiacciai) {
        this.altitudineGhiacciai = altitudineGhiacciai;
    }

    public int getMassaGhiacciai() {
        return massaGhiacciai;
    }

    public void setMassaGhiacciai(int massaGhiacciai) {
        this.massaGhiacciai = massaGhiacciai;
    }

    public int getVento() {
        return vento;
    }

    public void setVento(int vento) {
        this.vento = vento;
    }

    public String getNomeCentroMonitoraggio() {
        return centroMonitoraggio.getNome();
    }
}
