package zdarzenie;

import sportowiec.Sportowiec;
import węzeł.Węzeł;

public class PoczątekJazdy extends Zdarzenie
{
    private Sportowiec sportowiec;
    private Węzeł startowy;

    public PoczątekJazdy(Godzina godzina, Sportowiec sportowiec, Węzeł startowy)
    {
        super(godzina);
        this.sportowiec = sportowiec;
        this.startowy = startowy;
    }

    public void wydarzSię()
    {
        sportowiec.przyjedźDo(startowy, godzina);
    }
}
