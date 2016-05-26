package com.example.david.intendencia.Objetos;


public class Tarea {

    private String creadoPOR;
    private String tarea;
    private boolean completo;

    public Tarea() {
    }

    public Tarea(String creadoPOR, String tarea) {
        this.creadoPOR = creadoPOR;
        this.tarea = tarea;
        this.completo = false;
    }

    public String getCreadoPOR() {
        return creadoPOR;
    }

    public void setCreadoPOR(String creadoPOR) {
        this.creadoPOR = creadoPOR;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }
}
