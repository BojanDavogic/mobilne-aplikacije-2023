package com.example.slagalica.model;

public class Prijatelj {
    private int slika;
    private String ime;
    private Boolean aktivan;
    private Boolean zauzet;

    public Prijatelj(int slika, String ime, Boolean aktivan, Boolean zauzet) {
        this.slika = slika;
        this.ime = ime;
        this.aktivan = aktivan;
        this.zauzet = zauzet;
    }

    public int getSlika() {
        return slika;
    }

    public void setSlika(int slika) {
        this.slika = slika;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }

    public Boolean getZauzet() {
        return zauzet;
    }

    public void setZauzet(Boolean zauzet) {
        this.zauzet = zauzet;
    }
}
