package com.example.slagalica.fragmenti;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slagalica.R;
import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.model.Prijatelj;
import com.example.slagalica.pomocniAlati.PrijateljiAdapter;

import java.util.ArrayList;
import java.util.List;

public class PocetniEkranFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Prijatelj> listaPrijatelja;
    private PrijateljiAdapter prijateljiAdapter;
    CardView cardView;
    Dialog popupDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pocetni_ekran, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        listaPrijatelja = new ArrayList<>();
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Aleksandar", true, false));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Nikola", true, false));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Marko", true, true));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Stevan", true, true));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Bojan",true, false));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Nemanja", false, false));
        listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Tomislav", false, false));

        prijateljiAdapter = new PrijateljiAdapter(listaPrijatelja, getActivity());
        recyclerView.setAdapter(prijateljiAdapter);

        Button pokreniIgru = (Button) view.findViewById(R.id.zapocniIgruBtn);
        pokreniIgru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IgreSlagalice.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
