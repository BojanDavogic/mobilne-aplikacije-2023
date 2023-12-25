package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class SpojniceFragment extends Fragment {

    TextView poeniLeviIgrac;

    SharedData sharedData = SharedData.getInstance();
    int prenetiPoeni = sharedData.getPoeniIgraca();
    int poeni = 0;
    private FirebaseFirestore db;
    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 30000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;

    private TextView naslov;
    private AppCompatButton red1kolona1;
    private AppCompatButton red1kolona2;
    private AppCompatButton red2kolona1;
    private AppCompatButton red2kolona2;
    private AppCompatButton red3kolona1;
    private AppCompatButton red3kolona2;
    private AppCompatButton red4kolona1;
    private AppCompatButton red4kolona2;
    private AppCompatButton red5kolona1;
    private AppCompatButton red5kolona2;

    private Map<String, Object> polja;
    private int brojac = 0;

    private AppCompatButton poslednjeKliknutoIzKolone1;
    private AppCompatButton poslednjeKliknutoIzKolone2;


    ImageButton btnSpojniceDalje;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spojnice, container, false);
        poeniLeviIgrac = view.findViewById(R.id.poeniLeviIgrac);
        poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));

        tajmerTextView = view.findViewById(R.id.tajmer);

        naslov = view.findViewById(R.id.naslov);
        red1kolona1 = view.findViewById(R.id.red1kolona1);
        red1kolona2 = view.findViewById(R.id.red1kolona2);
        red2kolona1 = view.findViewById(R.id.red2kolona1);
        red2kolona2 = view.findViewById(R.id.red2kolona2);
        red3kolona1 = view.findViewById(R.id.red3kolona1);
        red3kolona2 = view.findViewById(R.id.red3kolona2);
        red4kolona1 = view.findViewById(R.id.red4kolona1);
        red4kolona2 = view.findViewById(R.id.red4kolona2);
        red5kolona1 = view.findViewById(R.id.red5kolona1);
        red5kolona2 = view.findViewById(R.id.red5kolona2);

        db = FirebaseFirestore.getInstance();
        ucitajPoljaIzFirestore();
        pokreniTajmerIgre();

//        onemoguciPoljaUKoloni();
//        onemoguciPolja();

        red1kolona1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                red1kolona1.requestFocusFromTouch();
                poslednjeKliknutoIzKolone1 = red1kolona1;
            }
        });

        red2kolona1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                red2kolona1.requestFocusFromTouch();
                poslednjeKliknutoIzKolone1 = red2kolona1;
            }
        });

        red3kolona1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                red3kolona1.requestFocusFromTouch();
                poslednjeKliknutoIzKolone1 = red3kolona1;
            }
        });

        red4kolona1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                red4kolona1.requestFocusFromTouch();
                poslednjeKliknutoIzKolone1 = red4kolona1;
            }
        });

        red5kolona1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                red5kolona1.requestFocusFromTouch();
                poslednjeKliknutoIzKolone1 = red5kolona1;
            }
        });

        red1kolona2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (poslednjeKliknutoIzKolone1 != null) {
                    proveriSpoj(poslednjeKliknutoIzKolone1, red1kolona2);
                    poslednjeKliknutoIzKolone1 = null;
                } else {
                    poslednjeKliknutoIzKolone2 = red1kolona2;
                }
            }
        });

        red2kolona2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (poslednjeKliknutoIzKolone1 != null) {
                    proveriSpoj(poslednjeKliknutoIzKolone1, red2kolona2);
                    poslednjeKliknutoIzKolone1 = null;
                } else {
                    poslednjeKliknutoIzKolone2 = red2kolona2;
                }
            }
        });

        red3kolona2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (poslednjeKliknutoIzKolone1 != null) {
                    proveriSpoj(poslednjeKliknutoIzKolone1, red3kolona2);
                    poslednjeKliknutoIzKolone1 = null;
                } else {
                    poslednjeKliknutoIzKolone2 = red3kolona2;
                }
            }
        });

        red4kolona2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (poslednjeKliknutoIzKolone1 != null) {
                    proveriSpoj(poslednjeKliknutoIzKolone1, red4kolona2);
                    poslednjeKliknutoIzKolone1 = null;
                } else {
                    poslednjeKliknutoIzKolone2 = red4kolona2;
                }
            }
        });

        red5kolona2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (poslednjeKliknutoIzKolone1 != null) {
                    proveriSpoj(poslednjeKliknutoIzKolone1, red5kolona2);
                    poslednjeKliknutoIzKolone1 = null;
                } else {
                    poslednjeKliknutoIzKolone2 = red5kolona2;
                }
            }
        });

        btnSpojniceDalje = view.findViewById(R.id.spojniceDalje);
        btnSpojniceDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkockoFragment skockoFragment = new SkockoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, skockoFragment).commit();
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
                        prenetiPoeni += poeni;
                        poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                        sharedData.setPoeniIgraca(prenetiPoeni);
                        prikaziObavestenje("Vreme je isteklo\nOsvojili ste " + poeni + " bodova\nSledeca igra pocinje za:");
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
                tajmerIgra.cancel();
                prikaziBodoveObavestenje();
            }
        };

        tajmerPrikaz.start();
    }

    private void prikaziBodoveObavestenje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Osvojeni bodovi");
        builder.setMessage("Osvojili ste ukupno " + prenetiPoeni + " poena.");

        builder.setPositiveButton("Ok", (dialog, which) -> {
            // Idi na početni ekran
            idiNaPocetniEkran();
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void idiNaPocetniEkran() {
        PocetniEkranFragment pocetniEkranFragment = new PocetniEkranFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, pocetniEkranFragment).commit();
    }

    private void prikaziOdgovor(String poruka) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("");
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

        tajmerPrikaz = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tajmerObavestenjeTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
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
        db.collection("spojnice")
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

                        db.collection("spojnice").document(randomDokument)
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
        naslov.setText(polja.get("naslov").toString());
        red1kolona1.setText(polja.get("red1kolona1").toString());
        red1kolona2.setText(polja.get("red1kolona2").toString());
        red2kolona1.setText(polja.get("red2kolona1").toString());
        red2kolona2.setText(polja.get("red2kolona2").toString());
        red3kolona1.setText(polja.get("red3kolona1").toString());
        red3kolona2.setText(polja.get("red3kolona2").toString());
        red4kolona1.setText(polja.get("red4kolona1").toString());
        red4kolona2.setText(polja.get("red4kolona2").toString());
        red5kolona1.setText(polja.get("red5kolona1").toString());
        red5kolona2.setText(polja.get("red5kolona2").toString());
    }

    private void proveriSpoj(AppCompatButton prvoDugme, AppCompatButton drugoDugme) {
        String spojeno1 = prvoDugme.getText().toString();
        String spojeno2 = drugoDugme.getText().toString();

        String tacanOdgovor = polja.get("tacanOdgovor").toString();

        String[] parovi = tacanOdgovor.split(" - ");

        boolean tacnoSpojeno = false;

        for (String tacanPar : parovi) {
            if ((spojeno1 + " " + spojeno2).equals(tacanPar) || (spojeno2 + " " + spojeno1).equals(tacanPar)) {
                tacnoSpojeno = true;
                break;
            }
        }

        if (tacnoSpojeno) {
            poeni += 2;
            prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
            drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
            prvoDugme.setEnabled(false);
            drugoDugme.setEnabled(false);
        } else {
            prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.wrong_answer));
            drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
            prvoDugme.setEnabled(false);
        }

        brojac+=1;

        if(brojac == 5){
            prenetiPoeni += poeni;
            poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
            sharedData.setPoeniIgraca(prenetiPoeni);

            prikaziObavestenje("Osvojili ste " + poeni + " poena");
        }
    }

    private void omoguciPoljaUKoloni() {
        red1kolona2.setClickable(true);
        red2kolona2.setClickable(true);
        red3kolona2.setClickable(true);
        red4kolona2.setClickable(true);
        red5kolona2.setClickable(true);
    }
    private void onemoguciPoljaUKoloni() {
        red1kolona2.setClickable(false);
        red2kolona2.setClickable(false);
        red3kolona2.setClickable(false);
        red4kolona2.setClickable(false);
        red5kolona2.setClickable(false);
    }

    private void onemoguciPolja() {
        red1kolona2.setEnabled(false);
        red2kolona2.setEnabled(false);
        red3kolona2.setEnabled(false);
        red4kolona2.setEnabled(false);
        red5kolona2.setEnabled(false);
    }

    private void omoguciPolja() {
        red1kolona2.setEnabled(true);
        red2kolona2.setEnabled(true);
        red3kolona2.setEnabled(true);
        red4kolona2.setEnabled(true);
        red5kolona2.setEnabled(true);
    }

}