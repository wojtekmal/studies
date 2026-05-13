package zdarzenie;

public abstract class Zdarzenie
{
    protected Godzina godzina;

    public Zdarzenie(Godzina godzina)
    {
        this.godzina = godzina;
    }

    public Godzina godzina()
    {
        return godzina;
    }

    public boolean jestPrzed(Zdarzenie inne)
    {
        return godzina.jestPrzed(inne.godzina());
    }

    protected abstract void poinformuj();

    protected abstract void zaraportuj();

    public void wydarzSię()
    {
        poinformuj();
        zaraportuj();
    }
}
