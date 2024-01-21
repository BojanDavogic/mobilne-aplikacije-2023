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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slagalica.MainActivity;
import com.example.slagalica.R;
import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.pomocniAlati.DisableTouchFragment;
import com.example.slagalica.pomocniAlati.SharedData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AsocijacijeFragment extends Fragment {
    DisableTouchFragment disableTouchFragment;

    TextView poeniLevogIgraca;
    TextView textUsernameRight;
    TextView textPoeniLeft;
    TextView textPoeniRight;

    TextView textUsernameLeft;

    SharedData sharedData = SharedData.getInstance();
    public static Socket socket;
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

    private int trenutniIgrac = 1;
    String korisnickoImeLeviIgrac;
    String korisnickoImeDesniIgrac;
    int poeniLeviIgrac;
    int poeniDesniIgrac;
    int pokusaj = 1;
//    private int runda = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asocijacije, container, false);
        disableTouchFragment = new DisableTouchFragment(requireContext());

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null || (korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals(""))) {
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
                boolean konacnoTacno = false;
                boolean tacnaKolona = false;
                if (!unetoKonacnoResenje.isEmpty()) {
                    konacnoTacno = proveriKonacnoResenje(unetoKonacnoResenje);
                } if (!unetoResenjeKoloneA.isEmpty()) {
                    tacnaKolona = proveriResenje("a", unetoResenjeKoloneA, resenjeKoloneA);
                } if (!unetoResenjeKoloneB.isEmpty()) {
                    tacnaKolona = proveriResenje("b", unetoResenjeKoloneB, resenjeKoloneB);
                } if (!unetoResenjeKoloneC.isEmpty()) {
                    tacnaKolona = proveriResenje("c", unetoResenjeKoloneC, resenjeKoloneC);
                } if (!unetoResenjeKoloneD.isEmpty()) {
                    tacnaKolona = proveriResenje("d", unetoResenjeKoloneD, resenjeKoloneD);
                }
                int poeni = 0;
                if (konacnoTacno) {
                    prenetiPoeni += 7;
                    poeni += 7;
                    poeniLeviIgrac += 7;

                    if (poljeA1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneA.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeB1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneB.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeC1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneC.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeD1.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD2.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD3.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD4.isEnabled()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneD.isEnabled()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }
                } else if (tacnaKolona) {
                    if (poljeA1.isEnabled() && !unetoResenjeKoloneA.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA2.isEnabled() && !unetoResenjeKoloneA.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA3.isEnabled() && !unetoResenjeKoloneA.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeA4.isEnabled() && !unetoResenjeKoloneA.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneA.isEnabled() && !unetoResenjeKoloneA.isEmpty()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeB1.isEnabled() && !unetoResenjeKoloneB.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB2.isEnabled() && !unetoResenjeKoloneB.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB3.isEnabled() && !unetoResenjeKoloneB.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeB4.isEnabled() && !unetoResenjeKoloneB.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneB.isEnabled()  && !unetoResenjeKoloneB.isEmpty()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeC1.isEnabled() && !unetoResenjeKoloneC.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC2.isEnabled() && !unetoResenjeKoloneC.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC3.isEnabled() && !unetoResenjeKoloneC.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeC4.isEnabled() && !unetoResenjeKoloneC.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneC.isEnabled() && !unetoResenjeKoloneC.isEmpty()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }

                    if (poljeD1.isEnabled() && !unetoResenjeKoloneD.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD2.isEnabled() && !unetoResenjeKoloneD.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD3.isEnabled() && !unetoResenjeKoloneD.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (poljeD4.isEnabled() && !unetoResenjeKoloneD.isEmpty()) {
                        prenetiPoeni += 1;
                        poeni += 1;
                        poeniLeviIgrac += 1;
                    }

                    if (resenjeKoloneD.isEnabled()  && !unetoResenjeKoloneD.isEmpty()) {
                        prenetiPoeni += 2;
                        poeni += 2;
                        poeniLeviIgrac += 2;
                    }
                }

                int finalpoeni = poeni;

                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    IgreSlagalice.socket.emit("asocijacijeScoreUpdate", korisnickoImeLeviIgrac, finalpoeni, korisnickoImeDesniIgrac, poeniDesniIgrac);
                } else {
                    textPoeniLeft.setText(String.valueOf(prenetiPoeni));
                    sharedData.setPoeniIgraca(prenetiPoeni);
//                    tajmerIgra.cancel();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            prikaziObavestenje("Igra je zavrsena\nOsvojili ste " + finalpoeni + " poena\nSledeca igra pocinje za: ");
//                        }
//                    }, 3000);
                }
            }
        });


        return view;
    }

    private void pokreniTajmerIgre() {
        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
//            blockInput();
        }
        tajmerIgra = new CountDownTimer(trajanjeIgre, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tajmerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                if (millisUntilFinished <= 10000) {
                    tajmerTextView.setTextColor(Color.RED);
                }
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    IgreSlagalice.socket.on("scoreUpdate", args -> {
                        if (args.length > 0 && args[0] instanceof JSONObject) {
                            JSONObject data = (JSONObject) args[0];
                            Log.println(Log.INFO, "args-score", Arrays.toString(args));
                            try {
                                for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                                    String playerName = it.next();
                                    if (playerName.equals(korisnickoImeLeviIgrac)) {
                                        poeniLeviIgrac = data.getInt(playerName);
                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(() ->
                                                    textPoeniLeft.setText(Integer.toString(poeniLeviIgrac)));
                                        }
                                    } else if (playerName.equals(korisnickoImeDesniIgrac)) {
                                        poeniDesniIgrac = data.getInt(playerName);
                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(() ->
                                                    textPoeniRight.setText(Integer.toString(poeniDesniIgrac)));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFinish() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prikaziObavestenje("Vreme je isteklo\nSledeca igra pocinje za:");
                        prikaziSvaPolja();
                        prikaziSvaResenjaKolona();
                        prikaziKonacnoResenje();


                    }
                }, 2000);
            }
        };

        tajmerIgra.start();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {

            IgreSlagalice.socket.on("asocijacijeOpen", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String openedField = args[0].toString();

//                        if (polje.equals(openedField)) {
                                String text = (String) polja.get(openedField);
                                TextView button = getButtonForPolje(openedField);
                                if (button != null && text != null) {
                                    button.setText(text);
                                    button.setVisibility(View.VISIBLE);
                                }
//                        }
                            }
                        });
                    }
                }
            });

            IgreSlagalice.socket.on("asocijacijeSolved", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String polje = args[0].toString();
                                String resenjeKolone = args[1].toString();

                                EditText resenje = getEditTextForPolje(polje);

                                prikaziKolonu(polje);
                                resenje.setText(resenjeKolone);
                                resenje.setEnabled(false);
//                            resenje.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//                            resenje.setTextColor(Color.BLACK);
                            }
                        });
                    }
                }
            });

            IgreSlagalice.socket.on("asocijacijeKonacno", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String field = args[0].toString();
                                prikaziKonacnoResenje();
                                prikaziSvaPolja();
                                prikaziSvaResenjaKolona();
                                tajmerIgra.cancel();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        prikaziObavestenje("Igra je zavrsena\n " + "Sledeca igra pocinje za: ");
                                    }
                                }, 3000);
                            }
                        });
                    }
                }
            });
        }
    }


    private void prikaziObavestenje(String poruka) {
        final int[] inverted = {0};
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
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    if (((IgreSlagalice)getActivity()).getRunda() == 1) {
                        ((IgreSlagalice)getActivity()).setRunda(2);
                        fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
                        fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
                        fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
                        fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
                        prikaziAsocijacijeFragment(fragmentBundle);

                        if ((poeniLeviIgrac > poeniDesniIgrac || (poeniLeviIgrac == poeniDesniIgrac &&
                                korisnickoImeLeviIgrac.length() > korisnickoImeDesniIgrac.length())) && inverted[0] == 0) {
                            inverted[0]++;
                            IgreSlagalice.socket.emit("invert");
                        }
                        fragmentBundle.putInt("runda", ((IgreSlagalice)getActivity()).getRunda());
//                        blockInput();
                    } else {
                        fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
                        fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
                        fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
                        fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
//                        prikaziSkockoFragment(fragmentBundle);
                        prikaziBodoveObavestenje();
                        ((IgreSlagalice)getActivity()).setRunda(1);
                    }
                } else {
                    prikaziSkockoFragment(new Bundle());
                }
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

    private int currentIndex = 0;

    private void ucitajPoljaIzFirestore() {
        db.collection("asocijacije")
                .document("kombinacija" + ((IgreSlagalice)getActivity()).getRunda())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Dokument postoji, možete pristupiti podacima
                            polja = document.getData();
                            resenjaKolona.put("a", polja.get("resenjeA").toString());
                            resenjaKolona.put("b", polja.get("resenjeB").toString());
                            resenjaKolona.put("c", polja.get("resenjeC").toString());
                            resenjaKolona.put("d", polja.get("resenjeD").toString());
                            konacnoResenjee = polja.get("konacnoResenje").toString();
                        } else {
                            Log.d(TAG, "Dokument ne postoji");
                        }
                    } else {
                        Log.e(TAG, "Greška prilikom dobavljanja dokumenata: ", task.getException());
                    }
                });
    }

    private void prikaziPolje(String polje) {
        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
            if (pokusaj > 0) {
                IgreSlagalice.socket.emit("asocijacijeOpened", polje);
            }
            pokusaj = 0;
        } else {
            String text = (String) polja.get(polje);  // Dobijanje teksta za odabrano polje
            TextView button = getButtonForPolje(polje);  // Dobijanje odgovarajućeg dugmeta za polje
            if (button != null && text != null) {
                button.setText(text);  // Postavljanje teksta na dugme
                button.setVisibility(View.VISIBLE);  // Prikazivanje dugmeta

            }

        }
    }

    private boolean proveriResenje(String polje, String text, EditText resenje) {
        String resenjeKolone = resenjaKolona.get(polje);
        String resenjeKoloneText = getResenjeKoloneText(polje);

        if (text.equalsIgnoreCase(resenjeKolone)) {
            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                IgreSlagalice.socket.emit("asocijacijeSolution", polje, resenjeKolone);
            } else {
                prikaziKolonu(polje);
                resenje.setText(resenjeKolone);
                resenje.setEnabled(false);
//                resenje.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
//                resenje.setTextColor(Color.BLACK);
            }
            return true;
        } else {
            pokusaj = 1;
            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                IgreSlagalice.socket.emit("getTurn");
            }
            prikaziPoruku("Niste pogodili resenje");
            resenje.setText("");
            resenje.clearFocus();

            return false;
        }
    }

    private EditText getEditTextForPolje(String polje) {
        switch (polje.charAt(0)) {
            case 'a':
                return getView().findViewById(R.id.resenjeKoloneA);
            case 'b':
                return getView().findViewById(R.id.resenjeKoloneB);
            case 'c':
                return getView().findViewById(R.id.resenjeKoloneC);
            case 'd':
                return getView().findViewById(R.id.resenjeKoloneD);
            default:
                return null;
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
                String text = (String) polja.get(polje);  // Dobijanje teksta za odabrano polje
                TextView button = getButtonForPolje(polje);  // Dobijanje odgovarajućeg dugmeta za polje
                if (button != null && text != null) {
                    button.setText(text);  // Postavljanje teksta na dugme
                    button.setVisibility(View.VISIBLE);  // Prikazivanje dugmeta

                }
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
            tajmerIgra.cancel();
            prikaziPoruku("Pogodili ste konacno resenje!");

            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                MainActivity.socket.emit("asocijacijeKonacnoResenje", konacnoResenjee);
            } else {
                resenjeKoloneA.setEnabled(false);
                resenjeKoloneB.setEnabled(false);
                resenjeKoloneC.setEnabled(false);
                resenjeKoloneD.setEnabled(false);
                prikaziKonacnoResenje();
                prikaziSvaPolja();
                prikaziSvaResenjaKolona();

                tajmerIgra.cancel();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prikaziObavestenje("Igra je zavrsena\n " + "Sledeca igra pocinje za: ");
                    }
                }, 3000);
            }


            return true;
        } else {
            pokusaj = 1;
            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                IgreSlagalice.socket.emit("getTurn");
            }
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
            String text = (String) polja.get(polje);  // Dobijanje teksta za odabrano polje
            TextView button = getButtonForPolje(polje);  // Dobijanje odgovarajućeg dugmeta za polje
            if (button != null && text != null) {
                button.setText(text);  // Postavljanje teksta na dugme
                button.setVisibility(View.VISIBLE);  // Prikazivanje dugmeta

            }
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

    private void blockInput() {
        IgreSlagalice.socket.emit("getTurn");
        IgreSlagalice.socket.on("turn", args -> {
            String username = args[0].toString();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (!username.equals(korisnickoImeLeviIgrac)) {
                        disableTouchFragment.disableTouch();
//                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    Toast.makeText(getActivity(), "Unos nije dozvoljen. Sacekaj tvoj red!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Odblokiranje unosa
                        disableTouchFragment.enableTouch();
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    Toast.makeText(getActivity(), "Unos dozvoljen. Tvoj red!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }


    public void prikaziSkockoFragment(Bundle bundle) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SkockoFragment skockoFragment = new SkockoFragment();
        skockoFragment.setArguments(bundle);
        transaction.replace(R.id.igreSlagaliceContainer, skockoFragment);
        transaction.commit();
    }

    public void prikaziAsocijacijeFragment(Bundle bundle) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AsocijacijeFragment asocijacijeFragment = new AsocijacijeFragment();
        asocijacijeFragment.setArguments(bundle);
        transaction.replace(R.id.igreSlagaliceContainer, asocijacijeFragment);
        transaction.commit();
    }

    private void prikaziBodoveObavestenje() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Osvojeni bodovi");
        builder.setMessage("Osvojili ste ukupno " + poeniLevogIgraca + " poena.");

        builder.setPositiveButton("Ok", (dialog, which) -> {
            idiNaPocetniEkran(new Bundle());
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void idiNaPocetniEkran(Bundle bundle) {
        PocetniEkranFragment pocetniEkranFragment = new PocetniEkranFragment();
        pocetniEkranFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.igreSlagaliceContainer, pocetniEkranFragment).commit();
    }
}