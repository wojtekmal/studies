package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class PrzyjazdLubOdjazd extends Zdarzenie
{
    private Sportowiec sportowiec;
    private Wyciąg wyciąg;

    public PrzyjazdLubOdjazd(Sportowiec sportowiec, Wyciąg wyciąg)
    {
        this.sportowiec = sportowiec;
        this.wyciąg = wyciąg;
    }
}
