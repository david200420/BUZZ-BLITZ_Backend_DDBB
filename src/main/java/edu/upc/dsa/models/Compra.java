package edu.upc.dsa.models;

public class Compra {
    private String usuarioId;
    private Item objeto;

    public Compra() { }

    public Compra(String usuarioId, Item objeto) {
        this.usuarioId = usuarioId;
        this.objeto = objeto;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Item getObjeto() {
        return objeto;
    }

    public void setObjeto(Item objeto) {
        this.objeto = objeto;
    }
}
