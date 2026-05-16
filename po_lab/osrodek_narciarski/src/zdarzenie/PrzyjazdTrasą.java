package zdarzenie;

import połączenie.Trasa;
import sportowiec.Sportowiec;

public class PrzyjazdTrasą extends EtapPodróży
{
    private Trasa trasa;

    public PrzyjazdTrasą(Godzina godzina, Sportowiec sportowiec,
        Trasa trasa)
    {
        super(godzina, sportowiec);
        this.trasa = trasa;
    }

    public void poinformuj()
    {
        sportowiec.przyjedźDo(trasa.końcowy(), godzina);
    }

    public String format()
    {
        return "%s: Sportowiec %d zakończył zjazd trasą " + trasa.id() + ".";
    }

    @Override
    public boolean equals(Object obiekt)
    {
        if (obiekt instanceof PrzyjazdTrasą)
        {
            PrzyjazdTrasą inny = (PrzyjazdTrasą) obiekt;
            return inny.godzina == godzina && inny.sportowiec == sportowiec &&
                inny.trasa == trasa;
        }

        return false;
    }
}
