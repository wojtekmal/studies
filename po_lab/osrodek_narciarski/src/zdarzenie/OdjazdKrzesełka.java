package zdarzenie;

import połączenie.Wyciąg;

public class OdjazdKrzesełka extends Zdarzenie
{
    private Wyciąg wyciąg;

    public OdjazdKrzesełka(Godzina godzina, Wyciąg wyciąg)
    {
        super(godzina);
        this.wyciąg = wyciąg;
    }

    public void wydarzSię()
    {
        wyciąg.przyjmijZKolejki(godzina);
        wyciąg.zaplanujKolejneKrzesełko(godzina);
    }

    @Override
    public boolean equals(Object obiekt)
    {
        if (obiekt instanceof OdjazdKrzesełka)
        {
            OdjazdKrzesełka inny = (OdjazdKrzesełka) obiekt;
            return inny.godzina.equals(godzina) && inny.wyciąg == wyciąg;
        }

        return false;
    }
}
