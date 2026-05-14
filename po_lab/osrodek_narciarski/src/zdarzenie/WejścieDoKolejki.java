package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class WejścieDoKolejki extends EtapPodróży
{
    public WejścieDoKolejki(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec, wyciąg);
    }

    protected void poinformuj()
    {
        połączenie.dodajDoKolejki(sportowiec);
    }

    protected String format()
    {
        return "%s: Sportowiec %d ustawił się w kolejce pod wyciągiem %d.";
    }
}
