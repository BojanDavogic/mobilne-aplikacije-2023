package com.example.slagalica.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.slagalica.R;
public class KoZnaZnaFragment extends Fragment {

    ImageButton btnKoZnaZnaDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ko_zna_zna, container, false);
        btnKoZnaZnaDalje = view.findViewById(R.id.koZnaZnaDalje);
        btnKoZnaZnaDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsocijacijeFragment asocijacijeFragment = new AsocijacijeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, asocijacijeFragment).commit();
            }
        });
        return view;
    }
}