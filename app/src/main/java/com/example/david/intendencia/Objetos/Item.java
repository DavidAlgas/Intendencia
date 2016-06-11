package com.example.david.intendencia.Objetos;

import java.io.Serializable;

public class Item implements Serializable {

    private String marca;
    private String modelo;
    private String tipo;
    private String cantidad;
    private String detalles;

    public Item() {
    }

    public Item(String marca, String modelo, String tipo, String cantidad, String detalles) {
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.detalles = detalles;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
