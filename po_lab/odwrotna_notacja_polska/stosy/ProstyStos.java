package stosy;

/**
 * Prosta implementacja stosu.
 * @author Wojciech Malinowski
 * @version 0
 */
public class ProstyStos implements IStos{

    private int[] dane;
    private int ile;

    public ProstyStos(int rozmiar)
    {
        dane = new int[rozmiar];
        ile = 0;
    }

    @Override
    public void push(int x) throws PełnyStos {
        if (ile == dane.length) throw new PełnyStos();
        
        dane[ile++] = x;
    }

    @Override
    public int pop() throws PustyStos {
        if (empty()) throw new PustyStos();

        return dane[ile--];
    }

    @Override
    public boolean empty() {
        return ile == 0;
    }
}