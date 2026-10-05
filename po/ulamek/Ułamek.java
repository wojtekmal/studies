public class Ułamek
{
    // Niezmienniki klasy:
    // + mianownik > 0
    // + nwd(|licznik|, mianownik) = 1
    // + licznik > Integer.MIN_VALUE
    private int licznik;
    private int mianownik;

    private static int nwd(int a, int b)
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

        int nwd = nwd(licznik, mianownik);
        licznik /= nwd;
        mianownik /= nwd;

        this.licznik = licznik;
        this.mianownik = mianownik;
    }

    public Ułamek(Ułamek inny)
    {
        this.licznik = inny.licznik();
        this.mianownik = inny.mianownik();
    }

    public String toString()
    {
        if (mianownik == 1) return Integer.toString(licznik);
        
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

    public void dodaj(Ułamek inny)
    {
        int nwd = nwd(mianownik, inny.mianownik());

        assert mianownik / nwd <= Integer.MAX_VALUE / inny.mianownik():
            "Overflow.";
        int nww = mianownik / nwd * inny.mianownik();

        assert inny.mianownik() / nwd <= Integer.MAX_VALUE / Math.abs(licznik):
            "Overflow.";
        int składnik_1 = inny.mianownik() / nwd * licznik;

        assert mianownik / nwd <= Integer.MAX_VALUE / Math.abs(inny.licznik()):
            "Overflow.";
        int składnik_2 = mianownik / nwd * inny.licznik();

        if (Integer.signum(składnik_1) == Integer.signum(składnik_2))
        {
            assert Math.abs(składnik_1) <= 
                Integer.MAX_VALUE - Math.abs(składnik_2): "Overflow.";
        }

        licznik = składnik_1 + składnik_2;
        mianownik = nww;
    }

    public static Ułamek dodaj(Ułamek a, Ułamek b)
    {
        Ułamek wynik = new Ułamek(a);
        wynik.dodaj(b);
        return wynik;
    }

    public void odejmij(Ułamek inny)
    {
        licznik *= -1;
        dodaj(inny);
        licznik *= -1;
    }

    public static Ułamek odejmij(Ułamek a, Ułamek b)
    {
        Ułamek wynik = new Ułamek(a);
        wynik.odejmij(b);
        return wynik;
    }

    public void pomnóż(Ułamek inny)
    {// Trzeba podzielić przez oba nwd.
        int nwd_licznik = nwd(licznik, inny.mianownik());
        int nwd_mianownik = nwd(inny.licznik(), mianownik);

        licznik /= nwd_licznik;
        mianownik /= nwd_mianownik;
        int inny_licznik_skrócony = inny.licznik() / nwd_mianownik;
        int inny_mianownik_skrócony = inny.mianownik() / nwd_licznik;

        assert Math.abs(licznik) <= Integer.MAX_VALUE / inny_licznik_skrócony:
            "Overflow.";
        licznik *= inny_licznik_skrócony;

        assert mianownik <= Integer.MAX_VALUE / inny_mianownik_skrócony:
            "Overflow.";
        mianownik *= inny_mianownik_skrócony;
    }

    public static Ułamek pomnóż(Ułamek a, Ułamek b)
    {
        Ułamek wynik = new Ułamek(a);
        wynik.pomnóż(b);
        return wynik;
    }

    public void odwróć()
    {
        assert licznik != 0: "Nie można dzielić przez 0.";
        int tmp = mianownik;
        mianownik = licznik;
        licznik = tmp;

        if (mianownik < 0)
        {
            licznik *= -1;
            mianownik *= -1;
        }
    }

    public void podziel(Ułamek inny)
    {
        inny.odwróć();
        pomnóż(inny);
        inny.odwróć();
    }

    public static Ułamek podziel(Ułamek a, Ułamek b)
    {
        Ułamek wynik = new Ułamek(a);
        wynik.podziel(b);
        return wynik;
    }

    public boolean czyMniejszeNiż(Ułamek inny)
    {
        int nwd_licznik = nwd(licznik, inny.licznik());
        int nwd_mianownik = nwd(mianownik, inny.mianownik());
        
        assert Math.abs(licznik) / nwd_licznik <= Integer.MAX_VALUE / inny.mianownik():
            "Overflow.";
        int iloczyn_licznik = licznik / nwd_licznik * inny.mianownik();

        assert mianownik / nwd_mianownik <= Integer.MAX_VALUE / Math.abs(inny.licznik()):
            "Overflow.";
        int iloczyn_mianownik = mianownik / nwd_mianownik * inny.licznik();

        return (iloczyn_licznik < iloczyn_mianownik);
    }

    public static boolean czyMniejszeNiż(Ułamek a, Ułamek b)
    {
        return a.czyMniejszeNiż(b);
    }

    public boolean czyRówne(Ułamek inny)
    {
        return (licznik == inny.licznik() && mianownik == inny.mianownik());
    }

    public static boolean czyRówne(Ułamek a, Ułamek b)
    {
        return (a.czyRówne(b));
    }
}
