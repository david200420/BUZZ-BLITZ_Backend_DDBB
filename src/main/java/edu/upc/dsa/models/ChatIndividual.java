package edu.upc.dsa.models;

public class ChatIndividual {
    private String id;
    private String nameFrom;
    private String nameTo;
    private String comentario;
    private String date;

    public ChatIndividual() {}

    public ChatIndividual(String nameFrom, String nameTo, String comentario, String date) {
        this.nameFrom = nameFrom;
        this.nameTo = nameTo;
        this.comentario = comentario;
        this.date = date;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNameFrom() { return nameFrom; }
    public void setNameFrom(String nameFrom) { this.nameFrom = nameFrom; }
    public String getNameTo() { return nameTo; }
    public void setNameTo(String nameTo) { this.nameTo = nameTo; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}