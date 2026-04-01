public class Ułamek
{
    // Niezmienniki klasy:
    // + mianownik > 0
    // + nwd(|licznik|, mianownik) = 1
    // + licznik > Integer.MIN_VALUE
    private int licznik;
    private int mianownik;

    private static int gcd(int a, int b)
    {
        a = Math.abs(a);
        b = Math.abs(b);

        if (a < b)
        {
            int tmp = a;
            a = b;
            b = tmp;
        }

        while (b > 0)
        {
            a %= b;
            int tmp = a;
            a = b;
            b = tmp;
        }

        return a;
    }

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

        int gcd = gcd(licznik, mianownik);
        licznik /= gcd;
        mianownik /= gcd;

        this.licznik = licznik;
        this.mianownik = mianownik;
    }

    public String toString()
    {
        return Integer.toString(licznik) + "/" + Integer.toString(mianownik);
    }

    public double toDouble()
    {
        return (double) licznik / (double) mianownik;
    }

    public int licznik()
    {
        return licznik;
    }

    public int mianownik()
    {
        return mianownik;
    }

    public Ułamek dodaj(Ułamek inny)
    {
        int gcd = gcd(mianownik, inny.mianownik);

        assert mianownik / gcd <= Integer.MAX_VALUE / inny.mianownik:
            "Overflow.";
        int nww = mianownik / gcd * inny.mianownik;

        assert inny.mianownik / gcd <= Integer.MAX_VALUE / licznik:
            "Overflow.";
        int składnik_1 = inny.mianownik / gcd * licznik;
    }
}
