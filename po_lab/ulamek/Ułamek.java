public class Ułamek
{
    // Niezmienniki klasy:
    // + mianownik > 0
    // + nwd(|licznik|, mianownik) = 1
    // + licznik > Integer.MIN_VALUE
    private int licznik;
    private int mianownik;

    public Ułamek(int licznik, int mianownik)
    {
        assert mianownik != 0: "Mianownik nie może być równy 0.";
        assert mianownik > Integer.MIN_VALUE:
            "Mianownik nie może być równy Integer.MIN_VALUE";
        assert licznik > Integer.MIN_VALUE:
            "Licznik nie może być równy Integer.MIN_VALUE";

        if (mianownik < 0)
        {
            licznik *= -1;
            mianownik *= -1;
        }
        this.licznik = licznik;
        this.mianownik = mianownik;
    }
    public String toString()
    {
        return Integer.toString(licznik) + "/" + Integer.toString(mianownik);
    }
}
