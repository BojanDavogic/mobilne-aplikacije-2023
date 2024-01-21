package com.example.slagalica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.aktivnosti.Login;
import com.example.slagalica.aktivnosti.Register;
import com.example.slagalica.fragmenti.MesecnaListaFragment;
import com.example.slagalica.fragmenti.MojBrojFragment;
import com.example.slagalica.fragmenti.NedeljnaListaFragment;
import com.example.slagalica.fragmenti.PocetniEkranFragment;
import com.example.slagalica.fragmenti.PregledProfilaFragment;
import com.example.slagalica.fragmenti.SpojniceFragment;
import com.example.slagalica.fragmenti.StatistikaFragment;
import com.example.slagalica.konfiguracija.SocketHandler;
import com.example.slagalica.model.Korisnik;
import com.example.slagalica.servisi.KorisnikServis;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    public static Socket socket;
    private KorisnikServis korisnikServis = new KorisnikServis();

    private Bundle korisnikBundle;
    private Korisnik ulogovaniKorisnik = korisnikServis.getTrenutnoUlogovaniKorisnik();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView txtUsername, txtBrojTokena, txtBrojZvezdica;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        SocketHandler.setSocket();
        socket = SocketHandler.getSocket();
        socket.connect();

        if(socket != null){
            System.out.println("SOCKET JE KONEKTOVAN: " + socket);
//            socket.emit("joinGame", ulogovaniKorisnik.getKorisnickoIme());

//            Button pokreniIgru = findViewById(R.id.zapocniIgruBtn);
//
//            pokreniIgru.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Dialog popupDialog = new Dialog(MainActivity.this);
//                    popupDialog.setContentView(R.layout.loading_popup);
//                    popupDialog.show();
//                    Intent intent = new Intent(MainActivity.this, IgreSlagalice.class);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("korisnickoImeLeviIgrac", "ImeLevogIgraca");
//                    bundle.putString("korisnickoImeDesniIgrac", "ImeDesnogIgraca");
//                    bundle.putInt("poeniLeviIgrac", 0);
//                    bundle.putInt("poeniDesniIgrac", 0);
//
//                    // Postavite Bundle u Intent
//                    intent.putExtras(bundle);
//
//                    MainActivity.socket.emit("joinGame", ulogovaniKorisnik.getKorisnickoIme());
//                    System.out.println("SOCKET " + socket);
//                    startActivity(intent);
//                }
//            });

        }

        Bundle bundle = getIntent().getExtras();
        korisnikBundle = bundle;

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        headerView = navigationView.getHeaderView(0);
        txtUsername = headerView.findViewById(R.id.txtUsername);
        txtBrojTokena = headerView.findViewById(R.id.brojTokena);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        txtUsername.setText(ulogovaniKorisnik.getKorisnickoIme());
        txtBrojTokena.setText(String.valueOf(ulogovaniKorisnik.getTokeni()));
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PocetniEkranFragment()).commit();
            navigationView.setCheckedItem(R.id.pocetnaStranica);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.pocetnaStranica:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PocetniEkranFragment()).commit();
                        break;
                    case R.id.profil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PregledProfilaFragment()).commit();
                        break;
                    case R.id.nedeljnaLista:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NedeljnaListaFragment()).commit();
                        break;
                    case R.id.mesecnaLista:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MesecnaListaFragment()).commit();
                        break;
                    case R.id.statistika:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StatistikaFragment()).commit();
                        break;
                    case R.id.odjaviSe:
                        ulogovaniKorisnik = null;

                        if (socket != null && socket.connected()) {
                            socket.disconnect();
                            socket.off();
                        }

                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}