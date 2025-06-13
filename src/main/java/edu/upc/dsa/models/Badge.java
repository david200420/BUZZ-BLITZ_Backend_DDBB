package edu.upc.dsa.models;

public class Badge {
    private String name;
    private String avatar;

    public Badge() {}

    public Badge(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}