package com.example.slagalica.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.slagalica.R;
public class SkockoFragment extends Fragment {

    ImageButton btnSkockoDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skocko, container, false);
        btnSkockoDalje = view.findViewById(R.id.skockoDalje);
        btnSkockoDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KorakPoKorakFragment korakPoKorakFragment = new KorakPoKorakFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, korakPoKorakFragment).commit();
            }
        });
        return view;
    }
}