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
import java.util.Comparator;
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

    private ImageButton rezultatRed1, rezultatRed2, rezultatRed3, rezultatRed4;

    private int trenutniRed = 1;
    private int brojTacnihMesta = 0;
    private int brojPogresnihMesta = 0;
    private List<String> dobitnaKombinacija = new ArrayList<>();
    private List<String> trenutnaKombinacija = new ArrayList<>();

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

        petiRed1 = view.findViewById(R.id.petiRed1);
        petiRed2 = view.findViewById(R.id.petiRed2);
        petiRed3 = view.findViewById(R.id.petiRed3);
        petiRed4 = view.findViewById(R.id.petiRed4);

        sestiRed1 = view.findViewById(R.id.sestiRed1);
        sestiRed2 = view.findViewById(R.id.sestiRed2);
        sestiRed3 = view.findViewById(R.id.sestiRed3);
        sestiRed4 = view.findViewById(R.id.sestiRed4);

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

        red5Krug1 = view.findViewById(R.id.red5Krug1);
        red5Krug2 = view.findViewById(R.id.red5Krug2);
        red5Krug3 = view.findViewById(R.id.red5Krug3);
        red5Krug4 = view.findViewById(R.id.red5Krug4);

        red6Krug1 = view.findViewById(R.id.red6Krug1);
        red6Krug2 = view.findViewById(R.id.red6Krug2);
        red6Krug3 = view.findViewById(R.id.red6Krug3);
        red6Krug4 = view.findViewById(R.id.red6Krug4);

        rezultatRed1 = view.findViewById(R.id.rezultatRed1);
        rezultatRed2 = view.findViewById(R.id.rezultatRed2);
        rezultatRed3 = view.findViewById(R.id.rezultatRed3);
        rezultatRed4 = view.findViewById(R.id.rezultatRed4);

        pokreniTajmerIgre();

        db = FirebaseFirestore.getInstance();
        ucitajPoljaIzFirestore();

        postaviSlusaceDogadjaja();

        return view;
    }

    private void postaviSlusaceDogadjaja() {
        skocko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("skocko");
            }
        });

        tref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("tref");
            }
        });

        pik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("pik");
            }
        });

        srce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("srce");
            }
        });

        karo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("karo");
            }
        });

        zvezda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dodajElementKombinaciji("zvezda");
            }
        });
    }

    private void postaviTrenutnuSlikuNaImageButton(String resurs) {
        ImageButton trenutniRedImageButton1 = getTrenutniRedImageButton1();
        ImageButton trenutniRedImageButton2 = getTrenutniRedImageButton2();
        ImageButton trenutniRedImageButton3 = getTrenutniRedImageButton3();
        ImageButton trenutniRedImageButton4 = getTrenutniRedImageButton4();

        if (trenutniRedImageButton1 != null) {
            postaviSlikuNaImageButton(trenutniRedImageButton1, resurs);
        }
        if (trenutniRedImageButton2 != null) {
            postaviSlikuNaImageButton(trenutniRedImageButton2, resurs);
        }
        if (trenutniRedImageButton3 != null) {
            postaviSlikuNaImageButton(trenutniRedImageButton3, resurs);
        }
        if (trenutniRedImageButton4 != null) {
            postaviSlikuNaImageButton(trenutniRedImageButton4, resurs);
        }
    }

    private void dodajElementKombinaciji(String element) {
        if (trenutnaKombinacija.size() < 4) {
            trenutnaKombinacija.add(element);
            ImageButton trenutniRedImageButton1 = getTrenutniRedImageButton1();
            ImageButton trenutniRedImageButton2 = getTrenutniRedImageButton2();
            ImageButton trenutniRedImageButton3 = getTrenutniRedImageButton3();
            ImageButton trenutniRedImageButton4 = getTrenutniRedImageButton4();

            switch (trenutnaKombinacija.size()) {
                case 1:
                    postaviSlikuNaImageButton(trenutniRedImageButton1, trenutnaKombinacija.get(0));
                    break;
                case 2:
                    postaviSlikuNaImageButton(trenutniRedImageButton2, trenutnaKombinacija.get(1));
                    break;
                case 3:
                    postaviSlikuNaImageButton(trenutniRedImageButton3, trenutnaKombinacija.get(2));
                    break;
                case 4:
                    postaviSlikuNaImageButton(trenutniRedImageButton4, trenutnaKombinacija.get(3));
                    break;
            }
        }
        if (trenutnaKombinacija.size() == 4) {
            proveriKombinaciju();
        }
    }

    private void proveriKombinaciju() {
        List<String> dobitnaKopija = new ArrayList<>(dobitnaKombinacija);
        List<String> krugovi = new ArrayList<>();
        brojTacnihMesta = 0;
        brojPogresnihMesta = 0;

        dobitnaKombinacija.clear();
        dobitnaKombinacija.add("skocko");
        dobitnaKombinacija.add("skocko");
        dobitnaKombinacija.add("srce");
        dobitnaKombinacija.add("srce");


        for (int i = 0; i < 4; i++) {
            if (trenutnaKombinacija.get(i).equals(dobitnaKombinacija.get(i))) {
                krugovi.add("crvena");
                dobitnaKopija.remove(trenutnaKombinacija.get(i));
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!trenutnaKombinacija.get(i).equals(dobitnaKombinacija.get(i)) && dobitnaKopija.contains(trenutnaKombinacija.get(i))) {
                krugovi.add("zuta");
                dobitnaKopija.remove(trenutnaKombinacija.get(i));
            }
        }

        krugovi.sort(Comparator.naturalOrder());

        for (int i = 0; i < krugovi.size(); i++) {
            if (i == 0) {
                switch (trenutniRed) {
                    case 1:
                        if (krugovi.get(i).equals("crvena"))
                            red1Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red1Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 2:
                        if (krugovi.get(i).equals("crvena"))
                            red2Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red2Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 3:
                        if (krugovi.get(i).equals("crvena"))
                            red3Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red3Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 4:
                        if (krugovi.get(i).equals("crvena"))
                            red4Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red4Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 5:
                        if (krugovi.get(i).equals("crvena"))
                            red5Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red5Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 6:
                        if (krugovi.get(i).equals("crvena"))
                            red6Krug1.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red6Krug1.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                }
            } else if (i == 1) {
                switch (trenutniRed) {
                    case 1:
                        if (krugovi.get(i).equals("crvena"))
                            red1Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red1Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 2:
                        if (krugovi.get(i).equals("crvena"))
                            red2Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red2Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 3:
                        if (krugovi.get(i).equals("crvena"))
                            red3Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red3Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 4:
                        if (krugovi.get(i).equals("crvena"))
                            red4Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red4Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 5:
                        if (krugovi.get(i).equals("crvena"))
                            red5Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red5Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 6:
                        if (krugovi.get(i).equals("crvena"))
                            red6Krug2.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red6Krug2.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                }
            } else if (i == 2) {
                switch (trenutniRed) {
                    case 1:
                        if (krugovi.get(i).equals("crvena"))
                            red1Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red1Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 2:
                        if (krugovi.get(i).equals("crvena"))
                            red2Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red2Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 3:
                        if (krugovi.get(i).equals("crvena"))
                            red3Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red3Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 4:
                        if (krugovi.get(i).equals("crvena"))
                            red4Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red4Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 5:
                        if (krugovi.get(i).equals("crvena"))
                            red5Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red5Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 6:
                        if (krugovi.get(i).equals("crvena"))
                            red6Krug3.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red6Krug3.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                }
            } else if (i == 3) {
                switch (trenutniRed) {
                    case 1:
                        if (krugovi.get(i).equals("crvena"))
                            red1Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red1Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 2:
                        if (krugovi.get(i).equals("crvena"))
                            red2Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red2Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 3:
                        if (krugovi.get(i).equals("crvena"))
                            red3Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red3Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 4:
                        if (krugovi.get(i).equals("crvena"))
                            red4Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red4Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 5:
                        if (krugovi.get(i).equals("crvena"))
                            red5Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red5Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                    case 6:
                        if (krugovi.get(i).equals("crvena"))
                            red6Krug4.setBackgroundResource(R.drawable.crveni_krug);
                        else if (krugovi.get(i).equals("zuta"))
                            red6Krug4.setBackgroundResource(R.drawable.zuti_krug);
                        break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (trenutnaKombinacija.get(i).equals(dobitnaKombinacija.get(i)))
                brojTacnihMesta++;
        }
        int poeni = 0;
        if (brojTacnihMesta == 4) {
            switch (trenutniRed) {
                case 1:
                case 2:
                    prenetiPoeni += 20;
                    poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                    poeni = 20;
                    break;
                case 3:
                case 4:
                    prenetiPoeni += 15;
                    poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                    poeni = 15;
                    break;
                case 5:
                case 6:
                    prenetiPoeni += 10;
                    poeniLeviIgrac.setText(String.valueOf(prenetiPoeni));
                    poeni = 10;
                    break;
            }
            sharedData.setPoeniIgraca(prenetiPoeni);
            tajmerIgra.cancel();
            prikaziObavestenje("Igra je zavrsena\nOsvojili ste " + poeni + " poena\nSledeca igra pocinje za: ");
        }

        trenutniRed++;
        resetujTrenutnuKombinaciju();

        if (trenutniRed > 6) {
            zavrsiIgru();
        }

        if(brojTacnihMesta == 4){
            prikaziRezultat();
            tajmerIgra.cancel();
        }
    }
//        resetujTrenutnuKombinaciju();
//        if (trenutniRed == 6) {
//            // Ako je popunjen šesti red, prikaži rezultat i završi igru
//            prikaziRezultat();
//            prikaziObavestenje("Niste uspeli");
//        } else {
//            // Inače, prikaži sledeći red
//            prikaziSledeciRed();
//        }
//    }

    private void resetujTrenutnuKombinaciju() {
        trenutnaKombinacija.clear();
    }


    private void prikaziSledeciRed() {
        trenutniRed++;

        if (trenutniRed > 6) {
            zavrsiIgru();
        }
    }

    private void zavrsiIgru() {
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
                prikaziRezultat();
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
                prikaziKorakPoKorakFragment();
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
                        String randomDokument = dokumenti.get(1);

                        db.collection("skocko").document(randomDokument)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            polja = document2.getData();
                                            for (String key : polja.keySet()) {
                                                String value = (String) polja.get(key);
                                                dobitnaKombinacija.add(value);
                                            }
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

    private ImageButton getTrenutniRedImageButton1() {
        switch (trenutniRed) {
            case 1:
                return prviRed1;
            case 2:
                return drugiRed1;
            case 3:
                return treciRed1;
            case 4:
                return cetvrtiRed1;
            case 5:
                return petiRed1;
            case 6:
                return sestiRed1;
            default:
                return null;
        }
    }

    private ImageButton getTrenutniRedImageButton2() {
        switch (trenutniRed) {
            case 1:
                return prviRed2;
            case 2:
                return drugiRed2;
            case 3:
                return treciRed2;
            case 4:
                return cetvrtiRed2;
            case 5:
                return petiRed2;
            case 6:
                return sestiRed2;
            default:
                return null;
        }
    }

    private ImageButton getTrenutniRedImageButton3() {
        switch (trenutniRed) {
            case 1:
                return prviRed3;
            case 2:
                return drugiRed3;
            case 3:
                return treciRed3;
            case 4:
                return cetvrtiRed3;
            case 5:
                return petiRed3;
            case 6:
                return sestiRed3;
            default:
                return null;
        }
    }

    private ImageButton getTrenutniRedImageButton4() {
        switch (trenutniRed) {
            case 1:
                return prviRed4;
            case 2:
                return drugiRed4;
            case 3:
                return treciRed4;
            case 4:
                return cetvrtiRed4;
            case 5:
                return petiRed4;
            case 6:
                return sestiRed4;
            default:
                return null;
        }
    }

    private void postaviSlikuNaImageButton(ImageButton imageButton, String resurs) {
        switch (resurs) {
            case "skocko":
                imageButton.setImageResource(R.drawable.skocko);
                break;
            case "tref":
                imageButton.setImageResource(R.drawable.tref);
                break;
            case "pik":
                imageButton.setImageResource(R.drawable.pik);
                break;
            case "srce":
                imageButton.setImageResource(R.drawable.srce);
                break;
            case "karo":
                imageButton.setImageResource(R.drawable.karo);
                break;
            case "zvezda":
                imageButton.setImageResource(R.drawable.zvezda);
                break;
        }
    }

    private void prikaziRezultat() {
        if (polja != null) {
            postaviSlikuNaImageButton(rezultatRed1, dobitnaKombinacija.get(0));
            postaviSlikuNaImageButton(rezultatRed2, dobitnaKombinacija.get(1));
            postaviSlikuNaImageButton(rezultatRed3, dobitnaKombinacija.get(2));
            postaviSlikuNaImageButton(rezultatRed4, dobitnaKombinacija.get(3));


        }
    }

    private void postaviSlikuNaImageButton(ImageButton imageButton, Object resurs) {
        if (resurs instanceof String) {
            String imeSlike = (String) resurs;
            int resursId = getResources().getIdentifier(imeSlike, "drawable", requireContext().getPackageName());
            if (resursId != 0) {
                imageButton.setImageResource(resursId);
            }
        }
    }

    public void prikaziKorakPoKorakFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.igreSlagaliceContainer, new KorakPoKorakFragment());
        transaction.commitAllowingStateLoss();
    }
}