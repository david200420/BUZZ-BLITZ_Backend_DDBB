package edu.upc.dsa.models;

public class PartidaGuardada {
    private String id;
    private Integer flor;
    private Integer mejorPuntuacion;
    private Integer numPartidas;
    private Integer floreGold;

    public PartidaGuardada() {}
    public PartidaGuardada(String id, Integer flor, Integer mejorPuntuacion, Integer numPartidas, Integer floreGold) {
        this.id = id;
        this.flor = flor;
        this.mejorPuntuacion = mejorPuntuacion;
        this.numPartidas = numPartidas;
        this.floreGold = floreGold;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getFlor() {
        return flor;
    }
    public void setFlor(Integer flor) {
        this.flor = flor;
    }

    public Integer getMejorPuntuacion() {
        return mejorPuntuacion;
    }
    public void setMejorPuntuacion(Integer mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }
    public Integer getNumPartidas() {
        return numPartidas;
    }
    public void setNumPartidas(Integer numPartidas) {
        this.numPartidas = numPartidas;
    }
    public Integer getFloreGold() {
        return floreGold;
    }
    public void setFloreGold(Integer floreGold) {
        this.floreGold = floreGold;
    }
}
