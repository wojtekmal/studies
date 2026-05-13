package zdarzenie;

import połączenie.Trasa;
import sportowiec.Sportowiec;

public class OdjazdTrasą extends EtapPodróży
{
    public OdjazdTrasą(Godzina godzina, Sportowiec sportowiec,
        Trasa trasa)
    {
        super(godzina, sportowiec, trasa);
    }

    public void poinformuj() {}

    public String format()
    {
        return "%s: Sportowiec %d rozpoczął zjazd trasą %d.";
    }    
}
