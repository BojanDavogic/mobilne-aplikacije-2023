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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slagalica.R;
import com.example.slagalica.model.Gost;
import com.example.slagalica.pomocniAlati.SharedData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.sourceforge.jeval.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MojBrojFragment extends Fragment {

    private TextView poeniLeviIgrac;

    SharedData sharedData = SharedData.getInstance();
    int poeniIgraca = sharedData.getPoeniIgraca();

//    public int poeniIgraca = 0;

    private List<Integer> trocifreniRotirajuciBrojevi = new ArrayList<>();
    private List<Integer> jednocifreniRotirajuciBrojevi = new ArrayList<>();
    private List<Integer> dvocifreniRotirajuciBrojevi = Arrays.asList(10, 15, 20);
    private List<Integer> dvocifreni2RotirajuciBrojevi = Arrays.asList(25, 50, 75, 100);
    private int trenutniIndeks = 0;
    private Random random = new Random();

    private Handler handlerTrocifreni;
    private Handler handlerJednocifreni;
    private Handler handlerDvocifreni;
    private Runnable rotirajRunnableTrocifreni;
    private Runnable rotirajRunnableJednocifreni;
    private Runnable rotirajRunnableDvocifreni;


    private FirebaseFirestore db;

    private CountDownTimer tajmerIgra;
    private CountDownTimer tajmerPrikaz;
    private long trajanjeIgre = 60000;
    private long trajanjePrikaza = 5000;
    private TextView tajmerTextView;
    private boolean igraOdigrana = false;

    private TextView trazeniBrojTextView;
    private TextView ponudjeniBroj1TextView;
    private TextView ponudjeniBroj2TextView;
    private TextView ponudjeniBroj3TextView;
    private TextView ponudjeniBroj4TextView;
    private TextView ponudjeniBroj5TextView;
    private TextView ponudjeniBroj6TextView;

    private TextView plus;
    private TextView minus;
    private TextView puta;
    private TextView podeljeno;
    private TextView otvorenaZagrada;
    private TextView zatvorenaZagrada;


    private TextView rezultatTextView;

    private Map<String, Object> brojevi;
    private int currentIndex = 0;

    private List<TextView> brojeviTextViews = new ArrayList<>();

    ImageButton btnObrisi;
    Button btnStop;
    Button btnPotvrdi;

    private int brojacBtnStop = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
        poeniLeviIgrac = view.findViewById(R.id.poeniLeviIgrac);
        poeniLeviIgrac.setText(String.valueOf(poeniIgraca));

        for (int i = 1; i <= 20; i++) {
            int randomBroj = random.nextInt(900) + 100;
            trocifreniRotirajuciBrojevi.add(randomBroj);
        }

        for(int i = 1; i<=9; i++){
            jednocifreniRotirajuciBrojevi.add(i);
        }

        trazeniBrojTextView = view.findViewById(R.id.trazeniBroj);
        ponudjeniBroj1TextView = view.findViewById(R.id.jednocifren1);
        ponudjeniBroj2TextView = view.findViewById(R.id.jednocifren2);
        ponudjeniBroj3TextView = view.findViewById(R.id.jednocifren3);
        ponudjeniBroj4TextView = view.findViewById(R.id.jednocifren4);
        ponudjeniBroj5TextView = view.findViewById(R.id.dvocifren);
        ponudjeniBroj6TextView = view.findViewById(R.id.dvocifren2);

        plus = view.findViewById(R.id.plus);
        minus = view.findViewById(R.id.minus);
        puta = view.findViewById(R.id.puta);
        podeljeno = view.findViewById(R.id.podeljeno);
        otvorenaZagrada = view.findViewById(R.id.otvorenaZagrada);
        zatvorenaZagrada = view.findViewById(R.id.zatvorenaZagrada);

        rezultatTextView = view.findViewById(R.id.rezultat);

        btnObrisi = view.findViewById(R.id.btnObrisi);

        btnStop = view.findViewById(R.id.btnStop);
        btnPotvrdi = view.findViewById(R.id.btnPotvrdi);

        db = FirebaseFirestore.getInstance();
        ucitajBrojeveIzFirestore();

        handlerTrocifreni = new Handler();
        handlerJednocifreni = new Handler();
        handlerDvocifreni = new Handler();
        rotirajRunnableTrocifreni = new Runnable() {
            @Override
            public void run() {
                rotirajBrojeve(trocifreniRotirajuciBrojevi, trazeniBrojTextView);
                trenutniIndeks = (trenutniIndeks + 1) % trocifreniRotirajuciBrojevi.size();
                handlerTrocifreni.postDelayed(this, 70);
            }
        };

        handlerTrocifreni.postDelayed(rotirajRunnableTrocifreni, 0);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brojacBtnStop++;
                handlerTrocifreni.removeCallbacks(rotirajRunnableTrocifreni);
                prikaziBrojeve();

                if(brojacBtnStop == 1){
                    rotirajRunnableJednocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(jednocifreniRotirajuciBrojevi, ponudjeniBroj1TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % jednocifreniRotirajuciBrojevi.size();
                            handlerJednocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerJednocifreni.postDelayed(rotirajRunnableJednocifreni, 0);
                }
                if(brojacBtnStop == 2){
                    handlerJednocifreni.removeCallbacks(rotirajRunnableJednocifreni);
                    rotirajRunnableJednocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(jednocifreniRotirajuciBrojevi, ponudjeniBroj2TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % jednocifreniRotirajuciBrojevi.size();
                            handlerJednocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerJednocifreni.postDelayed(rotirajRunnableJednocifreni, 0);
                }
                if(brojacBtnStop == 3){
                    handlerJednocifreni.removeCallbacks(rotirajRunnableJednocifreni);
                    rotirajRunnableJednocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(jednocifreniRotirajuciBrojevi, ponudjeniBroj3TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % jednocifreniRotirajuciBrojevi.size();
                            handlerJednocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerJednocifreni.postDelayed(rotirajRunnableJednocifreni, 0);
                }
                if(brojacBtnStop == 4){
                    handlerJednocifreni.removeCallbacks(rotirajRunnableJednocifreni);
                    rotirajRunnableJednocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(jednocifreniRotirajuciBrojevi, ponudjeniBroj4TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % jednocifreniRotirajuciBrojevi.size();
                            handlerJednocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerJednocifreni.postDelayed(rotirajRunnableJednocifreni, 0);
                }
                if(brojacBtnStop == 5){
                    handlerJednocifreni.removeCallbacks(rotirajRunnableJednocifreni);
                    rotirajRunnableDvocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(dvocifreniRotirajuciBrojevi, ponudjeniBroj5TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % dvocifreniRotirajuciBrojevi.size();
                            handlerDvocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerDvocifreni.postDelayed(rotirajRunnableDvocifreni, 0);
                }
                if(brojacBtnStop == 6){
                    handlerDvocifreni.removeCallbacks(rotirajRunnableDvocifreni);
                    rotirajRunnableDvocifreni = new Runnable() {
                        @Override
                        public void run() {
                            rotirajBrojeve(dvocifreni2RotirajuciBrojevi, ponudjeniBroj6TextView);
                            trenutniIndeks = (trenutniIndeks + 1) % dvocifreni2RotirajuciBrojevi.size();
                            handlerDvocifreni.postDelayed(this, 70);
                        }
                    };
                    handlerDvocifreni.postDelayed(rotirajRunnableDvocifreni, 0);
                }
                if(brojacBtnStop == 7){
                    handlerDvocifreni.removeCallbacks(rotirajRunnableDvocifreni);
                }

//                prikaziBrojeve();
            }
        });

        tajmerTextView = view.findViewById(R.id.tajmer);

        ponudjeniBroj1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj1TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj1TextView.setEnabled(false);
                ponudjeniBroj1TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj1TextView);
            }
        });

        ponudjeniBroj2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj2TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj2TextView.setEnabled(false);
                ponudjeniBroj2TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj2TextView);
            }
        });

        ponudjeniBroj3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj3TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj3TextView.setEnabled(false);
                ponudjeniBroj3TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj3TextView);
            }
        });

        ponudjeniBroj4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj4TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj4TextView.setEnabled(false);
                ponudjeniBroj4TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj4TextView);
            }
        });

        ponudjeniBroj5TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj5TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj5TextView.setEnabled(false);
                ponudjeniBroj5TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj5TextView);
            }
        });

        ponudjeniBroj6TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ponudjeniBroj6TextView.getText().toString();
                appendToResult(text);
                ponudjeniBroj6TextView.setEnabled(false);
                ponudjeniBroj6TextView.setTextColor(Color.GRAY);
                brojeviTextViews.add(ponudjeniBroj6TextView);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = plus.getText().toString();
                appendToResult(text);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = minus.getText().toString();
                appendToResult(text);
            }
        });

        puta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = puta.getText().toString();
                appendToResult(text);
            }
        });

        podeljeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = podeljeno.getText().toString();
                appendToResult(text);
            }
        });

        otvorenaZagrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = otvorenaZagrada.getText().toString();
                appendToResult(text);
            }
        });

        zatvorenaZagrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = zatvorenaZagrada.getText().toString();
                appendToResult(text);
            }
        });

        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentResult = rezultatTextView.getText().toString().trim();
                if (!currentResult.isEmpty()) {
                    if (lastInputWasNumber) {
                        // Ako je poslednji unos bio broj, pronađi poslednji broj u izrazu
                        String lastNumber = getLastNumber(currentResult);
                        if (lastNumber != null) {
                            usedNumbers.remove(lastNumber);
                        }
                    }
                    // Pronaći poslednji broj ili operator
                    String lastInput = getLastNumberOrOperator(currentResult);
                    if (lastInput != null) {
                        // Ako je poslednji unos bio broj, obriši ceo broj
                        if (isNumber(lastInput)) {
                            currentResult = currentResult.substring(0, currentResult.lastIndexOf(lastInput));
                        } else {
                            // Ako je poslednji unos bio operator, obriši samo operator
                            currentResult = currentResult.substring(0, currentResult.length() - 1);
                        }
                        rezultatTextView.setText(currentResult);
                        lastInputWasNumber = isLastInputNumber(currentResult);
                        lastInputWasOperation = isLastInputOperation(currentResult);

                        for (TextView textView: brojeviTextViews) {
                            if (textView.getText().equals(lastInput)) {
                                textView.setEnabled(true);
                                textView.setTextColor(Color.WHITE);
                                break;
                            }
                        }
                    }
                }
            }
        });

        btnPotvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = rezultatTextView.getText().toString();
                evaluirajIzraz(expression);
                tajmerIgra.cancel();
            }
        });

        return view;
    }

    private void rotirajBrojeve(List<Integer> brojevi, TextView textView) {
        int brojElemenata = brojevi.size();
        if (brojElemenata > 0) {
            trenutniIndeks = (trenutniIndeks + 1) % brojElemenata;
            int trenutniBroj = brojevi.get(trenutniIndeks);
            textView.setText(String.valueOf(trenutniBroj));
        }
    }

    private String getLastNumberOrOperator(String expression) {
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
        if (tokens.length > 0) {
            return tokens[tokens.length - 1];
        }
        return null;
    }

    private String getLastNumber(String expression) {
        String[] tokens = expression.split("[+\\-*/()]");
        if (tokens.length > 0) {
            return tokens[tokens.length - 1];
        }
        return null;
    }

    private boolean isLastInputNumber(String expression) {
        expression = expression.trim();
        return !expression.isEmpty() && Character.isDigit(expression.charAt(expression.length() - 1));
    }

    private boolean isLastInputOperation(String expression) {
        expression = expression.trim();
        return !expression.isEmpty() && "+-*/".contains(expression.substring(expression.length() - 1));
    }

    private List<String> usedNumbers = new ArrayList<>();

    private boolean lastInputWasNumber = false;
    private boolean lastInputWasOperation = false;

    private void appendToResult(String value) {
        String currentResult = rezultatTextView.getText().toString();

        if (value.equals("otvorenaZagrada")) {
            if (currentResult.isEmpty() || isOperator(currentResult)) {
                currentResult += "(";
                rezultatTextView.setText(currentResult);
                lastInputWasNumber = false;
                lastInputWasOperation = false;
            }
        } else if (isNumber(value)) {

            if (!usedNumbers.contains(value) && (!lastInputWasNumber || lastInputWasOperation)) {
                currentResult += value;
                rezultatTextView.setText(currentResult);
                usedNumbers.add(value);
                lastInputWasNumber = true;
                lastInputWasOperation = false;
            }
        } else if (isOperator(value)) {
            if ((currentResult.isEmpty() && value.equals("-")) || (!isOperator(currentResult) && (lastInputWasNumber || lastInputWasOperation || currentResult.endsWith(")") || value.equals("-")))) {
                currentResult += value;
                rezultatTextView.setText(currentResult);
                lastInputWasNumber = false;
                lastInputWasOperation = true;
            }
        } else {
            currentResult += value;
            rezultatTextView.setText(currentResult);
            lastInputWasNumber = false;
            lastInputWasOperation = false;
        }
    }


    private boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String text) {
        return text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/");
    }

    private void prikaziRezultat(int result, int poeniIgraca) {
        Toast.makeText(getActivity(), "Rezultat: " + result, Toast.LENGTH_SHORT).show();
    }

    private void evaluirajIzraz(String expression) {
        try {
            Evaluator evaluator = new Evaluator();
            int result = (int) evaluator.getNumberResult(expression);
            String trazeniBroj = trazeniBrojTextView.getText().toString();

            if (result == Integer.parseInt(trazeniBroj)) {
                poeniIgraca += 20;
                sharedData.setPoeniIgraca(poeniIgraca);
                poeniLeviIgrac.setText(String.valueOf(poeniIgraca));
                prikaziObavestenje("Rezultat: " + result + " \n" +
                        "Broj osvojenih bodova: " + poeniIgraca + "\nSledeca igra pocinje za:");
//                prikaziRezultat(result, poeniIgraca);
            } else if (result != 0 && result != Integer.parseInt(trazeniBroj)) {
                poeniIgraca += 5;
                sharedData.setPoeniIgraca(poeniIgraca);
                poeniLeviIgrac.setText(String.valueOf(poeniIgraca));
                prikaziObavestenje("Rezultat: " + result + " \n" +
                        "Broj osvojenih bodova: " + poeniIgraca + "\nSledeca igra pocinje za:");
//                prikaziRezultat(result, poeniIgraca);
            } else {
                poeniIgraca += 0;
                sharedData.setPoeniIgraca(poeniIgraca);
                poeniLeviIgrac.setText(String.valueOf(poeniIgraca));
                prikaziObavestenje("Rezultat: " + result + " \n" +
                        "Broj osvojenih bodova: " + poeniIgraca + "\nSledeca igra pocinje za:");
//                prikaziRezultat(0, 0);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Greška pri evaluaciji izraza", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
                prikaziObavestenje("Vreme je isteklo\nOsvojili ste 0 bodova\nSledeca igra pocinje za:");
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

    private void ucitajBrojeveIzFirestore() {
        db.collection("mojBroj")
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

                        db.collection("mojBroj").document(randomDokument)
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            brojevi = document2.getData();
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


    private void prikaziBrojeve() {
        if (brojevi != null) {
            if (currentIndex < 7) {
                String kljuc;
                if (currentIndex == 0) {
                    kljuc = "trazeniBroj";
                    currentIndex++;
                } else if (currentIndex < 5) {
                    kljuc = "jednocifren" + currentIndex;
                    currentIndex++;
                } else if (currentIndex == 5) {
                    kljuc = "dvocifren";
                    currentIndex++;
                } else {
                    kljuc = "dvocifren2";
                    currentIndex++;
                }

                if (brojevi.containsKey(kljuc)) {
                    Object broj = brojevi.get(kljuc);
                    TextView textView = null;
                    switch (currentIndex) {
                        case 1:
                            textView = trazeniBrojTextView;
                            break;
                        case 2:
                            textView = ponudjeniBroj1TextView;
                            break;
                        case 3:
                            textView = ponudjeniBroj2TextView;
                            break;
                        case 4:
                            textView = ponudjeniBroj3TextView;
                            break;
                        case 5:
                            textView = ponudjeniBroj4TextView;
                            break;
                        case 6:
                            textView = ponudjeniBroj5TextView;
                            break;
                        case 7:
                            textView = ponudjeniBroj6TextView;
                            break;
                    }
                    if (textView != null) {
                        textView.setText(String.valueOf(broj));
                    }
                }

                if (currentIndex == 7) {
                    pokreniTajmerIgre();
                    btnStop.setEnabled(false);
                    btnStop.setBackgroundColor(Color.GRAY);
                }
            }
        }
    }

    public void prikaziKorakPoKorakFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.igreSlagaliceContainer, new KoZnaZnaFragment());
        transaction.commit();
    }
}