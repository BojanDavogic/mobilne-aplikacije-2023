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

import com.example.slagalica.R;
import com.example.slagalica.model.Korisnik;
import com.example.slagalica.servisi.KorisnikServis;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {


    KorisnikServis korisnikServis = new KorisnikServis();
    EditText email, korisnickoIme, lozinka, ponovljenaLozinka;
    boolean vidljivostLozinke, vidljivostPonovljeneLozinke;
    private Button btnRegistrujSe;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView tvPrijaviSe = findViewById(R.id.btnPrijaviSe);
        tvPrijaviSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        email = findViewById(R.id.txtEmail);
        korisnickoIme = findViewById(R.id.txtKorisnickoIme);
        lozinka = findViewById(R.id.txtLozinka);
        ponovljenaLozinka = findViewById(R.id.txtPonovljenaLozinka);
        btnRegistrujSe = findViewById(R.id.btnRegister);
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

        ponovljenaLozinka.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Desno = 2;
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX()>=ponovljenaLozinka.getRight()-ponovljenaLozinka.getCompoundDrawables()[Desno].getBounds().width()){
                        int selektovan = ponovljenaLozinka.getSelectionEnd();
                        if(vidljivostLozinke){
                            ponovljenaLozinka.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                            ponovljenaLozinka.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            vidljivostLozinke = false;
                        } else{
                            ponovljenaLozinka.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_on, 0);
                            ponovljenaLozinka.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            vidljivostLozinke = true;
                        }
                        ponovljenaLozinka.setSelection(selektovan);
                        return true;
                    }
                }
                return false;
            }
        });

        btnRegistrujSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString();
                String korisnickoImeStr = korisnickoIme.getText().toString();
                String lozinkaStr = lozinka.getText().toString();
                String ponovljenaLozinkaStr = ponovljenaLozinka.getText().toString();

                // Validacija podataka
                if (lozinkaStr.equals(ponovljenaLozinkaStr) && validacijaPodataka(emailStr, korisnickoImeStr, lozinkaStr)) {
                    // Kreiranje korisnika
                    Korisnik noviKorisnik = new Korisnik(emailStr, korisnickoImeStr, lozinkaStr);

                    // Dodavanje korisnika u bazu
                    boolean uspeh = korisnikServis.registrujKorisnika(noviKorisnik);
                    if (uspeh) {
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        Toast.makeText(Register.this, "Uspešno ste se registrovali", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Registracija nije uspela", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Prikazivanje odgovarajuće poruke u slučaju neispravnih podataka
                    if (!lozinkaStr.equals(ponovljenaLozinkaStr)) {
                        Toast.makeText(Register.this, "Lozinke se ne poklapaju", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Uneseni podaci nisu validni", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Metoda za validaciju podataka
    private boolean validacijaPodataka(String email, String korisnickoIme, String lozinka) {
        // Validacija email adrese
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Register.this, "Neispravna email adresa", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validacija korisničkog imena
        if (!Pattern.matches("[._]*[A-Za-z]{3,}[\\w._]*", korisnickoIme)) {
            Toast.makeText(Register.this, "Neispravno korisničko ime", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validacija lozinke
        if (lozinka.length() < 6) {
            Toast.makeText(Register.this, "Lozinka mora imati najmanje 6 karaktera", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Ako sve prođe validaciju, vraćamo true
    }

}