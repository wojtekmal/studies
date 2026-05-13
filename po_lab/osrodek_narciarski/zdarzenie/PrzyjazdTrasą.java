package zdarzenie;

import połączenie.Trasa;
import sportowiec.Sportowiec;

public class PrzyjazdTrasą extends EtapPodróży
{
    public PrzyjazdTrasą(Godzina godzina, Sportowiec sportowiec,
        Trasa trasa)
    {
        super(godzina, sportowiec, trasa);
    }

    public void poinformuj()
    {
        sportowiec.przyjedźDo(połączenie.końcowy(), godzina);
    }

    public String format()
    {
        return "%s: Sportowiec %d zakończył zjazd trasą %d.";
    }
}
