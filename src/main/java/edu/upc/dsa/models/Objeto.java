package edu.upc.dsa.models;

public class Objeto {
    private String id;
    private String nombre;
    private Integer precio;
    private int tipo;
    private String descripcion;
    private String imagenResId;// "arma" o "skin"

    public Objeto() {}

    public Objeto(String id, String nombre, int precio , int tipo, String descripcion ,String imagenResId) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenResId() {
        return imagenResId;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagenResId(String imagenResId) {
        this.imagenResId = imagenResId;
    }

    public int getPrecio() {
        return this.precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {

        this.tipo = tipo;
    }
}

