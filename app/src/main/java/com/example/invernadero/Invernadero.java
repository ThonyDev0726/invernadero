package com.example.invernadero;

public class Invernadero {
    String invTemperatura;
    String invHumedad;
    String invLuz;
    String invVentilacion;
    String invRiego;
    String invFecha;
    String invHora;
    String invObservacion;
    String invImagen;
    String Key;

    public Invernadero(String invTemperatura, String invHumedad, String invLuz, String invVentilacion, String invRiego, String invFecha, String invHora, String invObservacion, String invImagen) {
        this.invTemperatura = invTemperatura;
        this.invHumedad = invHumedad;
        this.invLuz = invLuz;
        this.invVentilacion = invVentilacion;
        this.invRiego = invRiego;
        this.invFecha = invFecha;
        this.invHora = invHora;
        this.invObservacion = invObservacion;
        this.invImagen = invImagen;
    }

    public String getInvTemperatura() {
        return invTemperatura;
    }

    public void setInvTemperatura(String invTemperatura) {
        this.invTemperatura = invTemperatura;
    }

    public String getInvHumedad() {
        return invHumedad;
    }

    public void setInvHumedad(String invHumedad) {
        this.invHumedad = invHumedad;
    }

    public String getInvLuz() {
        return invLuz;
    }

    public void setInvLuz(String invLuz) {
        this.invLuz = invLuz;
    }

    public String getInvVentilacion() {
        return invVentilacion;
    }

    public void setInvVentilacion(String invVentilacion) {
        this.invVentilacion = invVentilacion;
    }

    public String getInvRiego() {
        return invRiego;
    }

    public void setInvRiego(String invRiego) {
        this.invRiego = invRiego;
    }

    public String getInvFecha() {
        return invFecha;
    }

    public void setInvFecha(String invFecha) {
        this.invFecha = invFecha;
    }

    public String getInvHora() {
        return invHora;
    }

    public void setInvHora(String invHora) {
        this.invHora = invHora;
    }

    public String getInvObservacion() {
        return invObservacion;
    }

    public void setInvObservacion(String invObservacion) {
        this.invObservacion = invObservacion;
    }

    public String getInvImagen() {
        return invImagen;
    }

    public void setInvImagen(String invImagen) {
        this.invImagen = invImagen;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    //
    public Invernadero() {

    }
}
