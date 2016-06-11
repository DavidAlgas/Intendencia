package com.example.david.intendencia.Objetos;

import java.io.Serializable;

public class Tienda implements Serializable {

    private String nombre;
    private String modelo;
    private String tipo;
    private String capacidad;
    private String npiquetas;
    private String estado;
    private String ultimaRevision;
    private String logUpdate;
    private String poseedorID = "Nadie";
    private String poseedorNombre = "Nadie";
    private boolean disponible;

    public Tienda() {
    }

    public Tienda(String nombre, String modelo, String tipo, String capacidad, String npiquetas, String estado, String ultimaRevision, String logUpdate, boolean disponible) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.npiquetas = npiquetas;
        this.estado = estado;
        this.ultimaRevision = ultimaRevision;
        this.logUpdate = logUpdate;
        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getNpiquetas() {
        return npiquetas;
    }

    public void setNpiquetas(String npiquetas) {
        this.npiquetas = npiquetas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUltimaRevision() {
        return ultimaRevision;
    }

    public void setUltimaRevision(String ultimaRevision) {
        this.ultimaRevision = ultimaRevision;
    }

    public String getLogUpdate() {
        return logUpdate;
    }

    public void setLogUpdate(String logUpdate) {
        this.logUpdate = logUpdate;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getPoseedorID() {
        return poseedorID;
    }

    public void setPoseedorID(String poseedorID) {
        this.poseedorID = poseedorID;
    }

    public String getPoseedorNombre() {
        return poseedorNombre;
    }

    public void setPoseedorNombre(String poseedorNombre) {
        this.poseedorNombre = poseedorNombre;
    }
}
