package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slagalica.R;
import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.pomocniAlati.SharedData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KoZnaZnaFragment extends Fragment {

    TextView poeniLevogIgraca;
    TextView textUsernameRight;
    TextView textPoeniLeft;
    TextView textPoeniRight;

    TextView textUsernameLeft;
    SharedData sharedData = SharedData.getInstance();
    int prenetiPoeni = sharedData.getPoeniIgraca();
    private FirebaseFirestore db;
    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 25000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;
    private TextView pitanjeTextView;
    private TextView odgovor1TextView;
    private TextView odgovor2TextView;
    private TextView odgovor3TextView;
    private TextView odgovor4TextView;
    private AppCompatButton neZnamButton;
    private Map<String, Object> trenutnoPitanje;
    private List<Map<String, Object>> svaPitanja;
    private boolean prikazanOdgovor = false;
    private int trenutniIndeksPitanja = 0;
    private int poeni = 0;

    ImageButton btnKoZnaZnaDalje;
    private View view;

    String korisnickoImeLeviIgrac;
    String korisnickoImeDesniIgrac;
    int poeniLeviIgrac;
    int poeniDesniIgrac;
    int pokusaj = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ko_zna_zna, container, false);

        pitanjeTextView = view.findViewById(R.id.pitanje);
        odgovor1TextView = view.findViewById(R.id.odgovor1);
        odgovor2TextView = view.findViewById(R.id.odgovor2);
        odgovor3TextView = view.findViewById(R.id.odgovor3);
        odgovor4TextView = view.findViewById(R.id.odgovor4);
        neZnamButton = view.findViewById(R.id.koZnaZnaBtnNeZnam);

        tajmerTextView = view.findViewById(R.id.tajmer);
        svaPitanja = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        System.out.println("BUNDLE" + bundle);
        if (bundle != null || korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
            korisnickoImeLeviIgrac = bundle.getString("korisnickoImeLeviIgrac", "");
            korisnickoImeDesniIgrac = bundle.getString("korisnickoImeDesniIgrac", "");
            poeniLeviIgrac = bundle.getInt("poeniLeviIgrac", 0);
            poeniDesniIgrac = bundle.getInt("poeniDesniIgrac", 0);

            textUsernameLeft = view.findViewById(R.id.korisnickoImeLeviIgrac);
            textUsernameRight = view.findViewById(R.id.korisnickoImeDesniIgrac);
            textPoeniLeft = view.findViewById(R.id.poeniLeviIgrac);
            textPoeniRight = view.findViewById(R.id.poeniDesniIgrac);

            textUsernameLeft.setText(korisnickoImeLeviIgrac);
            textUsernameRight.setText(korisnickoImeDesniIgrac);
            textPoeniLeft.setText(String.valueOf(poeniLeviIgrac));
            textPoeniRight.setText(String.valueOf(poeniDesniIgrac));
        }
        else {
            ImageView desniIgracSlika = view.findViewById(R.id.desniIgracSlika);
            desniIgracSlika.setImageResource(0);
            textUsernameLeft = view.findViewById(R.id.korisnickoImeLeviIgrac);
            textUsernameLeft.setText("Gost");
            textPoeniLeft = view.findViewById(R.id.poeniLeviIgrac);
            textPoeniLeft.setText(String.valueOf(prenetiPoeni));
        }
        ucitajPoljaIzFirestore();

//        pokreniTajmerIgre();
//        pokreniTajmerPromenePitanja();

        odgovor1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proveriOdgovor(odgovor1TextView);
            }
        });

        odgovor2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proveriOdgovor(odgovor2TextView);
            }
        });

        odgovor3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proveriOdgovor(odgovor3TextView);
            }
        });

        odgovor4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proveriOdgovor(odgovor4TextView);
            }
        });


        neZnamButton.setOnClickListener(v -> {
            if (trenutniIndeksPitanja < svaPitanja.size()) {
                trenutnoPitanje = svaPitanja.get(trenutniIndeksPitanja);
                prikaziTacanOdgovor();
//                System.out.println("Trenutni index" + trenutniIndeksPitanja);
//                System.out.println("Trenutno pitanje" + trenutnoPitanje);
//
//                prikaziOdgovor("Tačan odgovor je: " + trenutnoPitanje.get("tacanOdgovor"));
//
////                trenutniIndeksPitanja++;
//                new CountDownTimer(1000, 1000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        trenutnoPitanje = svaPitanja.get(trenutniIndeksPitanja);
//                        prikaziNarednoPitanje();
////                        postaviPitanjeIOdgovore(trenutnoPitanje);
////                        azurirajBojuKrugaPitanja();
//                    }
//                }.start();
            } else {
            }
        });

        btnKoZnaZnaDalje = view.findViewById(R.id.koZnaZnaDalje);
        btnKoZnaZnaDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsocijacijeFragment asocijacijeFragment = new AsocijacijeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, asocijacijeFragment).commit();
            }
        });
        return view;
    }

    private void proveriOdgovor(TextView odgovor) {
        String txtOdgovor = odgovor.getText().toString();

        if (trenutniIndeksPitanja < svaPitanja.size()) {
            if (txtOdgovor.equals(trenutnoPitanje.get("tacanOdgovor"))) {
                odgovor.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
                prikazanOdgovor = true;
                poeni += 10;

                // Emitovanje soketa za tačan odgovor
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    IgreSlagalice.socket.emit("koZnaZnaAnswer", true, LocalDateTime.now().toString(),
                            korisnickoImeLeviIgrac);
                } else {
                    poeniLeviIgrac += 10;
                    textPoeniLeft.setText(Integer.toString(poeniLeviIgrac));
                }
            } else {
                odgovor.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.wrong_answer));
                prikaziTacanOdgovor();
                prikazanOdgovor = true;
                poeni -= 5;

                // Emitovanje soketa za netačan odgovor
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    IgreSlagalice.socket.emit("koZnaZnaAnswer", false, LocalDateTime.now().toString(),
                            korisnickoImeLeviIgrac);
                } else {

                    poeniLeviIgrac -= 5;
                    textPoeniLeft.setText(Integer.toString(poeniLeviIgrac));
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    odgovor.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
                }
            }, 1000);
        }
    }


    private void prikaziTacanOdgovor() {
        String tacanOdgovor = trenutnoPitanje.get("tacanOdgovor").toString();
        // Pronađite TextView za tačan odgovor na osnovu teksta
        TextView tacanOdgovorView = null;

        if (odgovor1TextView.getText().toString().equals(tacanOdgovor)) {
            tacanOdgovorView = odgovor1TextView;
        } else if (odgovor2TextView.getText().toString().equals(tacanOdgovor)) {
            tacanOdgovorView = odgovor2TextView;
        } else if (odgovor3TextView.getText().toString().equals(tacanOdgovor)) {
            tacanOdgovorView = odgovor3TextView;
        } else if (odgovor4TextView.getText().toString().equals(tacanOdgovor)) {
            tacanOdgovorView = odgovor4TextView;
        }

        if (tacanOdgovorView != null) {
            tacanOdgovorView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
        }

        TextView finalTacanOdgovorView = tacanOdgovorView;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finalTacanOdgovorView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
            }
        }, 1000);
    }

    private void pokreniTajmerPromenePitanja() {
        tajmerPrikaz = new CountDownTimer(trajanjePrikaza, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tajmerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                if (millisUntilFinished <= 10000) {
                    tajmerTextView.setTextColor(Color.RED);
                }
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    IgreSlagalice.socket.on("scoreUpdate", args -> {
                        Log.d("PRIMANJE ODGOVORA", "Primljen događaj scoreUpdate");
                        if (args.length > 0 && args[0] instanceof JSONObject) {
                            JSONObject data = (JSONObject) args[0];
                            Log.println(Log.INFO, "args-score", Arrays.toString(args));
                            try {
                                for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                                    String playerName = it.next();
                                    if (playerName.equals(korisnickoImeLeviIgrac)) {
                                        poeniLeviIgrac = data.getInt(playerName);
                                                textPoeniLeft.setText(Integer.toString(poeniLeviIgrac));
                                    } else if (playerName.equals(korisnickoImeDesniIgrac)) {
                                        poeniDesniIgrac = data.getInt(playerName);
                                                textPoeniRight.setText(Integer.toString(poeniDesniIgrac));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    prenetiPoeni = poeni;
                    textPoeniLeft.setText(String.valueOf(poeni));
                    sharedData.setPoeniIgraca(prenetiPoeni);
                }
            }

            @Override
            public void onFinish() {
                if (!prikazanOdgovor) {
                    if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                        Log.d("SLANJE ODGOVORA", "Emitovanje događaja koZnaZnaAnswerCheck");
                        IgreSlagalice.socket.emit("koZnaZnaAnswerCheck");
                    }
                    prikaziTacanOdgovor();
                }
                prikaziNarednoPitanje();
            }
        };
        tajmerPrikaz.start();
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
                Bundle fragmentBundle = new Bundle();
                if (korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    if (((IgreSlagalice) getActivity()).getRunda() == 1) {
                        fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
                        fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
                        fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
                        fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
                        prikaziSpojniceFragment(fragmentBundle);
                        ((IgreSlagalice) getActivity()).setRunda(1);
                    }
                } else {
                    prikaziSpojniceFragment(new Bundle());
                }
            }
        };

        tajmerPrikaz.start();
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

        db.collection("koZnaZna")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        svaPitanja.add(document.getData());
                    }

                    if (!svaPitanja.isEmpty()) {
                        trenutnoPitanje = svaPitanja.get(0);
                        postaviPitanjeIOdgovore(trenutnoPitanje);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Greška prilikom dobavljanja dokumenata: ", e));
    }

    private void postaviPitanjeIOdgovore(Map<String, Object> pitanje) {
        pitanjeTextView.setText(pitanje.get("pitanje").toString());
        odgovor1TextView.setText(pitanje.get("odgovor1").toString());
        odgovor2TextView.setText(pitanje.get("odgovor2").toString());
        odgovor3TextView.setText(pitanje.get("odgovor3").toString());
        odgovor4TextView.setText(pitanje.get("odgovor4").toString());

        pokreniTajmerPromenePitanja();
    }

    private void azurirajBojuKrugaPitanja() {
        for (int i = 1; i <= 5; i++) {
            TextView krugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + i, "id", getActivity().getPackageName()));
            krugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        }

        if(trenutniIndeksPitanja == 0) {
            TextView trenutniKrugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + 1, "id", getActivity().getPackageName()));
            trenutniKrugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.zelena));
        }

        if(trenutniIndeksPitanja == 1) {
            TextView trenutniKrugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + 2, "id", getActivity().getPackageName()));
            trenutniKrugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.zelena));
        }

        if(trenutniIndeksPitanja == 2) {
            TextView trenutniKrugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + 3, "id", getActivity().getPackageName()));
            trenutniKrugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.zelena));
        }

        if(trenutniIndeksPitanja == 3) {
            TextView trenutniKrugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + 4, "id", getActivity().getPackageName()));
            trenutniKrugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.zelena));
        }

        if(trenutniIndeksPitanja == 4) {
            TextView trenutniKrugPitanja = view.findViewById(getResources().getIdentifier("krugPitanje" + 5, "id", getActivity().getPackageName()));
            trenutniKrugPitanja.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.zelena));
        }
    }

    private void prikaziNarednoPitanje() {
        trenutniIndeksPitanja++;
        if (trenutniIndeksPitanja < svaPitanja.size()) {
            trenutnoPitanje = svaPitanja.get(trenutniIndeksPitanja);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    postaviPitanjeIOdgovore(trenutnoPitanje);
                    azurirajBojuKrugaPitanja();
                    prikazanOdgovor = false;
                }
            }, 1000);
        } else {
            prenetiPoeni = poeni;
//            poeniLevogIgraca.setText(String.valueOf(prenetiPoeni));
            sharedData.setPoeniIgraca(prenetiPoeni);

            prikaziObavestenje("Osvojili ste " + poeni + " poena.\nSledeca igra pocinje za:\n");
        }
    }

    public void prikaziSpojniceFragment(Bundle bundle) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SpojniceFragment spojniceFragment = new SpojniceFragment();
        spojniceFragment.setArguments(bundle);
        transaction.replace(R.id.igreSlagaliceContainer, spojniceFragment);
        transaction.commit();
    }
}