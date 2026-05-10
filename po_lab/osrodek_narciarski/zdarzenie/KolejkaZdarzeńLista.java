package zdarzenie;

public class KolejkaZdarzeńLista implements KolejkaZdarzeń
{
    private Zdarzenie kolejneZdarzenie;

    public KolejkaZdarzeńLista()
    {
        kolejneZdarzenie = null;
    }

    public Zdarzenie kolejneZdarzenie() throws BrakZdarzeń
    {
        if (kolejneZdarzenie == null)
        {
            throw new BrakZdarzeń();
        }

        Zdarzenie wynik = kolejneZdarzenie;
        kolejneZdarzenie = kolejneZdarzenie.następneWKolejce();

        return wynik;
    }

    public boolean brakZdarzeń()
    {
        return kolejneZdarzenie == null;
    }

    public void dodajZdarzenie(Zdarzenie zdarzenie)
    {
        
    }
}
