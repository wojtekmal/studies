package osrodek_narciarski.sportowcy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;

import java.util.HashMap;
import java.util.Map;

public abstract class Sportowiec {

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;

    private final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final int id;

    private final int poziomZaawansowania; // {0, 1, ..., 10}

    private final double wspolczynnikSpontanicznosci; // [0, 1]

    private final double wspolczynnikZnudzenia; // [0, 1]

    private final double wagaTrudnosci; // [0, 1]

    private final double wagaNawierzchni; // [0, 1]

    private final double wagaZnudzenia; // [0, 1]

    private final boolean sledzony;

    private final Wezel wezelStartowy;

    private final Moment momentStartu;

    private final MaszynaLosujaca maszynaLosujaca;

    private final Map<Trasa, Double> znudzenia = new HashMap<>();

    private final Map<Trasa, Integer> kiedyOdwiedzonoTrasy = new HashMap<>();

    private int liczbaPrzejechanychTras = 0;

    public Sportowiec(int id,
        int poziomZaawansowania,
        double wspolczynnikSpontanicznosci,
        double wspolczynnikZnudzenia,
        double wagaZnudzenia,
        double wagaTrudnosci,
        double wagaNawierzchni,
        boolean sledzony,
        Wezel wezelStartowy,
        Moment momentStartu,
        MaszynaLosujaca maszynaLosujaca) {
        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wspolczynnikZnudzenia = wagaZnudzenia;
        this.wagaTrudnosci = wagaTrudnosci;
        this.wagaNawierzchni = wagaNawierzchni;
        this.wagaZnudzenia = wspolczynnikZnudzenia;
        this.sledzony = sledzony;
        this.wezelStartowy = wezelStartowy;
        this.momentStartu = momentStartu;
        this.maszynaLosujaca = maszynaLosujaca;
    }

    /**
     * Konstruktor używany przez metodę kopia.
     */
    public Sportowiec(Sportowiec inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        this(inny.id + przesuniecieId,
            inny.poziomZaawansowania,
            inny.wspolczynnikSpontanicznosci,
            inny.wspolczynnikZnudzenia,
            inny.wagaTrudnosci,
            inny.wagaNawierzchni,
            inny.wagaZnudzenia,
            inny.sledzony,
            inny.wezelStartowy,
            inny.momentStartu.dodajInterwal(przesuniecieMomentuStartu),
            inny.maszynaLosujaca);
    }

    public int id() {
        return id;
    }

    public int poziomZaawansowania() {
        return poziomZaawansowania;
    }

    public double wspolczynnikTrudnosci() {
        return wagaTrudnosci;
    }

    public double wspolczynnikNawierzchni() {
        return wagaNawierzchni;
    }

    public double wspolczynnikSpontanicznosci() {
        return wspolczynnikSpontanicznosci;
    }

    public boolean sledzony() {
        return sledzony;
    }

    public Wezel wezelStartowy() {
        return wezelStartowy;
    }

    public Moment momentStartu() {
        return momentStartu;
    }

    /**
     * Dla zadanego obecnego węzła daje wydarzenie opisujące następny
     * krok sportowca: dołączenie do kolejki na wyciągu lub rozpoczęcie zjazdu trasą.
     */
    public abstract Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel);

    protected boolean czyTerazSpontaniczny()
    {
        return maszynaLosujaca.losowyDouble(0, 1) < wspolczynnikSpontanicznosci();
    }
    /**
     * Spontaniczna decyzja polega na wylosowaniu następnej krawędzi jednostajnie
     * spośród wszystkich zaczynających się w obecnym wierzchołku.
     */
    protected Zdarzenie podejmijSpontanicznaDecyzje(Moment moment, Wezel obecnyWezel) {
        Krawedz[] bezposrednieKrawedzie = obecnyWezel.wychodzaceKrawedzie();

        int losowyWybor = maszynaLosujaca.losowyInt(0, bezposrednieKrawedzie.length);
        Krawedz wybranaKrawedz = bezposrednieKrawedzie[losowyWybor];

        OdwiedzajacyNastepnyKrok odwiedzajacy = new OdwiedzajacyNastepnyKrok(this, moment);
        return wybranaKrawedz.odwiedz(odwiedzajacy);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    protected double lacznaAtrakcyjnosc(Trasa trasa) {
        return wagaTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
            + wagaNawierzchni * trasa.wyrownanieNawierzchni()
            + wagaZnudzenia * (1 - znudzenia.get(trasa));
    }

    private double atrakcyjnoscPoziomuTrudnosci(Trasa trasa) {
        int poziomTrudnosci = trasa.poziomTrudnosci();

        double roznicaPoziomow = poziomTrudnosci - poziomZaawansowania;

        if (poziomTrudnosci >= poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI) {
            return 0;
        } else if (poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI > poziomTrudnosci
            && poziomTrudnosci >= poziomZaawansowania) {
            return 1.0 - roznicaPoziomow / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI;
        } else {
            return Math.max(DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY,
                1 - (-roznicaPoziomow) / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY);
        }
    }

    public void przeliczZnudzenie(Trasa trasa) {
        int czasOdOstatniegoPrzejazdu = liczbaPrzejechanychTras - kiedyOdwiedzonoTrasy.get(trasa);
        double stareZnudzenie;

        if (znudzenia.containsKey(trasa)) {
            stareZnudzenie = znudzenia.get(trasa);
        }
        else {
            stareZnudzenie = 1;
        }

        double noweZnudzenie = stareZnudzenie * Math.pow(1 - wspolczynnikZnudzenia, czasOdOstatniegoPrzejazdu);
        znudzenia.put(trasa, noweZnudzenie);
    }

    public void zanotujPrzejazdTrasa(Trasa trasa) {
        zanotujOstatnieOdwiedziny(trasa);
    }

    protected void zanotujOstatnieOdwiedziny(Trasa trasa) {
        kiedyOdwiedzonoTrasy.put(trasa, liczbaPrzejechanychTras);
        liczbaPrzejechanychTras++;
    }

    /**
     * Tworzy kopie sportowca z tymi samymi parametrami, ale ze zwiększonym id oraz momentem startu.
     * Używane na potrzeby tworzenia wielu sportowców z jednej grupy sportowców z wejścia.
     */

    public abstract Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu);

    @Override
    public String toString() {
        return String.format("Sportowiec nr %d", id);
    }
}
