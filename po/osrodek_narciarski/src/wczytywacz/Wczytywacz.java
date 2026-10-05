package wczytywacz;

import java.util.Scanner;

import czas.Interwal;
import czas.Moment;
import losowosc.MaszynaLosujaca;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.GrupaSportowcow;
import sportowcy.ParametrySportowca;
import sportowcy.Sportowiec;
import sportowcy.krotkowzroczny.SportowiecKrotkowzroczny;
import sportowcy.przeszukujacy.SportowiecKolekcjoner;
import sportowcy.przeszukujacy.SportowiecZachlanny;

public class Wczytywacz {
    public static class WyjatekRodzajSportowca extends WyjatekDaneWejsciowe {
        public WyjatekRodzajSportowca(String wiadomosc) {
            super(wiadomosc);
        }
    }

    private final Scanner scanner;

    private final MaszynaLosujaca maszynaLosujaca;

    public Wczytywacz(Scanner scanner, MaszynaLosujaca maszynaLosujaca) {
        this.scanner = scanner;
        this.maszynaLosujaca = maszynaLosujaca;
    }

    public DaneWejsciowe wczytajWejscie() throws WyjatekDaneWejsciowe
    {
        Wezel[] wezly = wczytajWezly();
        Wyciag[] wyciagi = wczytajWyciagi(wezly);
        Trasa[] trasy = wczytajTrasy(wezly);
        GrupaSportowcow[] grupySportowcow = wczytajGrupySportowcow(wezly);
        return new DaneWejsciowe(wezly, trasy, wyciagi, grupySportowcow);
    }

    private Wezel[] wczytajWezly() {
        int liczbaWezlow = scanner.nextInt();

        Wezel[] wezly = new Wezel[liczbaWezlow];

        for (int id = 0; id < liczbaWezlow; id++) {
            int wysokosc = scanner.nextInt();
            int wspolrzednaX = scanner.nextInt();
            int wspolrzednaY = scanner.nextInt();
            boolean czyStartowy = scanner.findInLine("s") != null;
            wezly[id] = new Wezel(id, wysokosc, wspolrzednaX, wspolrzednaY, czyStartowy);
        }

        return wezly;
    }

    private Wyciag[] wczytajWyciagi(Wezel[] wezly) throws WyjatekDaneWejsciowe {
        int liczbaWyciagow = scanner.nextInt();

        Wyciag[] wyciagi = new Wyciag[liczbaWyciagow];

        for (int id = 0; id < liczbaWyciagow; id++) {
            int poczatek = scanner.nextInt();
            int koniec = scanner.nextInt();
            int odstep = scanner.nextInt();
            int maksymalnaWielkoscGrupy = scanner.nextInt();
            int czasPrzejazdu = scanner.nextInt();

            Wyciag wyciag = new Wyciag(id,
                wezly[poczatek],
                wezly[koniec],
                new Interwal(odstep),
                new Interwal(czasPrzejazdu),
                maksymalnaWielkoscGrupy);

            wyciagi[id] = wyciag;
        }

        return wyciagi;
    }

    private Trasa[] wczytajTrasy(Wezel[] wezly) throws WyjatekDaneWejsciowe {
        int liczbaTras = scanner.nextInt();

        Trasa[] trasy = new Trasa[liczbaTras];

        for (int id = 0; id < liczbaTras; id++) {
            int poczatek = scanner.nextInt();
            int koniec = scanner.nextInt();
            int poziomTrudnosci = scanner.nextInt();
            int czasPrzejazdu = scanner.nextInt();
            Interwal dlugosc = new Interwal(czasPrzejazdu);
            double bazowaAtrakcyjnosc = scanner.nextDouble();
            double odpornoscNaNierownosci = scanner.nextDouble();

            Trasa trasa = new Trasa(id,
                wezly[poczatek],
                wezly[koniec],
                dlugosc,
                poziomTrudnosci,
                bazowaAtrakcyjnosc,
                odpornoscNaNierownosci);

            trasy[id] = trasa;
        }

        return trasy;
    }

    private GrupaSportowcow[] wczytajGrupySportowcow(Wezel[] wezly) throws WyjatekRodzajSportowca
    {
        int nastepneId = 0;
        int liczbaGrup = scanner.nextInt();

        GrupaSportowcow[] grupySportowcow = new GrupaSportowcow[liczbaGrup];

        for (int grupa = 0; grupa < liczbaGrup; grupa++) {
            grupySportowcow[grupa] = wczytajGrupeSportowcow(nastepneId, wezly);
            nastepneId += grupySportowcow[grupa].krotnosc();
        }

        return grupySportowcow;
    }

    private GrupaSportowcow wczytajGrupeSportowcow(int nastepneId, Wezel[] wezly) throws WyjatekRodzajSportowca
    {
        int liczbaSportowcowWGrupie = scanner.nextInt();
        int poziomZaawansowania = scanner.nextInt();
        double wspolczynnikSpontanicznosci = scanner.nextDouble();
        double wspolczynnikZnudzenia = scanner.nextDouble();
        String rodzajSportowcow = scanner.next();
        boolean czySledzeni = scanner.findInLine("s") != null;

        double wagaDopasowania = scanner.nextDouble();
        double wagaNawierzchni = scanner.nextDouble();
        double wagaZnudzenia = scanner.nextDouble();
        int idPoczatkowegoWezla = scanner.nextInt();

        Moment start = wczytajMoment();

        Interwal odstepCzasowy = new Interwal(0);

        if (liczbaSportowcowWGrupie > 1) {
            odstepCzasowy = new Interwal(scanner.nextInt());
        }

        ParametrySportowca parametryPierwszegoSportowca = new ParametrySportowca(nastepneId,
            poziomZaawansowania,
            wspolczynnikSpontanicznosci,
            wspolczynnikZnudzenia,
            wagaDopasowania,
            wagaNawierzchni,
            wagaZnudzenia,
            czySledzeni,
            wezly[idPoczatkowegoWezla],
            start,
            maszynaLosujaca);

        Sportowiec pierwszySportowiec = pierwszySportowiec(rodzajSportowcow, parametryPierwszegoSportowca);

        return new GrupaSportowcow(pierwszySportowiec, liczbaSportowcowWGrupie, odstepCzasowy);
    }

    private static Sportowiec pierwszySportowiec(String rodzajSportowcow, ParametrySportowca parametry) throws WyjatekRodzajSportowca
    {
        Sportowiec pierwszySportowiec = null;

        switch (rodzajSportowcow) {
            case "L" -> pierwszySportowiec = new SportowiecKrotkowzroczny(parametry);
            case "Z" -> pierwszySportowiec = new SportowiecZachlanny(parametry);
            case "K" -> pierwszySportowiec = new SportowiecKolekcjoner(parametry);
            default -> {
                throw new WyjatekRodzajSportowca(rodzajSportowcow);
            }
        }
        return pierwszySportowiec;
    }

    private Moment wczytajMoment() {
        String napis = scanner.next();
        String[] napisy = napis.split(":");
        int[] liczby = new int[napisy.length];

        for (int i = 0; i < liczby.length; i++) {
            liczby[i] = Integer.parseInt(napisy[i]);
        }

        return new Moment(liczby[0], liczby[1], liczby[2]);
    }
}
