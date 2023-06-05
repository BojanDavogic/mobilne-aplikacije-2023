package com.example.slagalica.aktivnosti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.slagalica.R;
import com.example.slagalica.fragmenti.MojBrojFragment;
import com.example.slagalica.fragmenti.PocetniEkranFragment;
import com.example.slagalica.fragmenti.SpojniceFragment;

public class IgreSlagalice extends AppCompatActivity {

    private int brojIgranja = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igre_slagalice);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.igreSlagaliceContainer, new MojBrojFragment()).commit();
    }
}