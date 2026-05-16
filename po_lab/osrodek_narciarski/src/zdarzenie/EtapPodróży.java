package zdarzenie;

import połączenie.Połączenie;
import sportowiec.Sportowiec;

public abstract class EtapPodróży extends Zdarzenie
{
    protected Sportowiec sportowiec;
    protected Połączenie połączenie;

    public EtapPodróży(Godzina godzina, Sportowiec sportowiec,
        Połączenie połączenie)
    {
        super(godzina);
        this.sportowiec = sportowiec;
        this.połączenie = połączenie;
    }

    protected abstract String format();

    public void zaraportuj()
    {
        if (sportowiec.czyŚledzić())
        {
            String output = String.format(
                format(), godzina.toString(), sportowiec.id(), połączenie.id());
            
            System.out.println(output);
        }
    }

    protected abstract void poinformuj();

    public void wydarzSię()
    {
        poinformuj();
        zaraportuj();
    }
}
