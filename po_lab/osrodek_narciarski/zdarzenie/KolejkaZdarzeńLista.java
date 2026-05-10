package zdarzenie;

public class KolejkaZdarzeńLista implements KolejkaZdarzeń
{
    public class WyjątekBrakZdarzeń extends Exception{};

    private Zdarzenie kolejneZdarzenie;

    public KolejkaZdarzeńLista()
    {
        kolejneZdarzenie = null;
    }

    public Zdarzenie kolejneZdarzenie()
    {
        if (kolejneZdarzenie == null)
        {
            throw new WyjątekBrakZdarzeń();
        }

        Zdarzenie wynik = kolejneZdarzenie;
        kolejneZdarzenie = kolejneZdarzenie.następneWKolejce();

        return wynik;
    }

    public boolean brakZdarzeń()
    {
        return kolejneZdarzenie == null;
    }
}
