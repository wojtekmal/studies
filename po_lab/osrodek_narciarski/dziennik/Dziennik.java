package osrodek_narciarski.dziennik;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.sportowcy.Sportowiec;

public interface Dziennik {

    /**
     * Dodaje wpis o zadanym momencie i treści.
     */
    void dodajWpis(Moment moment, String wpis);

    /**
     * Dodaje wpis o zadanym momencie, dotyczący danego sportowca.
     */
    void dodajWpisZeSportowcem(Moment moment, Sportowiec sportowiec, String coZrobil);

    void dodajTabele(String[][] wiersze);

    void dodajParametryOsrodka(String[] opisyWezlow, String[] opisyTras, String[] opisyWyciagow);

    void dodajStatystykiKoncowe(String[] statystykiTras, String[] statystykiWyciagow);

    void dodajPrzejazdySportowca(int idSportowca, Krawedz[] odwiedzoneKrawedzie);
}
