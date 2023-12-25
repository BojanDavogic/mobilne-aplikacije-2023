package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AsocijacijeFragment extends Fragment {

    TextView poeniLeviIgrac;

    SharedData sharedData = SharedData.getInstance();
    int prenetiPoeni = sharedData.getPoeniIgraca();
    public int poeniIgraca = 0;

    private FirebaseFirestore db;

    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 120000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;

    private TextView poljeA1, poljeA2, poljeA3, poljeA4;
    private TextView poljeB1, poljeB2, poljeB3, poljeB4;
    private TextView poljeC1, poljeC2, poljeC3, poljeC4;
    private TextView poljeD1, poljeD2, poljeD3, poljeD4;

    private EditText resenjeKoloneA, resenjeKoloneB, resenjeKoloneC, resenjeKoloneD;
    private EditText konacnoResenje;

    private ImageButton btnPotvrdi;

    private Map<String, Object> polja = new HashMap<>();
    private Map<String, String> resenjaKolona = new HashMap<>();
    private String konacnoResenjee;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asocijacije, container, false);

        db = FirebaseFirestore.getInstance();

        poeniLeviIgrac = view.findViewById(R.id.poeniLeviIgrac);
        poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));

        tajmerTextView = view.findViewById(R.id.tajmer);
        pokreniTajmerIgre();

        poljeA1 = view.findViewById(R.id.poljeA1);
        poljeA2 = view.findViewById(R.id.poljeA2);
        poljeA3 = view.findViewById(R.id.poljeA3);
        poljeA4 = view.findViewById(R.id.poljeA4);

        poljeB1 = view.findViewById(R.id.poljeB1);
        poljeB2 = view.findViewById(R.id.poljeB2);
        poljeB3 = view.findViewById(R.id.poljeB3);
        poljeB4 = view.findViewById(R.id.poljeB4);

        poljeC1 = view.findViewById(R.id.poljeC1);
        poljeC2 = view.findViewById(R.id.poljeC2);
        poljeC3 = view.findViewById(R.id.poljeC3);
        poljeC4 = view.findViewById(R.id.poljeC4);

        poljeD1 = view.findViewById(R.id.poljeD1);
        poljeD2 = view.findViewById(R.id.poljeD2);
        poljeD3 = view.findViewById(R.id.poljeD3);
        poljeD4 = view.findViewById(R.id.poljeD4);

        resenjeKoloneA = view.findViewById(R.id.resenjeKoloneA);
        resenjeKoloneB = view.findViewById(R.id.resenjeKoloneB);
        resenjeKoloneC = view.findViewById(R.id.resenjeKoloneC);
        resenjeKoloneD = view.findViewById(R.id.resenjeKoloneD);

        konacnoResenje = view.findViewById(R.id.konacnoResenje);

        btnPotvrdi = view.findViewById(R.id.btnPotvrdi);

        ucitajPoljaIzFirestore();

        poljeA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("a1");
                poljeA1.setEnabled(false);
                poljeA1.setTextColor(Color.WHITE);
            }
        });

        poljeA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("a2");
                poljeA2.setEnabled(false);
                poljeA2.setTextColor(Color.WHITE);
            }
        });

        poljeA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("a3");
                poljeA3.setEnabled(false);
                poljeA3.setTextColor(Color.WHITE);
            }
        });

        poljeA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("a4");
                poljeA4.setEnabled(false);
                poljeA4.setTextColor(Color.WHITE);
            }
        });

        poljeB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("b1");
                poljeB1.setEnabled(false);
                poljeB1.setTextColor(Color.WHITE);
            }
        });

        poljeB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("b2");
                poljeB2.setEnabled(false);
                poljeB2.setTextColor(Color.WHITE);
            }
        });

        poljeB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("b3");
                poljeB3.setEnabled(false);
                poljeB3.setTextColor(Color.WHITE);
            }
        });

        poljeB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("b4");
                poljeB4.setEnabled(false);
                poljeB4.setTextColor(Color.WHITE);
            }
        });

        poljeC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("c1");
                poljeC1.setEnabled(false);
                poljeC1.setTextColor(Color.WHITE);
            }
        });

        poljeC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("c2");
                poljeC2.setEnabled(false);
                poljeC2.setTextColor(Color.WHITE);
            }
        });

        poljeC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("c3");
                poljeC3.setEnabled(false);
                poljeC3.setTextColor(Color.WHITE);
            }
        });

        poljeC4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("c4");
                poljeC4.setEnabled(false);
                poljeC4.setTextColor(Color.WHITE);
            }
        });

        poljeD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("d1");
                poljeD1.setEnabled(false);
                poljeD1.setTextColor(Color.WHITE);
            }
        });

        poljeD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("d2");
                poljeD2.setEnabled(false);
                poljeD2.setTextColor(Color.WHITE);
            }
        });

        poljeD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("d3");
                poljeD3.setEnabled(false);
                poljeD3.setTextColor(Color.WHITE);
            }
        });

        poljeD4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziPolje("d4");
                poljeD4.setEnabled(false);
                poljeD4.setTextColor(Color.WHITE);
            }
        });

        btnPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unetoKonacnoResenje = konacnoResenje.getText().toString().trim();
                String unetoResenjeKoloneA = resenjeKoloneA.getText().toString().trim();
                String unetoResenjeKoloneB = resenjeKoloneB.getText().toString().trim();
                String unetoResenjeKoloneC = resenjeKoloneC.getText().toString().trim();
                String unetoResenjeKoloneD = resenjeKoloneD.getText().toString().trim();

                if (!unetoKonacnoResenje.isEmpty()) {
                    proveriKonacnoResenje(unetoKonacnoResenje);
                } if (!unetoResenjeKoloneA.isEmpty()) {
                    proveriResenje("a", unetoResenjeKoloneA, resenjeKoloneA);
                } if (!unetoResenjeKoloneB.isEmpty()) {
                    proveriResenje("b", unetoResenjeKoloneB, resenjeKoloneB);
                } if (!unetoResenjeKoloneC.isEmpty()) {
                    proveriResenje("c", unetoResenjeKoloneC, resenjeKoloneC);
                } if (!unetoResenjeKoloneD.isEmpty()) {
                    proveriResenje("d", unetoResenjeKoloneD, resenjeKoloneD);
                }
                int poeni = 0;
                if (proveriKonacnoResenje(unetoKonacnoResenje)) {
                    prenetiPoeni += 7;
                    poeni += 7;

                    if (poljeA1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeA2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeA3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeA4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (resenjeKoloneA.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                    }

                    if (poljeB1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeB2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeB3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeB4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (resenjeKoloneB.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                    }

                    if (poljeC1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeC2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeC3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeC4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (resenjeKoloneC.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                    }

                    if (poljeD1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeD2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeD3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (poljeD4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                    }

                    if (resenjeKoloneD.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                    }

                    int finalpoeni = poeni;

                    poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                    sharedData.setPoeniIgraca(prenetiPoeni);
//                    tajmerIgra.cancel();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prikaziObavestenje("Igra je zavrsena\nOsvojili ste " + finalpoeni + " poena\nSledeca igra pocinje za: ");
                        }
                    }, 3000);

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
                if (millisUntilFinished <= 10000) {
                    tajmerTextView.setTextColor(Color.RED);
                }
            }
            @Override
            public void onFinish() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prikaziObavestenje("Vreme je isteklo\nOsvojili ste 0 bodova\nSledeca igra pocinje za:");
                        prikaziSvaPolja();
                        prikaziSvaResenjaKolona();
                        prikaziKonacnoResenje();
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
        builder.setCancelable(true);

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
        db.collection("asocijacije")
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

                        db.collection("asocijacije").document(randomDokument)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            polja = document2.getData();
                                            resenjaKolona.put("a", polja.get("resenjeA").toString());
                                            resenjaKolona.put("b", polja.get("resenjeB").toString());
                                            resenjaKolona.put("c", polja.get("resenjeC").toString());
                                            resenjaKolona.put("d", polja.get("resenjeD").toString());
                                            konacnoResenjee = polja.get("konacnoResenje").toString();
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

    private void prikaziPolje(String polje) {
        String text = (String) polja.get(polje);  // Dobijanje teksta za odabrano polje
        TextView button = getButtonForPolje(polje);  // Dobijanje odgovarajućeg dugmeta za polje
        if (button != null && text != null) {
            button.setText(text);  // Postavljanje teksta na dugme
            button.setVisibility(View.VISIBLE);  // Prikazivanje dugmeta

        }
    }

    private void proveriResenje(String polje, String text, EditText resenje) {
        String resenjeKolone = resenjaKolona.get(polje);
        String resenjeKoloneText = getResenjeKoloneText(polje);

        if (text.equalsIgnoreCase(resenjeKolone)) {
            // Korisnik je pogodio resenje kolone
            prikaziKolonu(polje);
            resenje.setText(resenjeKolone);
            resenje.setEnabled(false);
            resenje.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            resenje.setTextColor(Color.BLACK);
        } else {
            prikaziPoruku("Niste pogodili resenje");
            resenje.setText("");
            resenje.clearFocus();
        }
    }

    private String getResenjeKoloneText(String polje) {
        switch (polje.charAt(0)) {
            case 'a':
                return resenjeKoloneA.getText().toString();
            case 'b':
                return resenjeKoloneB.getText().toString();
            case 'c':
                return resenjeKoloneC.getText().toString();
            case 'd':
                return resenjeKoloneD.getText().toString();
            default:
                return "";
        }
    }

    private void prikaziKolonu(String kolona) {
        // Prikazivanje svih polja iz odabrane kolone
        for (String polje : polja.keySet()) {
            if (polje.startsWith(kolona)) {
                prikaziPolje(polje);
            }
        }
    }

    private void prikaziPoruku(String poruka) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show();
    }

    private TextView getButtonForPolje(String polje) {
        String buttonId = "polje" + polje.toUpperCase();
        int resId = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
        return getView().findViewById(resId);
    }

    private boolean proveriKonacnoResenje(String unetoResenje) {

        if (unetoResenje.equalsIgnoreCase(konacnoResenjee)) {
            // Pogodak - prikazivanje svih polja i rešenja kolona
            tajmerIgra.cancel();
            prikaziKonacnoResenje();
            konacnoResenje.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            konacnoResenje.setTextColor(Color.BLACK);
            konacnoResenje.setEnabled(false);
            resenjeKoloneA.setEnabled(false);
            resenjeKoloneB.setEnabled(false);
            resenjeKoloneC.setEnabled(false);
            resenjeKoloneD.setEnabled(false);
            prikaziSvaPolja();
            prikaziSvaResenjaKolona();
            resenjeKoloneA.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            resenjeKoloneA.setTextColor(Color.BLACK);
            resenjeKoloneB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            resenjeKoloneB.setTextColor(Color.BLACK);
            resenjeKoloneC.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            resenjeKoloneC.setTextColor(Color.BLACK);
            resenjeKoloneD.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            resenjeKoloneD.setTextColor(Color.BLACK);
            prikaziPoruku("Pogodili ste konacno resenje!");
//            prikaziObavestenje("Pogodili ste konacno resenje");
            return true;
        } else {
            prikaziPoruku("Niste pogodili konacno resenje!");
            konacnoResenje.setText("");
            return false;
        }
    }

    private void prikaziKonacnoResenje() {
        konacnoResenje.setText(konacnoResenjee);
        konacnoResenje.setEnabled(false);
        konacnoResenje.setTextColor(Color.WHITE);
    }

    private void prikaziSvaPolja() {
        for (String polje : polja.keySet()) {
            prikaziPolje(polje);
        }
    }

    private void prikaziSvaResenjaKolona() {
        resenjeKoloneA.setText(resenjaKolona.get("a"));
        resenjeKoloneB.setText(resenjaKolona.get("b"));
        resenjeKoloneC.setText(resenjaKolona.get("c"));
        resenjeKoloneD.setText(resenjaKolona.get("d"));

        resenjeKoloneA.setEnabled(false);
        resenjeKoloneB.setEnabled(false);
        resenjeKoloneC.setEnabled(false);
        resenjeKoloneD.setEnabled(false);

        resenjeKoloneA.setTextColor(Color.WHITE);
        resenjeKoloneB.setTextColor(Color.WHITE);
        resenjeKoloneC.setTextColor(Color.WHITE);
        resenjeKoloneD.setTextColor(Color.WHITE);


    }

    public void prikaziSkockoFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.igreSlagaliceContainer, new SkockoFragment());
        transaction.commit();
    }
}