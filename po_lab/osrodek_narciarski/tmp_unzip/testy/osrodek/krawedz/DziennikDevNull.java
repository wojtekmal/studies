package osrodek.krawedz;

import czas.Moment;
import dziennik.Dziennik;
import generatorMapek.pliki.WyjatekSystemuPlikow;
import osrodek.Osrodek;
import sportowcy.Sportowiec;

public class DziennikDevNull implements Dziennik
{
    @Override
    public void dodajWpis(Moment moment, String wpis) {

    }

    @Override
    public void dodajWpisZeSportowcem(Moment moment, Sportowiec sportowiec, String coZrobil) {

    }

    @Override
    public void dodajTabele(String[][] wiersze) {

    }

    @Override
    public void dodajParametryOsrodka(Osrodek osrodek, String[] opisyTras, String[] opisyWyciagow) throws WyjatekSystemuPlikow {

    }

    @Override
    public void dodajStatystykiKoncowe(Osrodek osrodek, String[] statystykiTras, String[] statystykiWyciagow) throws WyjatekSystemuPlikow {

    }

    @Override
    public void dodajPrzejazdySportowca(Osrodek osrodek, int idSportowca, Krawedz[] odwiedzoneKrawedzie) throws WyjatekSystemuPlikow {

    }
}
