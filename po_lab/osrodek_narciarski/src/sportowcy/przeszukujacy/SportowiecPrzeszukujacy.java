package sportowcy.przeszukujacy;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import sportowcy.OdwiedzajacyNastepnyKrok;
import sportowcy.ParametrySportowca;
import sportowcy.Sportowiec;

import java.util.*;

public abstract class SportowiecPrzeszukujacy extends Sportowiec {

    private final Stack<Krawedz> plan = new Stack<>();

    public SportowiecPrzeszukujacy(ParametrySportowca parametry) {
        super(parametry);
    }

    public SportowiecPrzeszukujacy(Sportowiec doSkopiowania, int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        super(doSkopiowania, przesuniecieId, przesuniecieMomentuStartu);
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
