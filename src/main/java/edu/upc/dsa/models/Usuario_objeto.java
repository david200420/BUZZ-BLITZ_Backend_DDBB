package edu.upc.dsa.models;

public class Usuario_objeto {
    private String usuario_id;
    private String objeto_nombre;

    public Usuario_objeto() { }

    public Usuario_objeto(String usuario_id, String objeto_nombre) {
        this.usuario_id = usuario_id;
        this.objeto_nombre = objeto_nombre;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getObjeto_nombre() {
        return objeto_nombre;
    }

    public void setObjeto_nombre(String objeto_nombre) {
        this.objeto_nombre = objeto_nombre;
    }
}
