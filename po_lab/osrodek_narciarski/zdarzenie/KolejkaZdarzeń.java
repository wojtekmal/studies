package zdarzenie;

public interface KolejkaZdarzeń
{
    public class BrakZdarzeń extends Exception{};

    public void dodajZdarzenie(Zdarzenie zdarzenie);

    public Zdarzenie kolejneZdarzenie() throws BrakZdarzeń;

    public boolean brakZdarzeń();
}
