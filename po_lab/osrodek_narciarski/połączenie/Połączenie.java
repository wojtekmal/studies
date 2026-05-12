package połączenie;

import węzeł.Węzeł;

abstract public class Połączenie
{
    private Węzeł początkowy;
    private Węzeł końcowy;
    private int id;

    public Połączenie(Węzeł początkowy, Węzeł końcowy, int id)
    {
        this.początkowy = początkowy;
        this.końcowy = końcowy;
        this.id = id;
    }

    public int id()
    {
        return id;
    }

    public Węzeł końcowy()
    {
        return końcowy;
    }
}
