package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class PrzyjazdWyciągiem extends EtapPodróży
{
    private Wyciąg wyciąg;

    public PrzyjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec);
        this.wyciąg = wyciąg;
    }

    public void poinformuj()
    {
        sportowiec.przyjedźDo(wyciąg.końcowy(), godzina);
    }

    public String format()
    {
        return "%s: Sportowiec %d zakończył wjazd wyciągiem " + wyciąg.id() +
            ".";
    }
}
