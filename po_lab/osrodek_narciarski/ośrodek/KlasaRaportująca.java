package ośrodek;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;
import zdarzenie.Godzina;

public class KlasaRaportująca
{
    static public void przyjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        String output = String.format(
            "%s: Sportowiec %d zakończył wjazd wyciągiem %d.",
            godzina.toString(), sportowiec.id(), wyciąg.id());
        
        System.out.println(output);
    }

    static public void odjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        String output = String.format(
            "%s: Sportowiec %d zakończył wjazd wyciągiem %d.",
            godzina.toString(), sportowiec.id(), wyciąg.id());
        
        System.out.println(output);
    }
}
