import java.lang.reflect.InvocationTargetException;

public class Wartownik<T extends Comparable<T> & Dodawalny<T>> extends WęzełAbstrakcyjny<T>
{
    public void drukujDrzewo(int rozmiarWcięcia, int przesunięcie) {}

    public void drukujWLinii() {}

    public int rozmiar()
    {
        return 0;
    }

    @Override
    public int wysokość() {
        return 0;
    }

    public WęzełAbstrakcyjny<T> wstaw(T klucz, Wartownik<T> wartownik)
    {
        return new Węzeł<T>(klucz, wartownik);
    }

    public T klucz()
    {
        return null;
    }

    public Węzeł<T> znajdź(T klucz)
    {
        return null;
    }

    public T suma(Class<T> typ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        return typ.getDeclaredConstructor().newInstance();
    }
}
