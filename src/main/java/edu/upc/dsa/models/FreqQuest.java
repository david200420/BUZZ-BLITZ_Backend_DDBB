package edu.upc.dsa.models;

public class FreqQuest {
    private String id;
    private String date;
    private String pregunta;
    private String respuesta;
    private String sender;

    public FreqQuest() {}

    public FreqQuest(String id, String pregunta, String respuesta, String date, String sender) {
        this.id = id;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.date = date;
        this.sender = sender;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
