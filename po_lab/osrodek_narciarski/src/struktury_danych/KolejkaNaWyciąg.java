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

        if (pierwszy == null)
        {
            ostatni = null;
        }
        else
        {
            pierwszy.ustawPoprzedni(null);
        }

        return sportowiec;
    }

    public void dodaj(Sportowiec sportowiec)
    {
        KolejkaNaWyciągWęzeł nowy = new KolejkaNaWyciągWęzeł(sportowiec);
        nowy.ustawNastępny(null);
        nowy.ustawPoprzedni(ostatni);

        if (jestPusta())
        {
            pierwszy = nowy;
        }

        ostatni = nowy;
    }
}
