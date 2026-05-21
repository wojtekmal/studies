public abstract class Węzeł<T>
{
    public abstract Węzeł<T> wstaw(T klucz);

    public abstract Węzeł<T> znajdź(T klucz);

    public abstract int rozmiar();

    public abstract int wysokość();

    public abstract T suma(Class<T> typ);

    public abstract void drukujWLinii();

    public abstract void drukujDrzewo(int wcięcie, int przesunięcie);

    public abstract T klucz();
}
