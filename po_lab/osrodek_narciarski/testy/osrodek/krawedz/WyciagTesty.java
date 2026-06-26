package osrodek.krawedz;

import czas.Interwal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import osrodek.Wezel;
import osrodek.krawedz.wyciag.Wyciag;

public class WyciagTesty {

    private static final Wezel wezelPomocniczy = new Wezel(0, 0, 0, 0, false);
    private static final Interwal interwalPomocniczy = new Interwal(0);

    @Test
    public void sportowiecSieNieMiesciTest() {
        Wyciag wyciag = new Wyciag(0,
            wezelPomocniczy,
            wezelPomocniczy,
            interwalPomocniczy,
            interwalPomocniczy,
            3);

        assertEquals(wyciag.lacznaLiczbaPasazerow(), 3);
    }
}
