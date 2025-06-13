package edu.upc.dsa.models;

public class Info {

    private String usuario;
    private int mejorPuntuacion;
    private int numPartidas;
    private String pregunta; // Nuevo campo

    public Info(String usuario, int mejorPuntuacion, int numPartidas, String pregunta) {
        this.usuario = usuario;
        this.mejorPuntuacion = mejorPuntuacion;
        this.numPartidas = numPartidas;
        this.pregunta = pregunta;
    }
    public Info(){}

    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setMejorPuntuacion(int mejorPuntuacion) { this.mejorPuntuacion = mejorPuntuacion; }
    public void setNumPartidas(int numPartidas) { this.numPartidas = numPartidas; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public String getUsuario() { return usuario; }
    public int getNumPartidas() { return numPartidas; }
    public int getMejorPuntuacion() { return mejorPuntuacion; }
    public String getPregunta() { return pregunta; }
}