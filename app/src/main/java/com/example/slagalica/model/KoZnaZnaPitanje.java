package com.example.slagalica.model;

import java.util.List;

public class KoZnaZnaPitanje {
    private String pitanje;
    private List<String> odgovori;
    private String tacanOdgovor;

    public KoZnaZnaPitanje() {}

    public KoZnaZnaPitanje(String pitanje, List<String> odgovori, String tacanOdgovor) {
        this.pitanje = pitanje;
        this.odgovori = odgovori;
        this.tacanOdgovor = tacanOdgovor;
    }

    public String getPitanje() {
        return pitanje;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public List<String> getOdgovori() {
        return odgovori;
    }

    public void setOdgovori(List<String> odgovori) {
        this.odgovori = odgovori;
    }

    public String getTacanOdgovor() {
        return tacanOdgovor;
    }

    public void setTacanOdgovor(String tacanOdgovor) {
        this.tacanOdgovor = tacanOdgovor;
    }
}
