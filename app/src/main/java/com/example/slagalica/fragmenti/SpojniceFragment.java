package com.example.slagalica.fragmenti;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slagalica.R;
import com.example.slagalica.aktivnosti.IgreSlagalice;
import com.example.slagalica.pomocniAlati.DisableTouchFragment;
import com.example.slagalica.pomocniAlati.SharedData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.socket.emitter.Emitter;

public class SpojniceFragment extends Fragment {

    TextView textUsernameRight;
    TextView textPoeniLeft;
    TextView textPoeniRight;

    TextView textUsernameLeft;

    String korisnickoImeLeviIgrac;
    String korisnickoImeDesniIgrac;
    int poeniLeviIgrac;
    int poeniDesniIgrac;
    int pokusaj = 1;
    DisableTouchFragment disableTouchFragment;

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
    private String trenutniIgrac = "levi";
    private int brojacParova = 0;
    private int brojac = 0;
    private int brojacNetacnih = 0;
    private ArrayList<AppCompatButton> pogresnaPolja = new ArrayList<>();

    private AppCompatButton poslednjeKliknutoIzKolone1;
    private AppCompatButton poslednjeKliknutoIzKolone2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spojnice, container, false);
        disableTouchFragment = new DisableTouchFragment(requireContext());

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

        ucitajPoljaIzFirestore();
        pokreniTajmerIgre(trajanjeIgre);

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
        return view;
    }

    private void pokreniTajmerIgre(long trajanjeIgre) {
        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
            blockInput();
        }
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
                        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){}
                        else {
                            prenetiPoeni += poeni;
                            textPoeniLeft.setText(String.valueOf(prenetiPoeni));
                            sharedData.setPoeniIgraca(prenetiPoeni);
                            prikaziObavestenje("Vreme je isteklo\nOsvojili ste " + poeni + " bodova\nSledeca igra pocinje za:");
                        }
                    }
                }, 2000);
            }
        };

        tajmerIgra.start();
    }

    private void pokreniTajmerIgre2(long trajanjeIgre) {
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
            }
            @Override
            public void onFinish() {
//                blockInput();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prenetiPoeni += poeni;
                        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
                            prikaziObavestenje("Vreme je isteklo\nSledeca igra pocinje za:");
                        }
                        else {
                            textPoeniLeft.setText(String.valueOf(prenetiPoeni));
                            sharedData.setPoeniIgraca(prenetiPoeni);
                            prikaziObavestenje("Vreme je isteklo\nOsvojili ste " + poeni + " bodova\nSledeca igra pocinje za:");
                        }
                    }
                }, 2000);
            }
        };

        tajmerIgra.start();
    }

    private void prikaziObavestenje(String poruka) {
        final int[] inverted = {0};
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
                Bundle fragmentBundle = new Bundle();
                if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
                    blockInput();
                    if (((IgreSlagalice)getActivity()).getRunda() == 1) {
                        ((IgreSlagalice)getActivity()).setRunda(2);
                        fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
                        fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
                        fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
                        fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
                        prikaziSpojniceFragment(fragmentBundle);

                        if ((poeniLeviIgrac > poeniDesniIgrac || (poeniLeviIgrac == poeniDesniIgrac &&
                                korisnickoImeLeviIgrac.length() > korisnickoImeDesniIgrac.length())) && inverted[0] == 0) {
                            inverted[0]++;
                            IgreSlagalice.socket.emit("invert");
                        }
                        fragmentBundle.putInt("runda", ((IgreSlagalice)getActivity()).getRunda());
                    } else {
                        fragmentBundle.putString("korisnickoImeLeviIgrac", korisnickoImeLeviIgrac);
                        fragmentBundle.putString("korisnickoImeDesniIgrac", korisnickoImeDesniIgrac);
                        fragmentBundle.putInt("poeniLeviIgrac", poeniLeviIgrac);
                        fragmentBundle.putInt("poeniDesniIgrac", poeniDesniIgrac);
                        prikaziAsocijacijeFragment(fragmentBundle);
                        ((IgreSlagalice)getActivity()).setRunda(1);
                        inverted[0]--;
                    }
                } else {
                    prikaziAsocijacijeFragment(new Bundle());
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
        db.collection("spojnice")
                .document("kombinacija" + ((IgreSlagalice)getActivity()).getRunda())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            polja = document.getData();
                            prikaziPolja(); // Prikazujemo sva polja
                        } else {
                            Log.d(TAG, "Dokument ne postoji");
                        }
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
            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
                IgreSlagalice.socket.emit("spojniceParovi", spojeno1, spojeno2, true);
            } else {
                poeni += 2;
                prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
                drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
                prvoDugme.setEnabled(false);
                drugoDugme.setEnabled(false);
            }
        } else {
            if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
                IgreSlagalice.socket.emit("spojniceParovi", spojeno1, spojeno2, false);
            } else {
                prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.wrong_answer));
                drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
                prvoDugme.setEnabled(false);
            }
        }

        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")){
            brojacParova++;
        }
        else {
            brojac += 1;

            if (brojac == 5) {
                prenetiPoeni += poeni;
                textPoeniLeft.setText(String.valueOf(prenetiPoeni));
                sharedData.setPoeniIgraca(prenetiPoeni);

                prikaziObavestenje("Osvojili ste " + poeni + " poena");
            }
        }
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(korisnickoImeLeviIgrac != null && !korisnickoImeLeviIgrac.equals("")) {
            IgreSlagalice.socket.on("spojniceAzuriraj", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String prvoDugmeStr = args[0].toString();
                                String drugoDugmeStr = args[1].toString();
                                boolean tacnost = (Boolean) args[2];

                                AppCompatButton prvoDugme = getButtonForDugme(prvoDugmeStr);
                                AppCompatButton drugoDugme = getButtonForDugme(drugoDugmeStr);

                                if(tacnost){
                                    if(trenutniIgrac.equals(korisnickoImeLeviIgrac)){
                                        poeniLeviIgrac += 2;
                                        textPoeniLeft.setText(String.valueOf(poeniLeviIgrac));
                                    } else {
                                        poeniDesniIgrac += 2;
                                        textPoeniRight.setText(String.valueOf(poeniDesniIgrac));
                                    }
                                    prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
                                    drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.correct_answer));
                                    prvoDugme.setEnabled(false);
                                    drugoDugme.setEnabled(false);
                                } else {
                                    brojacNetacnih++;
                                    pogresnaPolja.add(prvoDugme);
                                    prvoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
                                    drugoDugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.customborder));
                                    prvoDugme.setEnabled(false);
                                }
                                if(brojacParova == 5 && brojacNetacnih != 0 && trenutniIgrac.equals(korisnickoImeLeviIgrac)){
//                                    for(AppCompatButton dugme : pogresnaPolja) {
//                                        dugme.setEnabled(true);
//                                        dugme.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.wrong_answer));
//                                    }
                                    blockInput();
                                    IgreSlagalice.socket.emit("zavrsenPotez", tacnost);
//                                    trenutniIgrac = "desni";
                                }
                            }
                        });


                    }
                }
            });

            IgreSlagalice.socket.on("trenutnoStanjeIgre", args -> {
                boolean tacnost = (boolean) args[0];
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        for(AppCompatButton dugme : pogresnaPolja) {
                            dugme.setEnabled(true);
                        }
                        tajmerIgra.cancel();
                        pokreniTajmerIgre2(10000);
                    });
                }
            });

        }
    }

    private AppCompatButton getButtonForDugme(String dugmeText) {
        if(dugmeText.equals(red1kolona1.getText())){
            return red1kolona1;
        }
        if(dugmeText.equals(red1kolona2.getText())){
            return red1kolona2;
        }
        if(dugmeText.equals(red2kolona1.getText())){
            return red2kolona1;
        }
        if(dugmeText.equals(red2kolona2.getText())){
            return red2kolona2;
        }
        if(dugmeText.equals(red3kolona1.getText())){
            return red3kolona1;
        }
        if(dugmeText.equals(red3kolona2.getText())){
            return red3kolona2;
        }
        if(dugmeText.equals(red4kolona1.getText())){
            return red4kolona1;
        }
        if(dugmeText.equals(red4kolona2.getText())){
            return red4kolona2;
        }
        if(dugmeText.equals(red5kolona1.getText())){
            return red5kolona1;
        }
        if(dugmeText.equals(red5kolona2.getText())){
            return red5kolona2;
        }

        return null;
    }


    private void blockInput() {
        IgreSlagalice.socket.emit("getTurn");
        IgreSlagalice.socket.on("turn", args -> {
            String username = args[0].toString();
            if (getActivity() != null) {
                setTrenutniIgrac(username);
                getActivity().runOnUiThread(() -> {
                    if (!username.equals(korisnickoImeLeviIgrac)) {
                        disableTouchFragment.disableTouch();
                    } else {
                        disableTouchFragment.enableTouch();
                    }

                });
            }
        });
    }

    private void setTrenutniIgrac(String username){
        trenutniIgrac = username;
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

    public void prikaziSpojniceFragment(Bundle bundle) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SpojniceFragment spojniceFragment = new SpojniceFragment();
        spojniceFragment.setArguments(bundle);
        transaction.replace(R.id.igreSlagaliceContainer, spojniceFragment);
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

}