package dziennik;

import czas.Moment;
import generatorMapek.pliki.WyjatekSystemuPlikow;
import osrodek.Osrodek;
import osrodek.krawedz.Krawedz;
import sportowcy.Sportowiec;

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
}
