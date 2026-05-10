package zdarzenie;

public class Zdarzenie
{
    private Godzina godzina;
    private Zdarzenie następneWKolejce;

    public Zdarzenie(Godzina godzina)
    {
        this.godzina = godzina;
    }

    public void ustawNastępneWKolejce(Zdarzenie następneWKolejce)
    {
        this.następneWKolejce = następneWKolejce;
    }

    public Zdarzenie następneWKolejce()
    {
        return następneWKolejce;
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
