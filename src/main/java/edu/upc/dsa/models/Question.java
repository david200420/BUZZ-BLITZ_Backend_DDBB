package edu.upc.dsa.models;

public class Question {
    private String id;
    private String date;
    private String title;
    private String message;
    private String sender;

    public Question() {}

    public Question(String id, String date, String title, String message, String sender) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.message = message;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
