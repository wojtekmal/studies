public class Zmienna extends Wyrażenie
{
    public static Zmienna twórz()
    {
        return new Zmienna();
    }

    public double wartość(double x)
    {
        return x;
    }

    public Wyrażenie pochodna()
    {
        return Jeden.twórz();
    }
}
