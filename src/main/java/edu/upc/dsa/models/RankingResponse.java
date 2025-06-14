package edu.upc.dsa.models;

import java.util.List;

public class RankingResponse {
    private List<Info> ranking;
    private int posicionUsuario;

    public RankingResponse() {}

    public RankingResponse(List<Info> ranking, int posicionUsuario) {
        this.ranking = ranking;
        this.posicionUsuario = posicionUsuario;
    }

    // Getters y Setters
    public List<Info> getRanking() {
        return ranking;
    }

    public void setRanking(List<Info> ranking) {
        this.ranking = ranking;
    }

    public int getPosicionUsuario() {
        return posicionUsuario;
    }

    public void setPosicionUsuario(int posicionUsuario) {
        this.posicionUsuario = posicionUsuario;
    }
}
