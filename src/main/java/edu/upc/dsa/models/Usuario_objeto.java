package edu.upc.dsa.models;

public class Usuario_objeto {
    private String usuario_id;
    private String objeto_id;

    public Usuario_objeto() { }

    public Usuario_objeto(String usuario_id, String objeto_id) {
        this.usuario_id = usuario_id;
        this.objeto_id = objeto_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getObjeto_id() {
        return objeto_id;
    }

    public void setObjeto_id(String objeto_id) {
        this.objeto_id = objeto_id;
    }
}
