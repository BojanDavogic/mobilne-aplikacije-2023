package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
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

public class SkockoFragment extends Fragment {

    TextView poeniLeviIgrac;

    SharedData sharedData = SharedData.getInstance();
    int prenetiPoeni = sharedData.getPoeniIgraca();

    private FirebaseFirestore db;

    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 30000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;

    private Map<String, Object> polja;

    private ImageButton skocko, tref, pik, srce, karo, zvezda;

    private ImageButton prviRed1, prviRed2, prviRed3, prviRed4;
    private View red1Krug1, red1Krug2, red1Krug3, red1Krug4;

    private ImageButton drugiRed1, drugiRed2, drugiRed3, drugiRed4;
    private View red2Krug1, red2Krug2, red2Krug3, red2Krug4;

    private ImageButton treciRed1, treciRed2, treciRed3, treciRed4;
    private View red3Krug1, red3Krug2, red3Krug3, red3Krug4;

    private ImageButton cetvrtiRed1, cetvrtiRed2, cetvrtiRed3, cetvrtiRed4;
    private View red4Krug1, red4Krug2, red4Krug3, red4Krug4;

    private ImageButton petiRed1, petiRed2, petiRed3, petiRed4;
    private View red5Krug1, red5Krug2, red5Krug3, red5Krug4;

    private ImageButton sestiRed1, sestiRed2, sestiRed3, sestiRed4;
    private View red6Krug1, red6Krug2, red6Krug3, red6Krug4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skocko, container, false);
        tajmerTextView = view.findViewById(R.id.tajmer);
        poeniLeviIgrac = view.findViewById(R.id.poeniLeviIgrac);
        poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));

        skocko = view.findViewById(R.id.skocko);
        tref = view.findViewById(R.id.tref);
        pik = view.findViewById(R.id.pik);
        srce = view.findViewById(R.id.srce);
        karo = view.findViewById(R.id.karo);
        zvezda = view.findViewById(R.id.zvezda);

        prviRed1 = view.findViewById(R.id.prviRed1);
        prviRed2 = view.findViewById(R.id.prviRed2);
        prviRed3 = view.findViewById(R.id.prviRed3);
        prviRed4 = view.findViewById(R.id.prviRed4);

        drugiRed1 = view.findViewById(R.id.drugiRed1);
        drugiRed2 = view.findViewById(R.id.drugiRed2);
        drugiRed3 = view.findViewById(R.id.drugiRed3);
        drugiRed4 = view.findViewById(R.id.drugiRed4);

        treciRed1 = view.findViewById(R.id.treciRed1);
        treciRed2 = view.findViewById(R.id.treciRed2);
        treciRed3 = view.findViewById(R.id.treciRed3);
        treciRed4 = view.findViewById(R.id.treciRed4);

        cetvrtiRed1 = view.findViewById(R.id.cetvrtiRed1);
        cetvrtiRed2 = view.findViewById(R.id.cetvrtiRed2);
        cetvrtiRed3 = view.findViewById(R.id.cetvrtiRed3);
        cetvrtiRed4 = view.findViewById(R.id.cetvrtiRed4);

        red1Krug1 = view.findViewById(R.id.red1Krug1);
        red1Krug2 = view.findViewById(R.id.red1Krug2);
        red1Krug3 = view.findViewById(R.id.red1Krug3);
        red1Krug4 = view.findViewById(R.id.red1Krug4);

        red2Krug1 = view.findViewById(R.id.red2Krug1);
        red2Krug2 = view.findViewById(R.id.red2Krug2);
        red2Krug3 = view.findViewById(R.id.red2Krug3);
        red2Krug4 = view.findViewById(R.id.red2Krug4);

        red3Krug1 = view.findViewById(R.id.red3Krug1);
        red3Krug2 = view.findViewById(R.id.red3Krug2);
        red3Krug3 = view.findViewById(R.id.red3Krug3);
        red3Krug4 = view.findViewById(R.id.red3Krug4);

        red4Krug1 = view.findViewById(R.id.red4Krug1);
        red4Krug2 = view.findViewById(R.id.red4Krug2);
        red4Krug3 = view.findViewById(R.id.red4Krug3);
        red4Krug4 = view.findViewById(R.id.red4Krug4);

        pokreniTajmerIgre();

        return view;
    }

    private void postaviSlusaceDogadjaja() {
        skocko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed1
            }
        });

        tref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed2
            }
        });

        pik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed3
            }
        });

        srce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed4
            }
        });

        karo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed4
            }
        });

        zvezda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementirajte odgovarajuću logiku za klik na prviRed4
            }
        });

        // Postavite slušače događaja za preostale ImageButton elemente
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
                prikaziAsocijacijeFragment();
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
        db.collection("skocko")
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

                        db.collection("skocko").document(randomDokument)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            polja = document2.getData();
//                                            prikaziPolja(); // Prikazujemo sva polja
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

    public void prikaziAsocijacijeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.igreSlagaliceContainer, new AsocijacijeFragment());
        transaction.commitAllowingStateLoss();
    }
}