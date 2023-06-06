package com.example.slagalica.pomocniAlati;

public class SharedData {
    private static SharedData instance;
    private int poeniIgraca;

    private SharedData() {
    }

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public int getPoeniIgraca() {
        return poeniIgraca;
    }

    public void setPoeniIgraca(int poeniIgraca) {
        this.poeniIgraca = poeniIgraca;
    }
}
