package struktury_danych;

import zdarzenie.Zdarzenie;

public class KolejkaZdarzeńLista implements KolejkaZdarzeń
{
    private KolejkaZdarzeńWierzchołek pierwszyWierzchołek;

    public KolejkaZdarzeńLista()
    {
        pierwszyWierzchołek = null;
    }

    public Zdarzenie dajKolejne() throws BrakZdarzeń
    {
        if (pierwszyWierzchołek == null)
        {
            throw new BrakZdarzeń();
        }

        Zdarzenie wynik = pierwszyWierzchołek.zdarzenie();
        pierwszyWierzchołek = pierwszyWierzchołek.następny();

        return wynik;
    }

    public boolean jestPusta()
    {
        return pierwszyWierzchołek == null;
    }

    public void dodajZdarzenie(Zdarzenie zdarzenie)
    {
        KolejkaZdarzeńWierzchołek wierzchołek = new
            KolejkaZdarzeńWierzchołek(zdarzenie);
        
        // Jeśli zdarzenia są w tej samej chwili, to nowe zdarzenie,
        // które się przyrównuje do innych, ma być później.
        
        if (zdarzenie.jestPrzed(pierwszyWierzchołek.zdarzenie()))
        {
            wierzchołek.ustawNastępny(pierwszyWierzchołek);
            pierwszyWierzchołek = wierzchołek;
        }
        else
        {
            KolejkaZdarzeńWierzchołek poprzedni = pierwszyWierzchołek;

            while (poprzedni.następny() != null && 
                !zdarzenie.jestPrzed(poprzedni.następny().zdarzenie()))
            {
                poprzedni = poprzedni.następny();
            }

            wierzchołek.ustawNastępny(poprzedni.następny());
            poprzedni.ustawNastępny(wierzchołek);
        }
    }
}
