package osrodek_narciarski.dziennik;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.osrodek.Osrodek;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;
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
    
    void dodajParametryOsrodka(Osrodek osrodek, String[] opisyTras, String[] opisyWyciagow);

    void dodajStatystykiKoncowe(Osrodek osrodek, String[] statystykiTras, String[] statystykiWyciagow);

    void dodajPrzejazdySportowca(Osrodek osrodek, int idSportowca, Krawedz[] odwiedzoneKrawedzie);
}
