package com.example.proyectoprogra4.modelos;

public class Modelo {
    private String mensaje;

    public Modelo(){

    }

    public Modelo(String mensaje){
    this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
