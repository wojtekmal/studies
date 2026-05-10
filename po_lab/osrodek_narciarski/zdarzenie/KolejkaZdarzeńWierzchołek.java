package zdarzenie;

public class KolejkaZdarzeńWierzchołek
{
    private Zdarzenie zdarzenie;
    private KolejkaZdarzeńWierzchołek następny;

    public KolejkaZdarzeńWierzchołek(Zdarzenie zdarzenie)
    {
        this.zdarzenie = zdarzenie;
    }

    public Zdarzenie zdarzenie()
    {
        return zdarzenie;
    }

    public KolejkaZdarzeńWierzchołek następny()
    {
        return następny;
    }

    public void ustawNastępny(KolejkaZdarzeńWierzchołek następny)
    {
        this.następny = następny;
    }
}
