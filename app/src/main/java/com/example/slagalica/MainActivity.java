package com.example.slagalica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.example.slagalica.fragmenti.MesecnaListaFragment;
import com.example.slagalica.fragmenti.NedeljnaListaFragment;
import com.example.slagalica.fragmenti.PocetniEkranFragment;
import com.example.slagalica.fragmenti.PregledProfilaFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
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
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}