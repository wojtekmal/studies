package zdarzenie;

public interface KolejkaZdarzeń
{
    public void dodajZdarzenie(Zdarzenie zdarzenie);

    public Zdarzenie kolejneZdarzenie();

    public boolean brakZdarzeń();
}
