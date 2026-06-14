import java.util.ArrayList;
import java.util.Collection;

public class Zbiór<E>
{
    private Collection<E> elty;

    /**
     * Domyślny konstruktor.
     */
    public Zbiór()
    {
        elty = new ArrayList<E>();
    }

    /**
     * Copy konstruktor.
     * @param kolekcja kolekcja do skopiowania.
     */
    public Zbiór(Collection<? extends E> kolekcja)
    {
        this();

        for (E elt : kolekcja)
        {
            wstaw(elt);
        }
    }

    /**
     * Wstaw element do zbioru. Jeśli zbiór już zawiera ten element, to nic się nie dzieje.
     * @param elt element do wstawienia.
     */
    public void wstaw(E elt)
    {
        if (!elty.contains(elt))
        {
            elty.add(elt);
        }
    }

    /**
     * Konstruktor na podstawie parametrów określających elementy.
     * @param parametry początkowe elementy zbioru.
     */
    public Zbiór(E... parametry)
    {
        this();

        for (E elt : parametry)
        {
            wstaw(elt);
        }
    }

    /**
     * Zamień zbiór na String.
     * @return opis zawartości w formacie [1, 2, 3].
     */
    @Override
    public String toString()
    {
        return elty.toString();
    }

    /**
     * Zwraca true, jeśli zbiór jest pusty.
     * @return true, jeśli zbiór jest pusty, false w.p.p.
     */
    public boolean czyPusty()
    {
        return elty.isEmpty();
    }

    /**
     * Zwraca true, jeśli zbiór zawiera element.
     * @param elt element, o którym chcemy się dowiedzieć, czy zbiór go zawiera.
     * @return true, jeśli zbiór zawiera element, false w.p.p.
     */
    public boolean czyZawiera(E elt)
    {
        return elty.contains(elt);
    }

    /**
     * Zwraca rozmiar zbioru.
     * @return rozmiar zbioru.
     */
    public int rozmiar()
    {
        return elty.size();
    }

    /**
     * Usuwa element ze zbioru. Jeśli zbiór nie zawiera tego elementu, to nic się nie dzieje.
     * @param elt element do usunięcia.
     */
    public void usuń(E elt)
    {
        elty.remove(elt);
    }

    /**
     * Suma dwóch zbiorów. Suma jest umieszczana w this.
     * @param inny zbiór, z którym jest sumowany this.
     */
    public void suma(Zbiór<? extends E> inny)
    {
        elty.addAll(inny.elty);
    }

    /**
     * Różnica dwóch zbiorów. Różnica jest umieszczana w this.
     * @param inny zbiór, który jest odejmowany od this.
     */
    public void różnica(Zbiór<? extends E> inny)
    {
        elty.removeAll(inny.elty);
    }

    /**
     * Przecięcie dwóch zbiorów. Przecięcie jest umieszczane w this.
     * @param inny zbiór, z którym jest przecinany this.
     */
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

    /**
     * Zwraca true, jeśli this zawiera inny zbiór.
     * @param inny zbiór, o który pytamy, czy jest zawarty w this.
     * @return true, jeśli this zawiera inny zbiór, false w.p.p.
     */
    public boolean czyZawiera(Zbiór<?> inny)
    {
        return elty.containsAll(inny.elty);
    }

    /**
     * Zwraca true, jeśli inny zbiór zawiera this.
     * @param inny zbiór, o który pytamy, czy zawiera this.
     * @return true, jeśli inny zbiór zawiera this.
     */
    public boolean czyZawartyW(Zbiór<? extends E> inny)
    {
        return inny.elty.containsAll(elty);
    }

    /**
     * Sprawdza, czy dwa zbiory są równe.
     * @param obj zbiór, o który pytamy, czy jest równy z this.
     * @return true, jeśli zbiory są równe, false w.p.p.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Zbiór<?>)) return false;

        Zbiór<? extends E> inny = (Zbiór<? extends E>) obj;

        return this.czyZawiera(inny) && inny.czyZawiera(this);
    }

    /**
     * Zwraca hash zbioru. Algorytm gwarantuje, że zbiory o tych samych
     * elementach mają równe hashe, niezależnie od kolejności.
     * @return hash od this.
     */
    @Override
    public int hashCode()
    {
        int sum = 0;

        for (E elt : elty)
        {
            sum += elt.hashCode();
        }

        return sum;
    }
}
