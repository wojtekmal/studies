import java.lang.reflect.InvocationTargetException;

public class DrzewoBST<T extends Comparable<T> & Dodawalny<T>>
{
    private Wartownik<T> wartownik;
    private WęzełAbstrakcyjny<T> korzeń;

    public DrzewoBST()
    {
        wartownik = new Wartownik<T>();
        korzeń = wartownik;
    }

    public void wstaw(T nowy)
    {
        korzeń = korzeń.wstaw(nowy, wartownik);
    }

    public Węzeł<T> znajdź(T klucz)
    {
        return korzeń.znajdź(klucz);
    }

    public int rozmiar()
    {
        return korzeń.rozmiar();
    }

    public int wysokość()
    {
        return korzeń.wysokość();
    }

    public T suma(Class<T> typ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        return korzeń.suma(typ);
    }

    public void drukujWLinii()
    {
        korzeń.drukujWLinii();
        System.out.println();
    }

    public void drukujDrzewo(int rozmiarWcięcia)
    {
        korzeń.drukujDrzewo(rozmiarWcięcia, 0);
    }
}
