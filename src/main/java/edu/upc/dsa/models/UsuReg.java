package edu.upc.dsa.models;

public class UsuReg {

    private String id;
    private String name;
    private String pswd;
    private String mail;

    public UsuReg() {}
    public UsuReg(String id, String name, String pswd, String mail) {
        this.id = id;
        this.name = name;
        this.pswd = pswd;
        this.mail = mail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPswd() {
        return pswd;
    }

    public String getMail() {
        return mail;
    }
}
