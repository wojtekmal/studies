public abstract class Węzeł<T>
{
    public abstract Węzeł<T> wstaw(T klucz);

    public abstract Węzeł<T> znajdź(T klucz);

    public abstract int rozmiar();

    public abstract int wysokość();
}
