package osrodek.krawedz;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import losowosc.DeterministycznaMaszynaLosujaca;
import losowosc.MaszynaLosujaca;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import osrodek.Wezel;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.ParametrySportowca;
import sportowcy.krotkowzroczny.SportowiecKrotkowzroczny;

public class WyciagTesty {

    private static final Wezel wezelPomocniczy1 = new Wezel(0, 0, 0, 0, false);
    private static final Wezel wezelPomocniczy2 = new Wezel(0, 1, 0, 0, false);
    private static final Interwal interwalPomocniczy = new Interwal(0);
    private static final Moment momentPomocniczy = new Moment(0, 0, 0);
    private static final MaszynaLosujaca maszynaLosujacaPomocnicza = new DeterministycznaMaszynaLosujaca(0);
    private static final ParametrySportowca parametrySportowcaPomocnicze = new ParametrySportowca(0, 0, 0, 0, 0, 0, 0, false, wezelPomocniczy1, momentPomocniczy, maszynaLosujacaPomocnicza);
    private static final Dziennik dziennikPomocniczy = new DziennikDevNull();

    @Test
    public void sportowiecSieNieMiesciTest() {
        Wyciag wyciag = new Wyciag(0,
            wezelPomocniczy1,
            wezelPomocniczy2,
            interwalPomocniczy,
            interwalPomocniczy,
            3);

        for (int i = 0; i < 4; i++) {
            wyciag.dodajDoKolejki(new SportowiecKrotkowzroczny(parametrySportowcaPomocnicze));
        }

        wyciag.odjazd(momentPomocniczy, dziennikPomocniczy);

        assertEquals(3, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void niepelneKrzeselkoTest() {
        Wyciag wyciag = new Wyciag(0,
            wezelPomocniczy1,
            wezelPomocniczy2,
            interwalPomocniczy,
            interwalPomocniczy,
            3);

        for (int i = 0; i < 2; i++) {
            wyciag.dodajDoKolejki(new SportowiecKrotkowzroczny(parametrySportowcaPomocnicze));
        }

        wyciag.odjazd(momentPomocniczy, dziennikPomocniczy);

        assertEquals(2, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void maksymalnaDlugoscKolejkiTest() {
        Wyciag wyciag = new Wyciag(0,
            wezelPomocniczy1,
            wezelPomocniczy2,
            interwalPomocniczy,
            interwalPomocniczy,
            2);

        for (int i = 0; i < 4; i++) {
            wyciag.dodajDoKolejki(new SportowiecKrotkowzroczny(parametrySportowcaPomocnicze));
        }
        wyciag.zaktualizujStatystyki(momentPomocniczy);

        wyciag.odjazd(momentPomocniczy, dziennikPomocniczy);

        wyciag.dodajDoKolejki(new SportowiecKrotkowzroczny(parametrySportowcaPomocnicze));
        wyciag.zaktualizujStatystyki(momentPomocniczy);

        assertEquals(4, wyciag.maksymalnaDlugoscKolejki());
    }
}
