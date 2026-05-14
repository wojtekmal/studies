package struktury_danych;

import zdarzenie.Zdarzenie;

public interface KolejkaZdarzeń
{
    public class BrakZdarzeń extends Exception{};

    public void dodajZdarzenie(Zdarzenie zdarzenie);

    public Zdarzenie dajKolejne() throws BrakZdarzeń;

    public boolean jestPusta();
}
