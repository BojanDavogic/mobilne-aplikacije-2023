package com.example.slagalica.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.slagalica.R;

public class KorakPoKorakFragment extends Fragment {

    ImageButton btnKorakPoKorakDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_korak_po_korak, container, false);
        btnKorakPoKorakDalje = view.findViewById(R.id.korakPoKorakDalje);
        btnKorakPoKorakDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KoZnaZnaFragment koZnaZnaFragment = new KoZnaZnaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, koZnaZnaFragment).commit();
            }
        });
        return view;
    }
}