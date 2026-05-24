public class Wartownik<T extends Comparable<T> & Dodawalny<T>> extends WęzełAbstrakcyjny<T>
{
    public void drukujDrzewo(int wcięcie, int przesunięcie) {}

    public void drukujWLinii() {}

    public int rozmiar()
    {
        return 0;
    }

    public WęzełAbstrakcyjny<T> wstaw(T klucz)
    {
        return new Węzeł<T>(klucz);
    }

    public T klucz()
    {
        return null;
    }

    public WęzełAbstrakcyjny<T> znajdź(T klucz)
    {
        return null;
    }

    public T suma(Class<T> typ)
    {
        return typ.getDeclaredConstructor().newInstance();
    }
}
