import java.lang.reflect.InvocationTargetException;

public abstract class WęzełAbstrakcyjny<T extends Comparable<T> & Dodawalny<T>>
{
    public abstract WęzełAbstrakcyjny<T> wstaw(T klucz, Wartownik<T> wartownik);

    public abstract Węzeł<T> znajdź(T klucz);

    public abstract int rozmiar();

    public abstract int wysokość();

    public abstract T suma(Class<T> typ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    public abstract void drukujWLinii();

    public abstract void drukujDrzewo(int rozmiarWcięcia, int przesunięcie);

    public abstract T klucz();
}
