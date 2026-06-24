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

    @Override
    public boolean equals(Object obiekt)
    {
        if (obiekt instanceof PrzyjazdWyciągiem)
        {
            PrzyjazdWyciągiem inny = (PrzyjazdWyciągiem) obiekt;
            return inny.godzina.equals(godzina) && inny.sportowiec == sportowiec &&
                inny.wyciąg == wyciąg;
        }

        return false;
    }
}
