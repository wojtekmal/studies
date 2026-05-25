import java.lang.reflect.InvocationTargetException;

public class Węzeł<T extends Comparable<T> & Dodawalny<T>> extends WęzełAbstrakcyjny<T>
{
    private T klucz;
    private WęzełAbstrakcyjny<T> lewy;
    private WęzełAbstrakcyjny<T> prawy;

    public Węzeł(T klucz)
    {
        this.klucz = klucz;
    }

    @Override
    public WęzełAbstrakcyjny<T> wstaw(T klucz)
    {
        if (klucz.compareTo(this.klucz) <= 0)
        {
            lewy = lewy.wstaw(klucz);
        }
        else
        {
            prawy = prawy.wstaw(klucz);
        }
    }

    @Override
    public WęzełAbstrakcyjny<T> znajdź(T klucz)
    {
        return null;
    }

    @Override
    public int rozmiar()
    {
        return 0;
    }

    @Override
    public int wysokość()
    {
        return 0;
    }

    @Override
    public T suma(Class<T> typ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        return null;
    }

    @Override
    public void drukujWLinii()
    {

    }

    @Override
    public void drukujDrzewo(int wcięcie, int przesunięcie) {

    }

    @Override
    public T klucz() {
        return null;
    }
}
