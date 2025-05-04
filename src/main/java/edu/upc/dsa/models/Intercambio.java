package edu.upc.dsa.models;

public class Intercambio {
    private int tarrosMiel;
    private int Flores;

    public Intercambio(int tarrosMiel){}
    public Intercambio(int tarrosMiel, int Flores){
        this.tarrosMiel = tarrosMiel;
        this.Flores = Flores;
    }

    public void setTarrosMiel(int tarrosMiel) {
        this.tarrosMiel = tarrosMiel;
    }

    public void setFlores(int flores) {
        Flores = flores;
    }

    public int getFlores() {
        return Flores;
    }

    public int getTarrosMiel() {
        return tarrosMiel;
    }
}
