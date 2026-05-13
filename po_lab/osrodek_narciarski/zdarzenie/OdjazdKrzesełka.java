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

    public void poinformuj()
    {
        wyciąg.przyjmijZKolejki(godzina);
    }

    public void zaraportuj() {}
}
