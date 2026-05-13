package zdarzenie;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class PrzyjazdWyciągiem extends Zdarzenie
{
    public PrzyjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina);

        this.sportowiec = sportowiec;
        this.wyciąg = wyciąg;
    }

    public void poinformuj()
    {
        sportowiec.przyjedźDo(wyciąg.końcowy());
    }

    protected void zaraportuj()
    {
        String output = String.format(
            "%s: Sportowiec %d zakończył wjazd wyciągiem %d.",
            godzina.toString(), sportowiec.id(), wyciąg.id());
        
        System.out.println(output);
    }
}
