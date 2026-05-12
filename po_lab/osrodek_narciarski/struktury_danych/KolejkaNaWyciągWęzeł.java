package struktury_danych;

import sportowiec.Sportowiec;

public class KolejkaNaWyciągWęzeł
{
    private Sportowiec sportowiec;
    private KolejkaNaWyciągWęzeł poprzedni;
    private KolejkaNaWyciągWęzeł następny;

    public KolejkaNaWyciągWęzeł(Sportowiec sportowiec,
        KolejkaNaWyciągWęzeł poprzedni, KolejkaNaWyciągWęzeł następny)
    {
        this.sportowiec = sportowiec;
        this.poprzedni = poprzedni;
        this.następny = następny;
    }

    public Sportowiec sportowiec()
    {
        return sportowiec;
    }
    
    public KolejkaNaWyciągWęzeł poprzedni()
    {
        return poprzedni;
    }

    public KolejkaNaWyciągWęzeł następny()
    {
        return następny;
    }

    public void ustawPoprzedni(KolejkaNaWyciągWęzeł poprzedni)
    {
        this.poprzedni = poprzedni;
    }

    public void ustawNastępny(KolejkaNaWyciągWęzeł następny)
    {
        this.następny = następny;
    }
}
