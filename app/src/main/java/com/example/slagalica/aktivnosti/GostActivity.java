package com.example.slagalica.aktivnosti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slagalica.R;

public class GostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocetni_ekran);

//        Button pokreniIgru = (Button) findViewById(R.id.zapocniIgruBtn);
//        pokreniIgru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GostActivity.this, IgreSlagalice.class);
//                startActivity(intent);
//            }
//        });
    }
}
