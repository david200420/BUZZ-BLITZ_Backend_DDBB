package edu.upc.dsa.models;

import java.util.HashMap;
import java.util.Map;

public class Tienda {
    private Map<String, Objeto> armas;
    private Map<String, Objeto> skins;
    private int converser;
    private int converserGold;

    public Tienda() {
        this.armas = new HashMap<>();
        this.skins = new HashMap<>();
        this.converser = 1;
        this.converserGold = 100;
    }

    // --- Getters y Setters ---

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

    public int getConverser() {
        return converser;
    }

    public void setConverser(int converser) {
        this.converser = converser;
    }

    public int getConverserGold() {
        return converserGold;
    }

    public void setConverserGold(int converserGold) {
        this.converserGold = converserGold;
    }


}

