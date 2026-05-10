package połączenie;

import węzeł.Węzeł;

abstract public class Połączenie
{
    private Węzeł początkowy;
    private Węzeł końcowy;

    public Połączenie(Węzeł początkowy, Węzeł końcowy)
    {
        this.początkowy = początkowy;
        this.końcowy = końcowy;
    }
}
