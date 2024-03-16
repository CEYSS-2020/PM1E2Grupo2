package com.uth.pm1e2grupo2.models;

import com.google.gson.annotations.SerializedName;

public class Contactos {

    //private int id, pais;
    //private String nombre, telefono, nota, avatar, latitud, longitud;

    @SerializedName("id")
    private int id;
    @SerializedName("pais")
    private int pais;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("nota")
    private String nota;
    @SerializedName("latitud")
    private String latitud;
    @SerializedName("longitud")
    private String longitud;
    @SerializedName("avatar")
    private String avatar;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPais() {
        return pais;
    }

    public void setPais(int pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLatitud() {
        return latitud;
    }
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
