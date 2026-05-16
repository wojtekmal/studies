package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class OdjazdWyciągiem extends EtapPodróży
{
    private Wyciąg wyciąg;

    public OdjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec);
        this.wyciąg = wyciąg;
    }

    public void poinformuj() {}

    public String format()
    {
        return "%s: Sportowiec %d rozpoczął wjazd wyciągiem " + wyciąg.id() +
            ".";
    }
}
