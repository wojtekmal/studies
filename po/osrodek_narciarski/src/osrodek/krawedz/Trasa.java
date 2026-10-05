package osrodek.krawedz;

import czas.Interwal;
import czas.Moment;
import osrodek.Wezel;
import wczytywacz.WyjatekDaneWejsciowe;

public class Trasa extends Krawedz {

    private final int poziomTrudnosci; // {0, 1, ..., 10}

    private final double bazowaAtrakcyjnosc; // [0, 1]

    private final double odpornoscNaNierownosci; // [0, 1]

    private int liczbaZjazdow;

    public Trasa(int id,
        Wezel poczatek,
        Wezel koniec,
        Interwal dlugoscZjazdu,
        int poziomTrudnosci,
        double bazowaAtrakcyjnosc,
        double odpornoscNaNierownosci) throws WyjatekDaneWejsciowe {
        super(id, poczatek, koniec, dlugoscZjazdu);

        if (poczatek.wysokosc() <= koniec.wysokosc()) {
            throw new WyjatekDaneWejsciowe(String.format("Trasa %d prowadzi w górę: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc()));
        }

        this.poziomTrudnosci = poziomTrudnosci;
        this.bazowaAtrakcyjnosc = bazowaAtrakcyjnosc;
        this.odpornoscNaNierownosci = odpornoscNaNierownosci;
        liczbaZjazdow = 0;
    }

    public int poziomTrudnosci() {
        return poziomTrudnosci;
    }

    public Moment przemierz(Moment start) {
        liczbaZjazdow++;
        return start.dodajInterwal(dlugosc());
    }

    public double atrakcyjnoscNawierzchni() {
        return (bazowaAtrakcyjnosc + (1 - bazowaAtrakcyjnosc) * wyrownanieNawierzchni());
    }

    private double wyrownanieNawierzchni() {
        return Math.pow(odpornoscNaNierownosci, liczbaZjazdow);
    }

    public int liczbaZjazdow() {
        return liczbaZjazdow;
    }

    @Override
    public String wypiszParametry() {
        return String.format("t%d: poziom: %d, czas: %ds\nodporność: %.2f, %.5f",
            id(),
            poziomTrudnosci,
            dlugosc().sekundy(),
            bazowaAtrakcyjnosc,
            odpornoscNaNierownosci);
    }

    @Override
    public String wypiszStatystyki() {
        return String.format("t%d: śnieg: %.2f\nzjazdy: %d",
            id(),
            wyrownanieNawierzchni(),
            liczbaZjazdow);
    }

    @Override
    public String toString() {
        return String.format("Trasa nr %d", id());
    }

    @Override
    public <R> R odwiedz(OdwiedzajacyKrawedz<R> odwiedzajacyKrawedz) {
        return odwiedzajacyKrawedz.trasa(this);
    }
}
