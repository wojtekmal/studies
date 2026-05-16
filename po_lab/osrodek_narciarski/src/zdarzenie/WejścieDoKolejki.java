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
}
