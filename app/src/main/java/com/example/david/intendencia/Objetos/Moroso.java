package com.example.david.intendencia.Objetos;


import java.util.List;

public class Moroso {

    private String Nombre;
    private List<String> IDTIENDA;

    public Moroso() {
    }

    public Moroso(String Nombre, List<String> lTiendas) {
        this.Nombre = Nombre;
        this.setIDTIENDA(lTiendas);
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public List<String> getIDTIENDA() {
        return IDTIENDA;
    }

    public void setIDTIENDA(List<String> IDTIENDA) {
        this.IDTIENDA = IDTIENDA;
    }
}
