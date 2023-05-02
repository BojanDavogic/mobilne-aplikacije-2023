package com.example.slagalica.pomocniAlati;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slagalica.R;
import com.example.slagalica.fragmenti.PocetniEkranFragment;
import com.example.slagalica.model.Prijatelj;

import java.util.List;

public class PrijateljiAdapter extends RecyclerView.Adapter<PrijateljiAdapter.ViewHolder> {
    private List<Prijatelj> prijateljiList;
    private Context context;

    public PrijateljiAdapter(List<Prijatelj> prijateljiList, Context context) {
        this.prijateljiList = prijateljiList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prijatelji_kartica, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Prijatelj prijatelj = prijateljiList.get(position);

        // Postavljanje slike
        holder.slika.setImageResource(prijatelj.getSlika());

        holder.naslov.setText(prijatelj.getIme());
        if(prijatelj.getAktivan() == true && prijatelj.getZauzet() == false){
            holder.aktivnost.setImageResource(R.drawable.aktivan_korisnik);
        } else if (prijatelj.getAktivan() == true && prijatelj.getZauzet() == true) {
            holder.aktivnost.setImageResource(R.drawable.zauzet_korisnik);
        } else {
            holder.aktivnost.setVisibility(View.INVISIBLE);
        }

        // Klik na karticu - ovde možete implementirati neku akciju na klik
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog popupDialog = new Dialog(view.getContext());
                popupDialog.setContentView(R.layout.prikaz_prijatelja_popup);
                popupDialog.setTitle("Prikaz prijatelja");
                ImageView slika = popupDialog.findViewById(R.id.popupSlika);
                TextView ime = popupDialog.findViewById(R.id.popupIme);
                TextView statusPrijatelja = popupDialog.findViewById(R.id.statusPrijatelja);
                AppCompatButton izazoviBtn = popupDialog.findViewById(R.id.izazovi);
                slika.setImageResource(prijatelj.getSlika());
                ime.setText(prijatelj.getIme());
                if(prijatelj.getAktivan() == true && prijatelj.getZauzet() == false){
                    statusPrijatelja.setText("Na mreži");

                } else if (prijatelj.getZauzet() == true && prijatelj.getZauzet() == true) {
                    statusPrijatelja.setText("Zauzet");
                    izazoviBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.pozadina));
                    izazoviBtn.setClickable(false);
                    statusPrijatelja.setTextColor(ContextCompat.getColor(context, R.color.crvena));
                } else {
                    izazoviBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.pozadina));
                    izazoviBtn.setClickable(false);
                    statusPrijatelja.setText("Nije na mreži");
                    statusPrijatelja.setTextColor(ContextCompat.getColor(context, R.color.siva));
                }


                popupDialog.show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return prijateljiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView slika;
        public ImageView aktivnost;
        public TextView naslov;

        public ViewHolder(View itemView) {
            super(itemView);

            slika = itemView.findViewById(R.id.prijateljiSlika);
            aktivnost = itemView.findViewById(R.id.aktivnost);
            naslov = itemView.findViewById(R.id.prijateljiIme);
        }
    }
}
