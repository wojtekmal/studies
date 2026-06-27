package sportowcy.przeszukujacy;

import czas.Interwal;
import czas.Moment;
import losowosc.DeterministycznaMaszynaLosujaca;
import losowosc.MaszynaLosujaca;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.ParametrySportowca;

import java.util.*;

public class BfsTesty {

    private static final Wezel wezelPomocniczy1 = new Wezel(0, 0, 0, 0, false);
    private static final Wezel wezelPomocniczy2 = new Wezel(0, 1, 0, 0, false);
    private static final Moment momentPomocniczy = new Moment(0, 0, 0);
    private static final MaszynaLosujaca maszynaLosujacaPomocnicza = new DeterministycznaMaszynaLosujaca(0);
    private final Interwal interwalPomocniczy = new Interwal(0);
    private static final ParametrySportowca parametrySportowcaPomocnicze = new ParametrySportowca(0, 0, 0, 0, 0, 0, 0, false, wezelPomocniczy1, momentPomocniczy, maszynaLosujacaPomocnicza);

    private final Wezel[] wezly = {
        new Wezel(0, 0, 0, 0, false),
        new Wezel(1, 2, 0, 0, false),
        new Wezel(2, 1, 0, 0, false),
        new Wezel(3, 4, 0, 0, false),
        new Wezel(4, 3, 0, 0, false),
        new Wezel(5, 5, 0, 0, false),
    };

    private final Trasa[] trasy = {
        new Trasa(0, wezly[5], wezly[3], interwalPomocniczy, 0, 0, 0),
        new Trasa(1, wezly[5], wezly[3], interwalPomocniczy, 0, 0, 0),
        new Trasa(2, wezly[3], wezly[4], interwalPomocniczy, 0, 0, 0),
        new Trasa(3, wezly[3], wezly[1], interwalPomocniczy, 0, 0, 0),
        new Trasa(4, wezly[1], wezly[2], interwalPomocniczy, 0, 0, 0),
        new Trasa(5, wezly[1], wezly[0], interwalPomocniczy, 0, 0, 0),
        new Trasa(6, wezly[2], wezly[0], interwalPomocniczy, 0, 0, 0),

        // Potrzebna są trasy pomocnicze od niektórych węzłów, ze względu na
        // implementację bfs; algorytm szuka krawędzi, nie wierzchołka.
        new Trasa(7, wezly[4], wezelPomocniczy1, interwalPomocniczy, 0, 0, 0),
    };

    private final Wyciag[] wyciagi = {
        new Wyciag(0, wezly[0], wezly[1], interwalPomocniczy, interwalPomocniczy, 0),
        new Wyciag(1, wezly[2], wezly[3], interwalPomocniczy, interwalPomocniczy, 0),
        new Wyciag(2, wezly[2], wezly[4], interwalPomocniczy, interwalPomocniczy, 0),
        new Wyciag(3, wezly[4], wezly[5], interwalPomocniczy, interwalPomocniczy, 0),
    };

    public BfsTesty() {
        wezly[5].wychodzaceTrasy(new Trasa[]{trasy[0], trasy[1]});
        wezly[3].wychodzaceTrasy(new Trasa[]{trasy[2], trasy[3]});
        wezly[1].wychodzaceTrasy(new Trasa[]{trasy[4], trasy[5]});
        wezly[2].wychodzaceTrasy(new Trasa[]{trasy[6]});
        wezly[4].wychodzaceTrasy(new Trasa[]{trasy[7]});

        wezly[0].wychodzaceWyciagi(new Wyciag[]{wyciagi[0]});
        wezly[2].wychodzaceWyciagi(new Wyciag[]{wyciagi[1], wyciagi[2]});
        wezly[4].wychodzaceWyciagi(new Wyciag[]{wyciagi[3]});
    }

    private Bfs bfsDoWezla(Wezel wezelDocelowy)
    {
        SportowiecPrzeszukujacy sportowiec = new SportowiecUwielbiajacyKonkretnyWezel(parametrySportowcaPomocnicze, wezelDocelowy);
        return new Bfs(sportowiec);
    }

    @Test
    public void dlugaSciezkaTest() {
        Bfs bfs = bfsDoWezla(wezly[4]);
        Stack<Krawedz> plan = bfs.dajPlan(wezly[0]);

        assertEquals(wyciagi[0], plan.pop());
        assertEquals(trasy[4], plan.pop());
        assertEquals(wyciagi[2], plan.pop());

        // Upewniamy się, że została tylko ta krawędź, która wychodzi z
        // docelowego węzła (czyli krawędź, której szuka sportowiec).
        assertEquals(1, plan.size());
    }

    @Test
    public void pojedynczaTrasaTest() {
        Bfs bfs = bfsDoWezla(wezly[1]);
        Stack<Krawedz> plan = bfs.dajPlan(wezly[3]);

        assertEquals(trasy[3], plan.pop());

        assertEquals(1, plan.size());
    }

    @Test
    public void pustaSciezkaTest() {
        Bfs bfs = bfsDoWezla(wezly[2]);
        Stack<Krawedz> plan = bfs.dajPlan(wezly[2]);

        assertEquals(1, plan.size());
    }

    @Test
    public void dwieMozliwosciTest() {
        Bfs bfs = bfsDoWezla(wezly[3]);
        Stack<Krawedz> plan = bfs.dajPlan(wezly[4]);

        assertEquals(wyciagi[3], plan.pop());

        Set<Krawedz> dozwoloneOstatnieKrawedzie = new HashSet<>();
        dozwoloneOstatnieKrawedzie.add(trasy[0]);
        dozwoloneOstatnieKrawedzie.add(trasy[1]);
        assertTrue(dozwoloneOstatnieKrawedzie.contains(plan.pop()));

        assertEquals(1, plan.size());
    }
}
