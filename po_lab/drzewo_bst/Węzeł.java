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

        return this;
    }

    @Override
    public WęzełAbstrakcyjny<T> znajdź(T klucz)
    {
        int wynikPorównania = klucz.compareTo(this.klucz);

        if (wynikPorównania < 0)
        {
            return lewy.znajdź(klucz);
        }
        else if (wynikPorównania == 0)
        {
            return this;
        }
        else
        {
            return prawy.znajdź(klucz);
        }
    }

    @Override
    public int rozmiar()
    {
        return lewy.rozmiar() + 1 + prawy.rozmiar();
    }

    @Override
    public int wysokość()
    {
        return Math.max(lewy.wysokość(), prawy.wysokość()) + 1;
    }

    @Override
    public Dodawalny<T> suma(Class<T> typ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        return lewy.suma(typ).dodaj(klucz).dodaj(prawy.suma(typ));
    }

    @Override
    public void drukujWLinii()
    {
        lewy.drukujWLinii();
        System.out.print(" " + klucz + " ");
        prawy.drukujWLinii();
    }

    @Override
    public void drukujDrzewo(int wcięcie)
    {
        prawy.drukujDrzewo(wcięcie + 4);
        System.out.println(" ".repeat(wcięcie) + klucz);
        lewy.drukujDrzewo(wcięcie + 4);
    }

    @Override
    public T klucz() {
        return klucz;
    }
}
