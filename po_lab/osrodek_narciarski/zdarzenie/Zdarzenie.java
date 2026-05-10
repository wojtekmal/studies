package zdarzenie;

public abstract class Zdarzenie
{
    private Godzina godzina;

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
}
