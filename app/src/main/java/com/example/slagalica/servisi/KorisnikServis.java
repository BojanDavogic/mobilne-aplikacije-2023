package com.example.slagalica.servisi;

import android.util.Log;

import com.example.slagalica.model.Korisnik;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class KorisnikServis {
    private static Map<String, Korisnik> korisnici = new HashMap<>();
    private static String kolekcija = "registrovaniKorisnici";
    private static Korisnik trenutnoUlogovaniKorisnik;

    public static int getBrojKorisnika() {
        return korisnici.size();
    }

    static {
        KorisnikServis.svi(new KorisnikServis.KorisnikCallback() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                for (int i = 1; i <= result.size(); i++) {
                    Map<String, Object> userData = (Map<String, Object>) result.get(String.valueOf(i));
                    int id = Integer.parseInt(userData.get("id").toString());
                    String email = (String) userData.get("email");
                    String korisnickoIme = (String) userData.get("korisnickoIme");
                    String lozinka = (String) userData.get("lozinka");
                    int tokeni = Integer.parseInt(userData.get("tokeni").toString());
                    String poslednjePrimljeno = (String) userData.get("poslednjePrimljeno");

                    Korisnik korisnik = new Korisnik(id, email, korisnickoIme, lozinka, tokeni, poslednjePrimljeno);
                    korisnici.put(Integer.toString(korisnik.getId()), korisnik);
                }
            }
        });
    }

    private static void osveziKorisnike() {
        KorisnikServis.svi(new KorisnikServis.KorisnikCallback() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                for (int i = 1; i <= result.size(); i++) {
                    Map<String, Object> userData = (Map<String, Object>) result.get(String.valueOf(i));
                    int id = Integer.parseInt(userData.get("id").toString());
                    String email = (String) userData.get("email");
                    String korisnickoIme = (String) userData.get("korisnickoIme");
                    String lozinka = (String) userData.get("lozinka");
                    int tokeni = Integer.parseInt(userData.get("tokeni").toString());
                    String poslednjePrimljeno = (String) userData.get("poslednjePrimljeno");

                    Korisnik korisnik = new Korisnik(id, email, korisnickoIme, lozinka, tokeni, poslednjePrimljeno);
                    korisnici.put(Integer.toString(korisnik.getId()), korisnik);
                }
            }
        });
    }

    public static Map<String, Object> get(int id, final KorisnikServis.KorisnikCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(kolekcija).document("korisnik" + id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> retVal = document.getData();
                            callback.onSuccess(retVal);
                        } else {
                            Map<String, Object> retVal = new HashMap<>();
                            retVal.put("error", null);
                            Log.e("FIRESTORE", "Dokument ne postoji");
                            callback.onSuccess(retVal);
                        }
                    } else {
                        Map<String, Object> retVal = new HashMap<>();
                        retVal.put("error", null);
                        Log.e("FIRESTORE", "Task nije uspeo");
                        callback.onSuccess(retVal);
                    }
                });
        return null;
    }

    public static Map<String, Object> svi(final KorisnikServis.KorisnikCallback callback) {
        Map<String, Object> korisniciMapa = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(kolekcija)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists())
                                korisniciMapa.put(String.valueOf(document.getData().get("id")), document.getData());
                        }
                        callback.onSuccess(korisniciMapa);
                    } else {
                        Log.w("FIRESTORE", "Greška pri dohvatanju dokumenata", task.getException());
                    }
                });
        return korisniciMapa;
    }

    public static void dodajKorisnika(int id, String email, String korisnickoIme, String lozinka) {
        Map<String, Object> korisnik = new HashMap<>();
        korisnik.put("id", id);
        korisnik.put("email", email);
        korisnik.put("korisnickoIme", korisnickoIme);
        korisnik.put("lozinka", lozinka);
        korisnik.put("tokeni", 5);
        korisnik.put("poslednjePrimljeno", null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(kolekcija).document("korisnik" + id)
                .set(korisnik)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIRESTORE", "Dokument uspešno upisan");
                    osveziKorisnike();
                })
                .addOnFailureListener(e -> Log.w("FIRESTORE", "Greška pri dodavanju dokumenta", e));
    }

    public static void azurirajTokeneKorisnika(int id, int tokeni, boolean azurirajDatum) {
        Map<String, Object> podaci = new HashMap<>();
        podaci.put("tokeni", tokeni);

        if (azurirajDatum)
            podaci.put("poslednjePrimljeno", LocalDate.now().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(kolekcija).document("korisnik" + id)
                .set(podaci, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIRESTORE", "Dokument uspešno upisan");
                    osveziKorisnike();
                })
                .addOnFailureListener(e -> Log.w("FIRESTORE", "Greška pri ažuriranju tokena", e));
    }

    public Korisnik prijaviKorisnika(String identifikatorPrijave, String lozinka) {
        Korisnik korisnik = null;
        boolean prijavaEmailom = identifikatorPrijave.contains("@");

        for (int i = 1; i <= korisnici.size(); i++) {
            Korisnik temp = (Korisnik) korisnici.get(String.valueOf(i));
            if ((prijavaEmailom && temp.getEmail().equals(identifikatorPrijave) && temp.getLozinka().equals(lozinka))
                    || (temp.getKorisnickoIme().equals(identifikatorPrijave) && temp.getLozinka().equals(lozinka))) {
                korisnik = temp;
                trenutnoUlogovaniKorisnik = korisnik;
            }
        }
        return korisnik;
    }

    public Korisnik getTrenutnoUlogovaniKorisnik() {
        return trenutnoUlogovaniKorisnik;
    }

    // Ako je povezan na internet, uvijek će proći, stoga uvijek vraća true
    public boolean registrujKorisnika(Korisnik korisnik) {
        dodajKorisnika(korisnik.getId(), korisnik.getEmail(), korisnik.getKorisnickoIme(), korisnik.getLozinka());
        return true;
    }

    // Callback interfejs zbog asinhronosti
    public interface KorisnikCallback {
        void onSuccess(Map<String, Object> result);
    }
}
