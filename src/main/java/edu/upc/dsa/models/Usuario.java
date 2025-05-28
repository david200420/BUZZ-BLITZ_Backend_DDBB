package edu.upc.dsa.models;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String name;
    private String pswd;
    private String mail;
    private String apellidos;

    private Integer tarrosMiel;
    private Integer flor;
    private Integer mejorPuntuacion;
    private Integer numPartidas;
    private Integer floreGold;

    private Map<String, Objeto> armas;
    private Map<String, Objeto> skins;

    private String pregunta;
    private String respuesta;

    public Usuario() {}

    public Usuario(String id, String name, String apellidos,String pswd, String mail, String q, String a) { // Primera vez que entras
        this.id = id;
        this.name = name;
        this.pswd = pswd;
        this.mail = mail;
        this.apellidos = apellidos;
        this.pregunta = q;
        this.respuesta = a;
    }
    public void UpdateArmas(Objeto objeto){
        armas.put(objeto.getId(), objeto);
    }
    public void UpdateSkin(Objeto objeto){
        skins.put(objeto.getId(), objeto);
    }
    // --- Getters y Setters básicos ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getTarrosMiel() {
        return tarrosMiel;
    }

    public void setTarrosMiel(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }

    public int getFlor() {
        return flor;
    }

    public void setFlor(int flor) {
        this.flor = flor;
    }

    public int getMejorPuntuacion() {
        return mejorPuntuacion;
    }

    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public int getNumPartidas() {
        return numPartidas;
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    public int getFloreGold() {
        return floreGold;
    }

    public void setFloreGold(int floreGold) {
        this.floreGold = floreGold;
    }

    public Map<String, Objeto> getArmas() {
        return armas;
    }

    public void setArmas(Map<String, Objeto> armas) {
        this.armas = armas;
    }

    public Map<String, Objeto> getSkins() {
        return skins;
    }

    public void setSkins(Map<String, Objeto> skins) {
        this.skins = skins;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
