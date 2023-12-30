package com.example.slagalica.fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.slagalica.R;
import com.example.slagalica.model.Korisnik;
import com.example.slagalica.servisi.KorisnikServis;

public class PregledProfilaFragment extends Fragment {
    KorisnikServis korisnikServis = new KorisnikServis();
    Korisnik ulogovaniKorisnik = korisnikServis.getTrenutnoUlogovaniKorisnik();
    TextView brojTokena, brojZvezdica, korisnickoIme, email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pregled_profila, container, false);

        brojTokena = view.findViewById(R.id.brojTokena);
        korisnickoIme = view.findViewById(R.id.korIme);
        email = view.findViewById(R.id.emailKorisnika);

        brojTokena.setText(String.valueOf(ulogovaniKorisnik.getTokeni()));
        korisnickoIme.setText(ulogovaniKorisnik.getKorisnickoIme());
        email.setText(ulogovaniKorisnik.getEmail());
        return view;
    }
}
