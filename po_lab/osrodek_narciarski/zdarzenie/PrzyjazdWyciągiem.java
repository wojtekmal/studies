package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class PrzyjazdWyciągiem extends PrzyjazdLubOdjazd
{
    public PrzyjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina, sportowiec, wyciąg);
    }

    public void poinformuj()
    {
        sportowiec.przyjedźDo(połączenie.końcowy());
    }

    public String format()
    {
        return "%s: Sportowiec %d zakończył wjazd wyciągiem %d.";
    }
}
