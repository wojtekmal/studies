package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.sportowcy.OdwiedzajacyNastepnyKrok;
import osrodek_narciarski.sportowcy.Sportowiec;

import java.util.*;

public abstract class SportowiecPrzeszukujacy extends Sportowiec {

    private final Stack<Krawedz> plan = new Stack<>();

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

    public SportowiecPrzeszukujacy(SportowiecPrzeszukujacy inny,
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

        OdwiedzajacyNastepnyKrok odwiedzajacy = new OdwiedzajacyNastepnyKrok(this, moment);
        Krawedz nastepnaKrawedz = plan.pop();
        return nastepnaKrawedz.odwiedz(odwiedzajacy);
    }

    public void ulozPlan(Wezel obecnyWezel) {
        Queue<Krawedz> kolejkaBfs = new ArrayDeque<>();
        Map<Krawedz, Krawedz> poprzednieKrawedzie = new HashMap<>();
        OdwiedzajacyNajlepszaKrawedz odwiedzajacy = new OdwiedzajacyNajlepszaKrawedz(this);

        for (Krawedz krawedz : obecnyWezel.wychodzaceKrawedzie()) {
            kolejkaBfs.add(krawedz);
            poprzednieKrawedzie.put(krawedz, null);
        }

        while (!kolejkaBfs.isEmpty())
        {
            Krawedz krawedz = kolejkaBfs.remove();
            krawedz.odwiedz(odwiedzajacy);

            for (Krawedz sasiedniaKrawedz : krawedz.koniec().wychodzaceKrawedzie())
            {
                if (poprzednieKrawedzie.containsKey(sasiedniaKrawedz)) continue;

                poprzednieKrawedzie.put(sasiedniaKrawedz, krawedz);
                kolejkaBfs.add(sasiedniaKrawedz);
            }
        }

        Trasa najlepszaTrasa = odwiedzajacy.najlepszaTrasa();

        for (Krawedz krawedz = najlepszaTrasa; krawedz != null; krawedz = poprzednieKrawedzie.get(krawedz)) {
            plan.add(krawedz);
        }
    }

    public abstract boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa);
}
