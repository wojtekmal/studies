package sportowcy.przeszukujacy;

import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

import java.util.*;

public class Bfs
{

    private final SportowiecPrzeszukujacy sportowiec;

    public Bfs(SportowiecPrzeszukujacy sportowiec) {
        this.sportowiec = sportowiec;
    }

    public Stack<Krawedz> dajPlan(Wezel obecnyWezel) {
        Stack<Krawedz> plan = new Stack<>();

        Queue<Krawedz> kolejkaBfs = new ArrayDeque<>();
        Map<Krawedz, Krawedz> poprzednieKrawedzie = new HashMap<>();
        OdwiedzajacyNajlepszaKrawedz odwiedzajacy = new OdwiedzajacyNajlepszaKrawedz(sportowiec);

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

        return plan;
    }
}
