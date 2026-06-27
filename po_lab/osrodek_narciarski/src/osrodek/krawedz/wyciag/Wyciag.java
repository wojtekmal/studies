package osrodek.krawedz.wyciag;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import kolejkaZdarzen.zdarzenia.DotarcieDoWezla;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.OdwiedzajacyKrawedz;
import sportowcy.Sportowiec;
import symulacja.Symulacja;
import wczytywacz.WyjatekDaneWejsciowe;

public class Wyciag extends Krawedz {

    private final Interwal odstepMiedzyOdjazdami;

    private final int ladownosc;

    private final KolejkaSportowcow obecnaKolejka;

    private int lacznaLiczbaPasazerow;

    private int maksymalnaDlugoscKolejki = 0;

    private int sumaDlugosciKolejekiPoSekundach = 0;

    private Moment ostatniaAktualizacjaStatystyk = Symulacja.POCZATEK_DNIA;

    private final static int LICZBA_SEKUND_W_SYMULACJI = Symulacja.POCZATEK_DNIA.policzInterwal(Symulacja.KONIEC_SYMULACJI).sekundy();

    public Wyciag(int id,
        Wezel poczatek,
        Wezel koniec,
        Interwal odstepMiedzyOdjazdami,
        Interwal dlugoscPrzejazdu,
        int ladownosc) throws WyjatekDaneWejsciowe {
        super(id, poczatek, koniec, dlugoscPrzejazdu);

        if (poczatek.wysokosc() >= koniec.wysokosc()) {
            throw new WyjatekDaneWejsciowe(String.format("Wyciąg %d prowadzi w dół: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc()));
        }

        this.odstepMiedzyOdjazdami = odstepMiedzyOdjazdami;
        this.ladownosc = ladownosc;
        obecnaKolejka = new BuforCyklicznySportowcow();
        lacznaLiczbaPasazerow = 0;
    }

    public int lacznaLiczbaPasazerow() {
        return lacznaLiczbaPasazerow;
    }

    public int maksymalnaDlugoscKolejki() {
        return maksymalnaDlugoscKolejki;
    }

    public void dodajDoKolejki(Sportowiec sportowiec) {
        obecnaKolejka.dodaj(sportowiec);
    }

    /**
     *  Symuluje odjazd następnego wagonika oraz daje nowo stworzone zdarzenia:
     * 1. Zdarzenie reprezentujące odjazd następnego wagonika.
     * 2. Zdarzenia reprezentujące dotarcie do końca wyciągu sportowców, którzy załapali się na obecny odjazd.
     */
    public Zdarzenie[] odjazd(Moment moment, Dziennik dziennik) {
        zaktualizujStatystyki(moment);

        Sportowiec[] odjezdzajacySportowcy = obecnaKolejka.zdejmij(Math.min(obecnaKolejka.rozmiar(), ladownosc));
        lacznaLiczbaPasazerow += odjezdzajacySportowcy.length;

        for (Sportowiec sportowiec : odjezdzajacySportowcy) {
            sportowiec.zanotujPrzejazdWyciagiem(this);
            dziennik.dodajWpisZeSportowcem(moment, sportowiec, String.format("rozpoczął wjazd %s", toString()));
        }

        Zdarzenie[] noweZdarzenia = new Zdarzenie[1 + odjezdzajacySportowcy.length];
        noweZdarzenia[0] = new OdjazdWyciagu(moment.dodajInterwal(odstepMiedzyOdjazdami), this);

        for (int i = 0; i < odjezdzajacySportowcy.length; i++) {
            noweZdarzenia[1 + i] = new DotarcieDoWezla(moment.dodajInterwal(dlugosc()),
                this,
                koniec(),
                odjezdzajacySportowcy[i]);
        }

        return noweZdarzenia;
    }

    @Override
    public String wypiszParametry() {
        return String.format("w%d: %d os. co %ds\nczas: %ds",
            id(),
            ladownosc,
            odstepMiedzyOdjazdami.sekundy(),
            dlugosc().sekundy());
    }

    @Override
    public String wypiszStatystyki() {
        return String.format("w%d: kol: %d(śr), %d(maks)\nwjazdy: %d / %d (%d%%)",
            id(),
            sredniaDlugoscKolejki(),
            maksymalnaDlugoscKolejki,
            lacznaLiczbaPasazerow,
            maksymalnaMozliwaLiczbaPasazerow(),
            procentObciazenia());
    }

    private int sredniaDlugoscKolejki() {
        double sumaDlugosci = sumaDlugosciKolejekiPoSekundach;
        return Math.toIntExact(Math.round(sumaDlugosci / LICZBA_SEKUND_W_SYMULACJI));
    }

    public int maksymalnaMozliwaLiczbaPasazerow() {
        return LICZBA_SEKUND_W_SYMULACJI / odstepMiedzyOdjazdami.sekundy() * ladownosc;
    }

    public int procentObciazenia()
    {
        double liczbaPasazerow = lacznaLiczbaPasazerow;
        return Math.toIntExact(Math.round(liczbaPasazerow / maksymalnaMozliwaLiczbaPasazerow() * 100));
    }

    @Override
    public String toString() {
        return String.format("Wyciąg nr %d", id());
    }

    @Override
    public <R> R odwiedz(OdwiedzajacyKrawedz<R> odwiedzajacyKrawedz) {
        return odwiedzajacyKrawedz.wyciag(this);
    }

    public void zaktualizujStatystyki(Moment moment) {
        int dlugoscKolejki = obecnaKolejka.rozmiar();
        Interwal interwalZStalalKolejka = ostatniaAktualizacjaStatystyk.policzInterwal(moment);

        maksymalnaDlugoscKolejki = Math.max(maksymalnaDlugoscKolejki, dlugoscKolejki);
        sumaDlugosciKolejekiPoSekundach += dlugoscKolejki * interwalZStalalKolejka.sekundy();

        ostatniaAktualizacjaStatystyk = moment;
    }
}
