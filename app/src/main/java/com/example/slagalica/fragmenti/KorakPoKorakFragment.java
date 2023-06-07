package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slagalica.R;
import com.example.slagalica.pomocniAlati.SharedData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KorakPoKorakFragment extends Fragment {

    TextView poeniLeviIgrac;

    SharedData sharedData = SharedData.getInstance();
    int prenetiPoeni = sharedData.getPoeniIgraca();

    private FirebaseFirestore db;

    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 70000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;

    private TextView polje1;
    private TextView polje2;
    private TextView polje3;
    private TextView polje4;
    private TextView polje5;
    private TextView polje6;
    private TextView polje7;
    private EditText resenje;

    private ImageButton btnPotvrdi;

    private Map<String, Object> polja;
    private int currentIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_korak_po_korak, container, false);
        poeniLeviIgrac = view.findViewById(R.id.poeniLeviIgrac);
        poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));

        tajmerTextView = view.findViewById(R.id.tajmer);
        pokreniTajmerIgre();

        polje1 = view.findViewById(R.id.polje1);
        polje2 = view.findViewById(R.id.polje2);
        polje3 = view.findViewById(R.id.polje3);
        polje4 = view.findViewById(R.id.polje4);
        polje5 = view.findViewById(R.id.polje5);
        polje6 = view.findViewById(R.id.polje6);
        polje7 = view.findViewById(R.id.polje7);
        resenje = view.findViewById(R.id.resenje);
        btnPotvrdi = view.findViewById(R.id.btnPotvrdi);

        db = FirebaseFirestore.getInstance();
        ucitajPoljaIzFirestore();

        btnPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unesenoResenje = resenje.getText().toString().trim();
                String tacnoResenje = polja.get("resenje") != null ? polja.get("resenje").toString().trim() : "";
                if (unesenoResenje.equalsIgnoreCase(tacnoResenje)) {
                    prikaziPolja();
                    resenje.setText(tacnoResenje);
                    resenje.setEnabled(false);
                    Toast.makeText(requireContext(), "Pogodili ste tacno resenje", Toast.LENGTH_SHORT).show();

                    // Zaustavi tajmer igre
                    if (tajmerIgra != null) {
                        tajmerIgra.cancel();
                    }

                    proveriUcitanoPolje();

                    prikaziPreostalaPolja();

                    // Dodeli poene na osnovu otvorenog polja
                    int poeni = 0;
                    switch (currentIndex - 1) {
                        case 1:
                            poeni = 20;
                            break;
                        case 2:
                            poeni = 18;
                            break;
                        case 3:
                            poeni = 16;
                            break;
                        case 4:
                            poeni = 14;
                            break;
                        case 5:
                            poeni = 12;
                            break;
                        case 6:
                            poeni = 10;
                            break;
                        case 7:
                            poeni = 8;
                            break;
                    }

                    // Dodaj poene korisniku
                    prenetiPoeni += poeni;
                    poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                    sharedData.setPoeniIgraca(prenetiPoeni);

                    // Prikazi obavestenje nakon 5 sekundi
                    int finalPoeni = poeni;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prikaziObavestenje("Pogodili ste!\nOsvojili ste " + finalPoeni + " poena");
                        }
                    }, 5000);
                } else {
                    Toast.makeText(requireContext(), "Niste pogodili tacno resenje", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    private void pokreniTajmerIgre() {
        tajmerIgra = new CountDownTimer(trajanjeIgre, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tajmerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prikaziObavestenje("Vreme je isteklo\nOsvojili ste 0 bodova\nSledeca igra pocinje za:");
                    }
                }, 2000);
            }
        };

        tajmerIgra.start();
    }

    private void prikaziObavestenje(String poruka) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Igra je zavrsena");
        builder.setMessage(poruka);
        builder.setCancelable(false);

        final TextView tajmerObavestenjeTextView = new TextView(requireContext());
        tajmerObavestenjeTextView.setTextSize(24);
        tajmerObavestenjeTextView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tajmerObavestenjeTextView.setLayoutParams(layoutParams);
        builder.setView(tajmerObavestenjeTextView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        tajmerPrikaz = new CountDownTimer(trajanjePrikaza, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tajmerObavestenjeTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                prikaziSkockoFragment();
            }
        };

        tajmerPrikaz.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tajmerIgra != null) {
            tajmerIgra.cancel();
        }
        if (tajmerPrikaz != null) {
            tajmerPrikaz.cancel();
        }
    }

    private void ucitajPoljaIzFirestore() {
        db.collection("korakPoKorak")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<String> dokumenti = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            dokumenti.add(document.getId());
                        }
                        int randomIndex = new Random().nextInt(dokumenti.size());
                        String randomDokument = dokumenti.get(randomIndex);

                        db.collection("korakPoKorak").document(randomDokument)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            polja = document2.getData();
                                            prikaziPolja(); // Prikazujemo sva polja
                                        } else {
                                            Log.d(TAG, "Dokument ne postoji");
                                        }
                                    } else {
                                        Log.e(TAG, "Greška prilikom dobavljanja dokumenata: ", task2.getException());
                                    }
                                });
                    } else {
                        Log.e(TAG, "Greška prilikom dobavljanja dokumenata: ", task.getException());
                    }
                });
    }



    private void prikaziPolja() {
        if (polja != null) {
            if (currentIndex < 8) {
                String kljuc = null;
                if (currentIndex < 7) {
                    kljuc = "polje" + (currentIndex + 1);
                    currentIndex++;
                } else if (currentIndex == 7) {
                    kljuc = "resenje";
                    currentIndex++;
                }

                if (polja.containsKey(kljuc)) {
                    Object polje = polja.get(kljuc);
                    TextView textView = null;
                    EditText editText = null;
                    switch (currentIndex) {
                        case 1:
                            textView = polje1;
                            break;
                        case 2:
                            textView = polje2;
                            break;
                        case 3:
                            textView = polje3;
                            break;
                        case 4:
                            textView = polje4;
                            break;
                        case 5:
                            textView = polje5;
                            break;
                        case 6:
                            textView = polje6;
                            break;
                        case 7:
                            textView = polje7;
                            break;
                        case 8:
                            editText = resenje;
                            break;
                    }
                    if (textView != null) {
                        textView.setText(String.valueOf(polje));
                    }
                    if (editText != null) {
                        editText.setVisibility(View.VISIBLE);
                        editText.setText(String.valueOf(polje));
                    }
                }

                if (currentIndex < 8) {
                    tajmerPrikaz = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            prikaziPolja();
                        }
                    };
                    tajmerPrikaz.start();
                }
            } else {
                // Ako su prikazana sva polja
                resenje.setEnabled(false);
                resenje.requestFocus();
            }
        }
    }

    private void prikaziPreostalaPolja() {
        if (polja != null && currentIndex < 8) {
            for (int i = currentIndex + 1; i <= 7; i++) {
                String kljuc = "polje" + i;
                if (polja.containsKey(kljuc)) {
                    Object polje = polja.get(kljuc);
                    TextView textView = null;
                    switch (i) {
                        case 1:
                            textView = polje1;
                            break;
                        case 2:
                            textView = polje2;
                            break;
                        case 3:
                            textView = polje3;
                            break;
                        case 4:
                            textView = polje4;
                            break;
                        case 5:
                            textView = polje5;
                            break;
                        case 6:
                            textView = polje6;
                            break;
                        case 7:
                            textView = polje7;
                            break;
                    }
                    if (textView != null && TextUtils.isEmpty(textView.getText().toString())) {
                        textView.setText(String.valueOf(polje));
                    }
                }
            }
        }
    }

    private void proveriUcitanoPolje() {
        String ucitanoPolje = null;
        switch (currentIndex) {
            case 1:
                ucitanoPolje = "polje1";
                break;
            case 2:
                ucitanoPolje = "polje2";
                break;
            case 3:
                ucitanoPolje = "polje3";
                break;
            case 4:
                ucitanoPolje = "polje4";
                break;
            case 5:
                ucitanoPolje = "polje5";
                break;
            case 6:
                ucitanoPolje = "polje6";
                break;
            case 7:
                ucitanoPolje = "polje7";
                break;
        }
        if (ucitanoPolje != null) {
            Log.d(TAG, "Učitano polje: " + ucitanoPolje);
            // Možete dodati dalju logiku ili obradu učitanog polja ovdje
        }
    }


    public void prikaziSkockoFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.igreSlagaliceContainer, new MojBrojFragment());
        transaction.commitAllowingStateLoss();
    }
}