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

    protected void zaraportuj()
    {
        String output = String.format(
            format(), godzina.toString(), sportowiec.id(), połączenie.id());
        
        System.out.println(output);
    }

    protected abstract String format();
}
