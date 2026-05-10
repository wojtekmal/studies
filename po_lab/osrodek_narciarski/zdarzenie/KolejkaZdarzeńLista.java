package zdarzenie;

public class KolejkaZdarzeńLista implements KolejkaZdarzeń
{
    private KolejkaZdarzeńWierzchołek pierwszyWierzchołek;

    public KolejkaZdarzeńLista()
    {
        pierwszyWierzchołek = null;
    }

    public Zdarzenie kolejneZdarzenie() throws BrakZdarzeń
    {
        if (pierwszyWierzchołek == null)
        {
            throw new BrakZdarzeń();
        }

        Zdarzenie wynik = pierwszyWierzchołek.zdarzenie();
        pierwszyWierzchołek = pierwszyWierzchołek.następny();

        return wynik;
    }

    public boolean brakZdarzeń()
    {
        return pierwszyWierzchołek == null;
    }

    public void dodajZdarzenie(Zdarzenie zdarzenie)
    {
        KolejkaZdarzeńWierzchołek wierzchołek = new
            KolejkaZdarzeńWierzchołek(zdarzenie);
        
        if (zdarzenie.jestPrzed(pierwszyWierzchołek.zdarzenie()))
        {
            wierzchołek.ustawNastępny(pierwszyWierzchołek);
            pierwszyWierzchołek = wierzchołek;
        }
        else
        {
            KolejkaZdarzeńWierzchołek poprzedni = pierwszyWierzchołek;

            while (poprzedni.następny() != null && 
                !zdarzenie.jestPrzed(poprzedni.zdarzenie()))
            {
                poprzedni = poprzedni.następny();
            }

            wierzchołek.ustawNastępny(poprzedni.następny());
            poprzedni.ustawNastępny(wierzchołek);
        }
    }
}
