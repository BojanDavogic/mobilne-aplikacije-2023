package com.example.slagalica.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.slagalica.R;

public class SpojniceFragment extends Fragment {

    ImageButton btnSpojniceDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spojnice, container, false);
        btnSpojniceDalje = view.findViewById(R.id.spojniceDalje);
        btnSpojniceDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkockoFragment skockoFragment = new SkockoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, skockoFragment).commit();
            }
        });
        return view;
    }
}