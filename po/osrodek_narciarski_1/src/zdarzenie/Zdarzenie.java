package zdarzenie;

public abstract class Zdarzenie
{
    protected Godzina godzina;

    public Zdarzenie(Godzina godzina)
    {
        this.godzina = godzina;
    }

    public boolean jestPrzed(Zdarzenie inne)
    {
        return godzina.jestPrzed(inne.godzina);
    }

    // Potrzebne aby zdarzenie mogły zaraportować coś bez wydarzenia się jeśli
    // jest po czasie.
    public void zaraportuj() {}

    public abstract void wydarzSię();
}
