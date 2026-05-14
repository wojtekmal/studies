package połączenie;

import sportowiec.Sportowiec;
import struktury_danych.KolejkaZdarzeń;
import węzeł.Węzeł;
import zdarzenie.Godzina;

abstract public class Połączenie
{
    protected Węzeł początkowy;
    protected Węzeł końcowy;
    protected int id;

    protected KolejkaZdarzeń kolejkaZdarzeń;
    protected int liczbaPrzejazdów;

    public Połączenie(Węzeł początkowy, Węzeł końcowy,
        KolejkaZdarzeń kolejkaZdarzeń, int id)
    {
        this.początkowy = początkowy;
        this.końcowy = końcowy;
        this.id = id;
        this.kolejkaZdarzeń = kolejkaZdarzeń;
    }

    public int id()
    {
        return id;
    }

    public Węzeł końcowy()
    {
        return końcowy;
    }

    public abstract void wybierzPołączenie(Godzina godzina,
        Sportowiec sportowiec);

    public void dodajDoKolejki(Sportowiec sportowiec)
    {
        // Co prawda trasy nie mają kolejek, ale dzięki temu odstępstwu
        // hierarchia zdarzeń jest prostsza.
    }

    public abstract void wypiszStatystyki();
}
