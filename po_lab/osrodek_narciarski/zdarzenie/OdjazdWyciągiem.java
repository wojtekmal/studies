package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class OdjazdWyciągiem extends PrzyjazdLubOdjazd
{
    public OdjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec, wyciąg);
    }

    public void poinformuj() {}

    public String format()
    {
        return "%s: Sportowiec %d rozpoczął wjazd wyciągiem %d.";
    }
}
