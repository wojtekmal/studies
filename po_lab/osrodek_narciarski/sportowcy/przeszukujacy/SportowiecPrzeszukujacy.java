package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;
import osrodek_narciarski.sportowcy.Sportowiec;
import osrodek_narciarski.sportowcy.krotkowzroczny.SportowiecKrotkowzroczny;

import java.util.*;

public abstract class SportowiecPrzeszukujacy extends Sportowiec {

    private final Deque<Krawedz> plan = new ArrayDeque<>();

    public SportowiecPrzeszukujacy(int id,
        int poziomZaawansowania,
        double wspolczynnikSpontanicznosci,
        double wspolczynnikZnudzenia,
        double wagaZnudzenia,
        double wagaTrudnosci,
        double wagaNawierzchni,
        boolean sledzony,
        Wezel wezelStartowy,
        Moment momentStartu,
        MaszynaLosujaca maszynaLosujaca) {
        super(id,
            poziomZaawansowania,
            wspolczynnikSpontanicznosci,
            wspolczynnikZnudzenia,
            wagaZnudzenia,
            wagaTrudnosci,
            wagaNawierzchni,
            sledzony,
            wezelStartowy,
            momentStartu,
            maszynaLosujaca);
    }

    public SportowiecPrzeszukujacy(SportowiecZachlanny inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        super(inny, przesuniecieId, przesuniecieMomentuStartu);
    }

    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel) {
        if (plan.isEmpty())
        {
            if (czyTerazSpontaniczny())
            {
                return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
            }
            else {
                ulozPlan(obecnyWezel);
            }
        }
    }

    public void ulozPlan(Wezel obecnyWezel) {
        Trasa najlepszaTrasa = null;
        Map<Krawedz, TrasaCzyWyciag> rodzajePoprzednich = new HashMap<>();
        Map<Krawedz, Trasa> poprzednieTrasy = new HashMap<>();
        Map<Krawedz, Wyciag> poprzednieWyciagi = new HashMap<>();
        Queue

        for (Trasa trasa : obecnyWezel.wychodzaceTrasy()) {
            rodzajePoprzednich.put(trasa, null);
        }
    }
}
