package com.example.crudfirebase;

public class MainModel {
    String Aro, Modelo, Talla, imgURl;

    public MainModel() {
    }

    public MainModel(String aro, String modelo, String talla, String imgURl) {
        Aro = aro;
        Modelo = modelo;
        this.Talla = talla;
        this.imgURl = imgURl;
    }

    public String getAro() {
        return Aro;
    }

    public void setAro(String aro) {
        Aro = aro;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String talla) {
        Talla = talla;
    }

    public String getImgURl() {
        return imgURl;
    }

    public void setImgURl(String imgURl) {
        this.imgURl = imgURl;
    }
}