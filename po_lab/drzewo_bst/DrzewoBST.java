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
        korzeń.wstaw(nowy);
    }

    public Węzeł<T> znajdź(T klucz)
    {
        return korzeń.znajdź(klucz);
    }
}
