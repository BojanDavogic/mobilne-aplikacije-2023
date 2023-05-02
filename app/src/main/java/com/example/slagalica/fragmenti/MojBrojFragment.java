package com.example.slagalica.fragmenti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.slagalica.R;
public class MojBrojFragment extends Fragment {

    ImageButton btnMojBrojDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
        btnMojBrojDalje = view.findViewById(R.id.mojBrojDalje);
        btnMojBrojDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpojniceFragment spojniceFragment = new SpojniceFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, spojniceFragment).commit();
            }
        });
        return view;
    }
}