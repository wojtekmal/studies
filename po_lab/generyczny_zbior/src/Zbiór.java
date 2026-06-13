import java.util.ArrayList;
import java.util.Collection;

public class Zbiór<E>
{
    private Collection<E> elty;

    public Zbiór()
    {
        elty = new ArrayList<E>();
    }

    public Zbiór(Collection<? extends E> kolekcja)
    {
        this();

        for (E elt : kolekcja)
        {
            wstaw(elt);
        }
    }

    public void wstaw(E elt)
    {
        elty.add(elt);
    }

    public Zbiór(E... parametry)
    {
        this();

        for (E elt : parametry)
        {
            wstaw(elt);
        }
    }

    @Override
    public String toString()
    {
        return elty.toString();
    }

    public boolean czyPusty()
    {
        return elty.isEmpty();
    }

    public boolean czyZawiera(E elt)
    {
        return elty.contains(elt);
    }

    public int rozmiar(E elt)
    {
        return elty.size();
    }

    public void usuń(E elt)
    {
        elty.remove(elt);
    }

    public void suma(Zbiór<? extends E> inny)
    {
        elty.addAll(inny.elty);
    }

    public void różnica(Zbiór<? extends E> inny)
    {
        elty.removeAll(inny.elty);
    }

    public void przecięcie(Zbiór<? extends E> inny)
    {
        ArrayList<E> tmp = new ArrayList<>();

        for (E elt : inny.elty)
        {
            if (czyZawiera(elt))
            {
                tmp.add(elt);
            }
        }

        this.elty = tmp;
    }

    public boolean czyZawiera(Zbiór<? extends E> inny)
    {
        return elty.containsAll(inny.elty);
    }

    public boolean czyZawartyW(Zbiór<? extends E> inny)
    {
        return inny.elty.containsAll(elty);
    }

    @Override
    public boolean equals(Object obj)
    {
        return elty.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return elty.hashCode();
    }
}
