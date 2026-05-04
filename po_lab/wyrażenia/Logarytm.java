public class Logarytm extends Funkcja
{
    protected Logarytm(Wyrażenie arg)
    {
        this.arg = arg;
    }

    public static Logarytm twórz(Wyrażenie arg)
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

    protected String symbol()
    {
        return "log";
    }
}
