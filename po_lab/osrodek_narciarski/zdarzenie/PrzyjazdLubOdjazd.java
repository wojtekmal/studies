package zdarzenie;

import połączenie.Połączenie;
import sportowiec.Sportowiec;

public abstract class PrzyjazdLubOdjazd extends Zdarzenie
{
    protected Sportowiec sportowiec;
    protected Połączenie połączenie;

    public PrzyjazdLubOdjazd(Godzina godzina, Sportowiec sportowiec,
        Połączenie połączenie)
    {
        super(godzina);
        this.sportowiec = sportowiec;
        this.połączenie = połączenie;
    }

    protected abstract String format();

    protected void zaraportuj()
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
