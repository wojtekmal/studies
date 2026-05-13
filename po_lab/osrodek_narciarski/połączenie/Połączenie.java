package połączenie;

import węzeł.Węzeł;

abstract public class Połączenie
{
    protected Węzeł początkowy;
    protected Węzeł końcowy;
    protected int id;

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
