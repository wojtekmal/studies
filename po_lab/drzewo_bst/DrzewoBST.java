public class DrzewoBST<T extends Comparable<T> & Dodawalny<T>>
{
    private Wartownik<T> wartownik;
    private Węzeł<T> korzeń;

    public DrzewoBST()
    {
        wartownik = new Wartownik<T>();
    }

    public void wstaw(T nowy)
    {
        korzeń.wstaw(nowy);
    }
}
