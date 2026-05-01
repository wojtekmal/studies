public class Logarytm extends Funkcja
{
    protected Logarytm(Wyrażenie arg)
    {
        this.arg = arg;
    }

    public static Funkcja twórz(Wyrażenie arg)
    {
        return new Logarytm(arg);
    }

    public double wartość(double x)
    {
        return Math.log(arg.wartość(x));
    }

    public Wyrażenie pochodna()
    {
        return arg.pochodna().podzielPrzez(arg);
    }

    public String toString()
    {
        return "log(" + arg.toString() + ")";
    }
}
