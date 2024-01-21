package com.example.slagalica.aktivnosti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import io.socket.emitter.Emitter;

public class IgreSlagalice extends AppCompatActivity {

    public static Socket socket;

    private int runda = 1;

    public int getRunda() {
        return runda;
    }

    public void setRunda(int runda) {
        this.runda = runda;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igre_slagalice);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && !bundle.getString("korisnickoImeLeviIgrac").equals("")){
            socket = SocketHandler.getSocket();
            socket.connect();
        }
        KoZnaZnaFragment koZnaZnaFragment = new KoZnaZnaFragment();
        SpojniceFragment spojniceFragment = new SpojniceFragment();
        AsocijacijeFragment asocijacijeFragment = new AsocijacijeFragment();
        MojBrojFragment mojBrojFragment = new MojBrojFragment();

        if (bundle != null) {
            String korisnickoImeLeviIgrac = bundle.getString("korisnickoImeLeviIgrac", "");
            String korisnickoImeDesniIgrac = bundle.getString("korisnickoImeDesniIgrac", "");
            int poeniLeviIgrac = bundle.getInt("poeniLeviIgrac", 0);
            int poeniDesniIgrac = bundle.getInt("poeniDesniIgrac", 0);

            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
            fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
            fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
            fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
            koZnaZnaFragment.setArguments(fragmentBundle);
            spojniceFragment.setArguments(fragmentBundle);
            asocijacijeFragment.setArguments(fragmentBundle);

        }
        getSupportFragmentManager().beginTransaction().add(R.id.igreSlagaliceContainer, koZnaZnaFragment).commitAllowingStateLoss();

    }
}