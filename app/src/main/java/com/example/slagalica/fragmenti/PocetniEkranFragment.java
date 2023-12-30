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

import com.example.slagalica.MainActivity;
import com.example.slagalica.R;
import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.konfiguracija.SocketHandler;
import com.example.slagalica.model.Korisnik;
import com.example.slagalica.model.Prijatelj;
import com.example.slagalica.pomocniAlati.PrijateljiAdapter;
import com.example.slagalica.servisi.KorisnikServis;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

public class PocetniEkranFragment extends Fragment {
    public static Socket socket;
    private Bundle korisnikBundle;
    KorisnikServis korisnikServis = new KorisnikServis();
    Korisnik ulogovaniKorisnik = korisnikServis.getTrenutnoUlogovaniKorisnik();
    private RecyclerView recyclerView;
    private List<Prijatelj> listaPrijatelja;
    private PrijateljiAdapter prijateljiAdapter;
    CardView cardView;
    Dialog popupDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pocetni_ekran, container, false);
        if(ulogovaniKorisnik != null) {

//            socket = SocketHandler.getSocket();
//            socket.connect();

            System.out.println(ulogovaniKorisnik.getKorisnickoIme());

            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

            listaPrijatelja = new ArrayList<>();
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Aleksandar", true, false));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Nikola", true, false));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Marko", true, true));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Stevan", true, true));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Bojan", true, false));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Nemanja", false, false));
            listaPrijatelja.add(new Prijatelj(R.drawable.human_face, "Tomislav", false, false));

            prijateljiAdapter = new PrijateljiAdapter(listaPrijatelja, getActivity());
            recyclerView.setAdapter(prijateljiAdapter);
        }

        Button pokreniIgru = (Button) view.findViewById(R.id.zapocniIgruBtn);
        pokreniIgru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog popupDialog = new Dialog(getActivity());
                popupDialog.setContentView(R.layout.loading_popup);
                popupDialog.show();
                Intent intent = new Intent(getActivity(), IgreSlagalice.class);

                Bundle bundle = new Bundle();
                bundle.putString("korisnickoImeLeviIgrac", ulogovaniKorisnik.getKorisnickoIme());
                bundle.putString("korisnickoImeDesniIgrac", "ImeDesnogIgraca");
                bundle.putInt("poeniLeviIgrac", 0);
                bundle.putInt("poeniDesniIgrac", 0);

                // Postavite Bundle u Intent
                intent.putExtras(bundle);

                MainActivity.socket.emit("joinGame", ulogovaniKorisnik.getKorisnickoIme());
                System.out.println("SOCKET " + socket);
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
