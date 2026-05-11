package struktury_danych;

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

    public dajKolejnego()
    {
        Sportowiec sportowiec = pierwszy.sportowiec();
    }
}
