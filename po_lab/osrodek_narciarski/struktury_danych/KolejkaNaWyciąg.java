package struktury_danych;

import sportowiec.Sportowiec;

public class KolejkaNaWyciąg
{
    private KolejkaNaWyciągWęzeł pierwszy;
    private KolejkaNaWyciągWęzeł ostatni;

    public KolejkaNaWyciąg()
    {
        this.pierwszy = null;
        this.ostatni = null;
    }

    public boolean jestPusta()
    {
        return pierwszy == null;
    }

    public Sportowiec dajKolejnego()
    {
        Sportowiec sportowiec = pierwszy.sportowiec();

        pierwszy = pierwszy.następny();
        pierwszy.ustawPoprzedni(null);

        return sportowiec;
    }
}
