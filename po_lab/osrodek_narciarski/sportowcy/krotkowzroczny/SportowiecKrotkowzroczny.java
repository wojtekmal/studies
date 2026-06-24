package osrodek_narciarski.sportowcy.krotkowzroczny;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;
import osrodek_narciarski.sportowcy.Sportowiec;

public class SportowiecKrotkowzroczny extends Sportowiec {

    public SportowiecKrotkowzroczny(int id,
        int poziomZaawansowania,
        double wspolczynnikSpontanicznosci,
        double wspolczynnikTrudnosci,
        double wspolczynnikNawierzchni,
        double wspolczynnikZnudzenia,
        boolean sledzony,
        Wezel wezelStartowy,
        Moment momentStartu,
        MaszynaLosujaca maszynaLosujaca) {
        super(id,
            poziomZaawansowania,
            wspolczynnikSpontanicznosci,
            wspolczynnikTrudnosci,
            wspolczynnikNawierzchni,
                wspolczynnikZnudzenia,
            sledzony,
            wezelStartowy,
            momentStartu,
            maszynaLosujaca);
    }

    public SportowiecKrotkowzroczny(SportowiecKrotkowzroczny inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        super(inny, przesuniecieId, przesuniecieMomentuStartu);
    }

    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel) {
        if (czyTerazSpontaniczny()) {
            return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        } else {
            return podejmijPrzemyslanaDecyzje(moment, obecnyWezel);
        }
    }

    /**
     * Przemyślana decyzja polega na wybraniu najatrakcyjniejszej trasy spośród sumy tras
     * zaczynających się w obecnym wierzchołku oraz tras, których początkowe wierzchołki
     * są osiągalne wyciągiem zaczynającym się w obecnym wierzchołku.
     */
    private Zdarzenie podejmijPrzemyslanaDecyzje(Moment moment, Wezel obecnyWezel) {
        Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

        double najwiekszaAtrakcyjnosc = -1;
        Trasa najlepszaTrasa = null;
        Wyciag nastepnyWyciag = null;

        for (Trasa trasa : bezposrednieTrasy) {
            double atrakcyjnosc = lacznaAtrakcyjnosc(trasa);
            if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
                najwiekszaAtrakcyjnosc = atrakcyjnosc;
                najlepszaTrasa = trasa;
            }
        }

        for (Wyciag wyciag : wyciagi) {
            for (Trasa trasa : wyciag.koniec().wychodzaceTrasy()) {
                double atrakcyjnosc = lacznaAtrakcyjnosc(trasa);
                if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
                    najwiekszaAtrakcyjnosc = atrakcyjnosc;
                    najlepszaTrasa = trasa;
                    nastepnyWyciag = wyciag;
                }
            }
        }

        if (najlepszaTrasa == null) {
            // Zbiór dostępnych tras jest pusty, wiec wybieramy dowolny wyciąg.
            // Mamy gwarancję, że taki istnieje, ponieważ graf jest silnie spójny.
            return nastepnyKrokWyciag(moment, wyciagi[0]);
        } else if (nastepnyWyciag == null) {
            // Wybrana trasa zaczyna się w obecnym wierzchołku.
            return nastepnyKrokTrasa(moment, najlepszaTrasa);
        } else {
            // Musimy wjechać wyciągiem, by dotrzeć do upatrzonej trasy.
            return nastepnyKrokWyciag(moment, nastepnyWyciag);
        }
    }

    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecKrotkowzroczny(this, przesuniecieId, przesuniecieMomentuStartu);
    }
}
