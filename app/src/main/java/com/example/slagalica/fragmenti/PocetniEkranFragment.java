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
import androidx.appcompat.widget.AppCompatButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    private String leviIgrac;
    private String desniIgrac;
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

                // Emitujte zahtev za pridruživanje igri
                MainActivity.socket.emit("joinGame", ulogovaniKorisnik.getKorisnickoIme());
                MainActivity.socket.on("playerJoined", args -> {
                    leviIgrac = args[0].toString();
                    desniIgrac = args[1].toString();
                });
                // Čekajte na događaj "startGame" da biste dobili informacije o protivniku
                MainActivity.socket.on("startGame", args -> {
                    if (args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject data = (JSONObject) args[0];

                        // Postavite informacije o levom igraču (trenutno ulogovanom korisniku)
                        Bundle bundle = new Bundle();
                        bundle.putString("korisnickoImeLeviIgrac", ulogovaniKorisnik.getKorisnickoIme());
                        bundle.putInt("poeniLeviIgrac", 0);

                        // Dobavljanje informacija o desnom igraču
                        for (Iterator<String> it = data.keys(); it.hasNext();) {
                            String socketId = it.next();
                            try {
                                String opponentUsername = data.getString(socketId);
                                if (!opponentUsername.equals(ulogovaniKorisnik.getKorisnickoIme())) {
                                    // Dodajte informacije o desnom igraču u Bundle
                                    bundle.putString("korisnickoImeDesniIgrac", opponentUsername);
                                    bundle.putInt("poeniDesniIgrac", 0);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // Postavite Bundle u Intent
                        Intent intent = new Intent(getActivity(), IgreSlagalice.class);
                        intent.putExtras(bundle);

                        // Pokrenite aktivnost
                        startActivity(intent);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
