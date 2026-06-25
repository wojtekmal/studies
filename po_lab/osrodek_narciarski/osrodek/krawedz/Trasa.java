package osrodek_narciarski.osrodek.krawedz;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.osrodek.Wezel;

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
        double odpornoscNaNierownosci) {
        super(id, poczatek, koniec, dlugoscZjazdu);

        assert poczatek.wysokosc() > koniec.wysokosc()
            : String.format("Trasa %d prowadzi w górę: %d -> %d", id, poczatek.wysokosc(), koniec.wysokosc());

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

    public double wyrownanieNawierzchni() {
        return (bazowaAtrakcyjnosc + (1 - bazowaAtrakcyjnosc) * Math.pow(odpornoscNaNierownosci, liczbaZjazdow));
    }

    public int liczbaZjazdow()
    {
        return liczbaZjazdow;
    }

    @Override
    public String wypiszStatystyki() {
        return String.format("%d zjazdów", liczbaZjazdow);
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
