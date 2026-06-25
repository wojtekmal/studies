package osrodek_narciarski;

import java.util.Locale;
import java.util.Scanner;

import osrodek_narciarski.dziennik.DziennikStdioIMapki;
import osrodek_narciarski.generatorMapek.pliki.WyjatekSystemuPlikow;
import osrodek_narciarski.kolejkaZdarzen.KolejkaZdarzenStandardowa;
import osrodek_narciarski.losowosc.DeterministycznaMaszynaLosujaca;
import osrodek_narciarski.symulacja.Symulacja;
import osrodek_narciarski.wczytywacz.DaneWejsciowe;
import osrodek_narciarski.wczytywacz.Wczytywacz;

public class Main {

    public static void main(String[] args) throws WyjatekSystemuPlikow
    {
        DaneWejsciowe daneWejsciowe = wczytajWejscie();

        new Symulacja().przeprowadzSymulacje(new DziennikStdioIMapki(),
            new KolejkaZdarzenStandardowa(),
            daneWejsciowe.osrodek(),
            daneWejsciowe.sportowcy());
    }

    private static DaneWejsciowe wczytajWejscie() {
        Scanner scanner = new Scanner(System.in);

        // Ustawiamy region na angielski, żeby Scanner parsował liczby
        // zmiennoprzecinkowe z '.' zamiast ','.
        scanner.useLocale(Locale.ENGLISH);

        Wczytywacz wczytywacz = new Wczytywacz(scanner, new DeterministycznaMaszynaLosujaca(0));
        return wczytywacz.wczytajWejscie();
    }
}
