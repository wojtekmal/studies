package zdarzenie;

import sportowiec.Sportowiec;

public abstract class EtapPodróży extends Zdarzenie
{
    protected Sportowiec sportowiec;

    public EtapPodróży(Godzina godzina, Sportowiec sportowiec)
    {
        super(godzina);
        this.sportowiec = sportowiec;
    }

    protected abstract String format();

    public void zaraportuj()
    {
        if (sportowiec.czyŚledzić())
        {
            String output = String.format(format(), godzina.toString(),
                sportowiec.id());
            
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
