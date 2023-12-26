package com.example.slagalica.model;

import com.example.slagalica.servisi.KorisnikServis;

public class Korisnik {
    private int id;
    private String email;
    private String korisnickoIme;
    private String lozinka;
    private int tokeni;
    private String poslednjePrimljeno;

    public Korisnik() { }

    public Korisnik(String email, String korisnickoIme, String lozinka) {
        this.id = generisiId();
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.tokeni = 5;
        this.poslednjePrimljeno = null;
    }

    public Korisnik(int id, String email, String korisnickoIme, String lozinka, int tokeni, String poslednjePrimljeno) {
        this.id = id;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.tokeni = tokeni;
        this.poslednjePrimljeno = poslednjePrimljeno;
    }

    public int getId() {
        return id;
    }

    private int generisiId() {
        return KorisnikServis.getBrojKorisnika() + 1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public int getTokeni() {
        return tokeni;
    }

    public void setTokeni(int tokeni) {
        this.tokeni = tokeni;
    }

    public String getPoslednjePrimljeno() {
        return poslednjePrimljeno;
    }

    public void setPoslednjePrimljeno(String poslednjePrimljeno) {
        this.poslednjePrimljeno = poslednjePrimljeno;
    }
}
