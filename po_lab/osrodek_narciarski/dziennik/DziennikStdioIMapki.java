package osrodek_narciarski.dziennik;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.sportowcy.Sportowiec;

public class DziennikStdioIMapki implements Dziennik {


    @Override
    public void dodajWpis(Moment moment, String wpis) {
        System.out.printf("%s: %s%n", moment.toString(), wpis);
    }

    @Override
    public void dodajWpisZeSportowcem(Moment moment, Sportowiec sportowiec, String coZrobil) {
        if (sportowiec.sledzony()) {
            dodajWpis(moment, String.format("%s %s.", sportowiec, coZrobil));
        }
    }

    @Override
    public void dodajTabele(String[][] wiersze) {
        int maxDlugoscKlucza = 0;

        for (String[] wiersz : wiersze) {
            maxDlugoscKlucza = Math.max(maxDlugoscKlucza, wiersz[0].length());
        }

        String formatWiersza = "%-" + maxDlugoscKlucza + "s: %s%n";

        for (String[] wiersz : wiersze) {
            System.out.format(formatWiersza, wiersz[0], wiersz[1]);
        }
    }

    @Override
    public void dodajParametryOsrodka(String[] opisyWezlow, String[] opisyTras, String[] opisyWyciagow) {

    }

    @Override
    public void dodajStatystykiKoncowe(String[] statystykiTras, String[] statystykiWyciagow) {

    }

    @Override
    public void dodajPrzejazdySportowca(int idSportowca, Krawedz[] odwiedzoneKrawedzie) {

    }
}
