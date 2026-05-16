package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class WejścieDoKolejki extends EtapPodróży
{
    private Wyciąg wyciąg;

    public WejścieDoKolejki(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec);
        this.wyciąg = wyciąg;
    }

    protected void poinformuj()
    {
        wyciąg.dodajDoKolejki(sportowiec);
    }

    protected String format()
    {
        return "%s: Sportowiec %d ustawił się w kolejce pod wyciągiem " +
            wyciąg.id() + ".";
    }

    @Override
    public boolean equals(Object obiekt)
    {
        if (obiekt instanceof WejścieDoKolejki)
        {
            WejścieDoKolejki inny = (WejścieDoKolejki) obiekt;
            return inny.godzina.equals(godzina) && inny.sportowiec == sportowiec
                && inny.wyciąg == wyciąg;
        }

        return false;
    }
}
