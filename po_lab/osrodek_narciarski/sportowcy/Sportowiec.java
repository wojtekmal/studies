package osrodek_narciarski.sportowcy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.DolaczenieDoKolejki;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;

public abstract class Sportowiec {

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;

    private final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;

    private final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final int id;

    private final int poziomZaawansowania; // {0, 1, ..., 10}

    private final double wspolczynnikSpontanicznosci; // [0, 1]

    private final double wspolczynnikTrudnosci; // [0, 1]

    private final double wspolczynnikNawierzchni; // [0, 1]

    private final double wspolczynnikZnuzenia; // [0, 1]

    private final boolean sledzony;

    private final Wezel wezelStartowy;

    private final Moment momentStartu;

    private final MaszynaLosujaca maszynaLosujaca;

    public Sportowiec(int id,
        int poziomZaawansowania,
        double wspolczynnikSpontanicznosci,
        double wspolczynnikTrudnosci,
        double wspolczynnikNawierzchni,
        double wspolczynnikZnuzenia,
        boolean sledzony,
        Wezel wezelStartowy,
        Moment momentStartu,
        MaszynaLosujaca maszynaLosujaca) {
        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wspolczynnikTrudnosci = wspolczynnikTrudnosci;
        this.wspolczynnikNawierzchni = wspolczynnikNawierzchni;
        this.wspolczynnikZnuzenia = wspolczynnikZnuzenia;
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
            inny.wspolczynnikTrudnosci,
            inny.wspolczynnikNawierzchni,
            inny.wspolczynnikZnuzenia,
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
        return wspolczynnikTrudnosci;
    }

    public double wspolczynnikNawierzchni() {
        return wspolczynnikNawierzchni;
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
        Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

        int losowyWybor = maszynaLosujaca.losowyInt(0, bezposrednieTrasy.length + wyciagi.length);

        if (losowyWybor < bezposrednieTrasy.length) {
            return nastepnyKrokTrasa(moment, bezposrednieTrasy[losowyWybor]);
        } else {
            return nastepnyKrokWyciag(moment, wyciagi[losowyWybor - bezposrednieTrasy.length]);
        }
    }

    /**
     * Tworzy zdarzenie dołączenia do kolejki do wyciągu w następnym kroku.
     */
    protected DolaczenieDoKolejki nastepnyKrokWyciag(Moment moment, Wyciag wyciag) {
        return new DolaczenieDoKolejki(moment, wyciag, this);
    }

    /**
     * Tworzy zdarzenie zjazdu bezpośrednią trasą w następnym kroku.
     */
    protected RozpoczecieZjazdu nastepnyKrokTrasa(Moment moment, Trasa trasa) {
        return new RozpoczecieZjazdu(moment, trasa, this);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    protected double lacznaAtrakcyjnosc(Trasa trasa) {
        return wspolczynnikTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
            + wspolczynnikNawierzchni * trasa.wyrownanieNawierzchni();
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
