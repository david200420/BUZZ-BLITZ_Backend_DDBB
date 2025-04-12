package edu.upc.dsa.models;

import java.util.List;

public class ConsultaTienda {
    private List<Object> consulta;

    public ConsultaTienda(List<Object> consulta) {
        this.consulta = consulta;
    }

    public List<Object> getConsulta() {
        return consulta;
    }
    public void setConsulta(List<Object> consulta) {
        this.consulta = consulta;
    }
}

