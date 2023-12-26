package com.example.slagalica.aktivnosti;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.slagalica.MainActivity;
import com.example.slagalica.R;
import com.example.slagalica.model.Korisnik;
import com.example.slagalica.servisi.KorisnikServis;

import java.util.Map;

public class Login extends AppCompatActivity {
    KorisnikServis korisnikServis = new KorisnikServis();

    EditText email, lozinka;
    boolean vidljivostLozinke;
    Button btnLogin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.txtEmail);
        lozinka = findViewById(R.id.txtLozinka);
        btnLogin = findViewById(R.id.btnLogin);

        lozinka.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Desno = 2;
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX()>=lozinka.getRight()-lozinka.getCompoundDrawables()[Desno].getBounds().width()){
                        int selektovan = lozinka.getSelectionEnd();
                        if(vidljivostLozinke){
                            lozinka.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                            lozinka.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            vidljivostLozinke = false;
                        } else{
                            lozinka.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_on, 0);
                            lozinka.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            vidljivostLozinke = true;
                        }
                        lozinka.setSelection(selektovan);
                        return true;
                    }
                }
                return false;
            }
        });

        TextView tvRegistrujSe = findViewById(R.id.btnRegistrujSe);
        tvRegistrujSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        TextView tvNastaviKaoGost = findViewById(R.id.btnGost);
        tvNastaviKaoGost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identifikatorPrijave = email.getText().toString();
                String unetaLozinka = lozinka.getText().toString();

                boolean uspeh = korisnikServis.prijaviKorisnika(identifikatorPrijave, unetaLozinka);

                if (uspeh) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Uspesno ste se prijavili!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Login.this, "Pogresni podaci za prijavu.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}