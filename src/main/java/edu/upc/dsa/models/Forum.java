package edu.upc.dsa.models;

public class Forum {
    private String id;
    private String name;
    private String comentario;
    private String date;

    public Forum() {}

    public Forum(String name, String comentario, String date) {
        this.name = name;
        this.comentario = comentario;
        this.date = date;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}