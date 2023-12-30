package com.example.slagalica.aktivnosti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.slagalica.R;
import com.example.slagalica.fragmenti.AsocijacijeFragment;
import com.example.slagalica.fragmenti.KoZnaZnaFragment;
import com.example.slagalica.fragmenti.MojBrojFragment;
import com.example.slagalica.fragmenti.PocetniEkranFragment;
import com.example.slagalica.fragmenti.SkockoFragment;
import com.example.slagalica.fragmenti.SpojniceFragment;
import com.example.slagalica.konfiguracija.SocketHandler;

import io.socket.client.Socket;

public class IgreSlagalice extends AppCompatActivity {

    public static Socket socket;

    private int brojIgranja = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igre_slagalice);

        socket = SocketHandler.getSocket();
        socket.connect();

        System.out.println("SOCKET IGRE SLAGALICE: " + socket);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String korisnickoImeLeviIgrac = bundle.getString("korisnickoImeLeviIgrac", "");
            String korisnickoImeDesniIgrac = bundle.getString("korisnickoImeDesniIgrac", "");
            int poeniLeviIgrac = bundle.getInt("poeniLeviIgrac", 0);
            int poeniDesniIgrac = bundle.getInt("poeniDesniIgrac", 0);

            // Sada možete postaviti ove podatke gde je potrebno, na primer u fragment
            AsocijacijeFragment asocijacijeFragment = new AsocijacijeFragment();
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
            fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
            fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
            fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
            asocijacijeFragment.setArguments(fragmentBundle);

            getSupportFragmentManager().beginTransaction().add(R.id.igreSlagaliceContainer, asocijacijeFragment).commitAllowingStateLoss();
        }
    }
}