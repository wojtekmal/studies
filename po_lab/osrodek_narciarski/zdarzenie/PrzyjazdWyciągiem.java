package zdarzenie;

import ośrodek.KlasaWczytująca;
import połączenie.Wyciąg;
import sportowiec.Sportowiec;

public class PrzyjazdWyciągiem extends Zdarzenie
{
    private Sportowiec sportowiec;
    private Wyciąg wyciąg;

    public PrzyjazdWyciągiem(Godzina godzina, Sportowiec sportowiec,
        Wyciąg wyciąg)
    {
        super(godzina);

        this.sportowiec = sportowiec;
        this.wyciąg = wyciąg;
    }

    public void wydarzSię()
    {
        sportowiec.przyjedźDo(wyciąg.końcowy());
        KlasaRaportująca.przyjazdWyciągiem(godzina, sportowiec, wyciąg);
    }
}
