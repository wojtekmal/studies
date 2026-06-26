package sportowcy;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

import java.util.HashMap;
import java.util.Map;

public abstract class Sportowiec {

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;

    private final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final ParametrySportowca parametry;

    private final Map<Trasa, Double> znudzenia = new HashMap<>();

    private final Map<Trasa, Integer> kiedyOdwiedzonoTrasy = new HashMap<>();

    private int liczbaPrzejechanychTras = 0;

    public Sportowiec(ParametrySportowca parametry) {
        this.parametry = parametry;
    }

    public Sportowiec(Sportowiec doSkopiowania, int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        this(doSkopiowania.parametry.kopia(przesuniecieId, przesuniecieMomentuStartu));
    }

    public boolean sledzony() {
        return parametry.sledzony();
    }

    public Wezel wezelStartowy() {
        return parametry.wezelStartowy();
    }

    public Moment momentStartu() {
        return parametry.momentStartu();
    }

    /**
     * Dla zadanego obecnego węzła daje wydarzenie opisujące następny
     * krok sportowca: dołączenie do kolejki na wyciągu lub rozpoczęcie zjazdu trasą.
     */
    public abstract Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel);

    protected boolean czyTerazSpontaniczny()
    {
        return parametry.maszynaLosujaca().losowyDouble(0, 1) < parametry.wspolczynnikSpontanicznosci();
    }
    /**
     * Spontaniczna decyzja polega na wylosowaniu następnej krawędzi jednostajnie
     * spośród wszystkich zaczynających się w obecnym wierzchołku.
     */
    protected Zdarzenie podejmijSpontanicznaDecyzje(Moment moment, Wezel obecnyWezel) {
        Krawedz[] bezposrednieKrawedzie = obecnyWezel.wychodzaceKrawedzie();

        int losowyWybor = parametry.maszynaLosujaca().losowyInt(0, bezposrednieKrawedzie.length);
        Krawedz wybranaKrawedz = bezposrednieKrawedzie[losowyWybor];

        OdwiedzajacyNastepnyKrok odwiedzajacy = new OdwiedzajacyNastepnyKrok(this, moment);
        return wybranaKrawedz.odwiedz(odwiedzajacy);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    protected double lacznaAtrakcyjnosc(Trasa trasa) {
        return parametry.wagaTrudnosci() * atrakcyjnoscPoziomuTrudnosci(trasa)
            + parametry.wagaNawierzchni() * trasa.atrakcyjnoscNawierzchni()
            + parametry.wagaZnudzenia() * (1 - znudzenia.get(trasa));
    }

    private double atrakcyjnoscPoziomuTrudnosci(Trasa trasa) {
        int poziomTrudnosci = trasa.poziomTrudnosci();

        double roznicaPoziomow = poziomTrudnosci - parametry.poziomZaawansowania();

        if (poziomTrudnosci >= parametry.poziomZaawansowania() + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI) {
            return 0;
        } else if (parametry.poziomZaawansowania() + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI > poziomTrudnosci
            && poziomTrudnosci >= parametry.poziomZaawansowania()) {
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

        double noweZnudzenie = stareZnudzenie * Math.pow(1 - parametry.wspolczynnikZnudzenia(), czasOdOstatniegoPrzejazdu);
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
        return String.format("Sportowiec nr %d", parametry.id());
    }
}
