package com.example.slagalica.aktivnosti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.slagalica.R;

public class Register extends AppCompatActivity {

    EditText email, lozinka, ponovljenaLozinka;
    boolean vidljivostLozinke, vidljivostPonovljeneLozinke;

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
        lozinka = findViewById(R.id.txtLozinka);
        ponovljenaLozinka = findViewById(R.id.txtPonovljenaLozinka);
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
    }
}