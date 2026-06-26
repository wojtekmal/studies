import java.util.Locale;
import java.util.Scanner;

import dziennik.DziennikStdioIMapki;
import generatorMapek.pliki.WyjatekSystemuPlikow;
import kolejkaZdarzen.KolejkaZdarzenStandardowa;
import losowosc.DeterministycznaMaszynaLosujaca;
import symulacja.Symulacja;
import wczytywacz.DaneWejsciowe;
import wczytywacz.Wczytywacz;
import wczytywacz.WyjatekDaneWejsciowe;

public class Main {

    private static class WyjatekZleArgumenty extends Exception {
        public WyjatekZleArgumenty(String komunikat) {
            super(komunikat);
        }
    }

    public static void main(String[] args)
    {
        try {
            sprawdzArgumenty(args);
            DaneWejsciowe daneWejsciowe = wczytajWejscie();

            new Symulacja().przeprowadzSymulacje(new DziennikStdioIMapki(),
                    new KolejkaZdarzenStandardowa(),
                    daneWejsciowe.osrodek(),
                    daneWejsciowe.sportowcy());
        }
        catch (WyjatekDaneWejsciowe wyjatekDaneWejsciowe) {
            System.err.println("Wykryto błąd w danych wejściowych:");
            System.err.println(wyjatekDaneWejsciowe.getMessage());
        }
        catch (WyjatekSystemuPlikow wyjatekSystemuPlikow) {
            System.err.println("Wykryto błąd związany z systemem plików:");
            System.err.println(wyjatekSystemuPlikow.getMessage());
        }
        catch (WyjatekZleArgumenty wyjatekZleArgumenty) {
            System.err.println("Podano niewłaściwe parametry do programu.");
            System.err.println(wyjatekZleArgumenty.getMessage());
        }
        catch(Exception wyjatek) {
            System.err.println("Wystąpił błąd:");
            System.err.println(wyjatek);
            System.err.println("Prosimy o zgłoszenie tego zespołowi deweloperskiemu.");
        }
    }

    private static void sprawdzArgumenty(String[] args) throws WyjatekZleArgumenty
    {
        if (args.length != 1) {
            throw new WyjatekZleArgumenty("Użycie: java -ea pakiet.GłównaKlasa \"sciezka/katalogu/do/mapek\"");
        }
    }

    private static DaneWejsciowe wczytajWejscie() throws Wczytywacz.WyjatekRodzajSportowca
    {
        Scanner scanner = new Scanner(System.in);

        // Ustawiamy region na angielski, żeby Scanner parsował liczby
        // zmiennoprzecinkowe z '.' zamiast ','.
        scanner.useLocale(Locale.ENGLISH);

        Wczytywacz wczytywacz = new Wczytywacz(scanner, new DeterministycznaMaszynaLosujaca(0));
        return wczytywacz.wczytajWejscie();
    }
}
