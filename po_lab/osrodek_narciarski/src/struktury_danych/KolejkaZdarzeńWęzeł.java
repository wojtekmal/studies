package struktury_danych;

import zdarzenie.Zdarzenie;

public class KolejkaZdarzeńWęzeł
{
    private Zdarzenie zdarzenie;
    private KolejkaZdarzeńWęzeł następny;

    public KolejkaZdarzeńWęzeł(Zdarzenie zdarzenie)
    {
        this.zdarzenie = zdarzenie;
    }

    public Zdarzenie zdarzenie()
    {
        return zdarzenie;
    }

    public KolejkaZdarzeńWęzeł następny()
    {
        return następny;
    }

    public void ustawNastępny(KolejkaZdarzeńWęzeł następny)
    {
        this.następny = następny;
    }
}
